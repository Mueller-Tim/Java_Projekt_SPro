
package ch.zhaw.spro.models;

import ch.zhaw.spro.observerinterfaces.IsObservable;
import ch.zhaw.spro.observerinterfaces.IsObserver;
import ch.zhaw.spro.SproApplication;
import ch.zhaw.spro.dbcontrollers.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The SystemDirector class acts as the central coordinator for the application's various components.
 * It facilitates communication between controllers and views and contains the main logic of the application.
 * This class implements the IsObservable interface to enable notification of the view upon state changes.
 */
public class SystemDirector implements IsObservable {

    private final List<IsObserver> listenerList;
    private final EmployeeDbController employeeDbController;
    private final QualificationDbController qualificationDbController;
    private final ShiftDbController shiftDbController;
    private final ShiftTypeDbController shiftTypeDbController;
    private final AbsenceDbController absenceDbController;
    private final ShiftGenerator shiftGenerator;
    private final PdfGenerator pdfGenerator;
    private List<Shift> currentScheduledShifts;
    @Getter
    private List<Employee> searchResult;
    private static final int SATURDAY = 6;
    private static final int SUNDAY = 7;
    public static final ConfigurableApplicationContext runningSpringBootApplication = SpringApplication.run(SproApplication.class);

    /**
     * Constructor for the SystemDirector class. Initializes various controllers and generators,
     * and loads initial data necessary for the application's operation.
     */
    public SystemDirector() {
        this.employeeDbController = runningSpringBootApplication.getBean(EmployeeDbController.class);
        this.qualificationDbController = runningSpringBootApplication.getBean(QualificationDbController.class);
        this.shiftDbController = runningSpringBootApplication.getBean(ShiftDbController.class);
        this.shiftTypeDbController = runningSpringBootApplication.getBean(ShiftTypeDbController.class);
        this.absenceDbController = runningSpringBootApplication.getBean(AbsenceDbController.class);
        this.searchResult = employeeDbController.getAllEmployees();
        this.shiftGenerator = new ShiftGenerator();
        try {
            this.pdfGenerator = new PdfGenerator();
        } catch (Exception e) {
            throw new IllegalStateException("Could not load PDF Generator");
        }
        this.listenerList = new ArrayList<>();
    }



    /**
     * Adds a new employee to the system with specified details.
     *
     * @param avatar Array of bytes representing the avatar image of the employee.
     * @param firstName The first name of the employee.
     * @param lastName The last name of the employee.
     * @param street The street address of the employee.
     * @param city The city where the employee resides.
     * @param ahv The AHV (Swiss social security) number of the employee.
     * @param qualifications List of qualification names for the employee.
     * @param selectedAbsenceDates List of dates representing the employee's absences.
     * @throws IllegalArgumentException if provided data is invalid.
     */
    public void addEmployee(byte[] avatar, String firstName, String lastName, String street, String city, String ahv,List<String> qualifications, List<String> selectedAbsenceDates) {
       try {

        Employee employee = new Employee(firstName, lastName, street, city, ahv, avatar);
           for (Qualification qualification : Objects.requireNonNull(qualificationDbController.getAllQualifications().getBody())) {
               if (qualifications.contains(qualification.getName())) {
                   employee.setQualifications(qualification);
               }
           }

        employeeDbController.addEmployee(employee);
        selectedAbsenceDates.forEach(absenceDate -> absenceDbController.addAbsence(new Absence(employee.getId(), LocalDate.parse(absenceDate))));

        informListener();
       } catch (IllegalArgumentException e) {
           throw new IllegalArgumentException(e.getMessage());
       }
    }

    /**
     * This method allows for the modification of an employee's absences and qualifications.
     * It updates the employee's qualifications based on the provided qualification names and
     * associates the selected absence dates with the employee.
     *
     * @param employee The employee to be edited.
     * @param selectedAbsenceDates List of selected absence dates to be associated with the employee.
     * @param qualifications List of qualification names to be assigned to the employee.
     */
    public void editAbsenceAndQualificationsOfEmployee(Employee employee, List<String> selectedAbsenceDates, List<String> qualifications) {

        ArrayList<String> qualificationIds = new ArrayList<>();
        for (Qualification qualification : Objects.requireNonNull(qualificationDbController.getAllQualifications().getBody())) {
            if (qualifications.contains(qualification.getName())) {
                qualificationIds.add(qualification.getId());
            }
        }
        employeeDbController.setQualificationsForEmployee(new ObjectId(employee.getId()), qualificationIds);
        selectedAbsenceDates.forEach(absenceDate -> absenceDbController.addAbsence(new Absence(employee.getId(), LocalDate.parse(absenceDate))));
        informListener();
    }

    public List<Absence> getAllAbsences() {
        ResponseEntity<List<Absence>> response = absenceDbController.getAllAbsences();
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Employee> getAllEmployee() {
        return employeeDbController.getAllEmployees();
    }

    public List<ShiftType> getShiftType() {
        return shiftTypeDbController.getAllShiftTypes().getBody();
    }

    /**
     * Generates and returns a list of shifts for the next month.
     *
     * @return List of LocalDate objects representing the dates of the next month's shifts.
     */
    public List<LocalDate> getDatesOfNextMonth() {
        List<LocalDate> dates = new ArrayList<>();
        Month nextMonth = LocalDate.now().getMonth().plus(1);
        int year = nextMonth.getValue() == 1 ? LocalDate.now().getYear() + 1 : LocalDate.now().getYear();
        LocalDate date = LocalDate.of(year, nextMonth, 1);
        while (date.getMonth() == nextMonth) {
            dates.add(date);
            date = date.plusDays(1);
        }
        dates.removeIf(date1 -> date1.getDayOfWeek().getValue() == SATURDAY || date1.getDayOfWeek().getValue() == SUNDAY);
        return dates;
    }

    @Override
    public void addListener(IsObserver observer) {
        listenerList.add(observer);
    }

    private void informListener() {
        for (IsObserver observer : listenerList) {
            observer.update();
        }
    }

    /**
     * Searches for employees by name.
     *
     * @param name The name or part of the name to search for.
     * @return List of Employee objects that match the search criteria.
     */
    public List<Employee> searchEmployeeByName(String name) {
        searchResult = employeeDbController.getAllEmployees();
        searchResult.removeIf(employee -> !employee.getFirstName().toLowerCase().concat(" ").concat(employee.getLastName().toLowerCase()).contains(name.toLowerCase()));
        return searchResult;
    }

    /**
     * Searches for employees by their qualifications.
     *
     * @param qualificationName List of qualification names to search for.
     */
    public void searchEmployeeByQualifications(List<String> qualificationName) {
        searchResult = employeeDbController.getAllEmployees();
        for (Qualification qualification : Objects.requireNonNull(qualificationDbController.getAllQualifications().getBody())) {
            if (qualificationName.contains(qualification.getName())) {
                searchResult.removeIf(employee -> !employee.getQualificationIds().contains(qualification.getId()));
            }
        }
        informListener();
    }


    /**
     * Exports a PDF document containing the scheduled shifts.
     *
     * @return List of Strings representing PDF content.
     * @throws IllegalArgumentException if there is an error in creating the PDF.
     */
    public List<String> exportPDF() {
        try {
          return pdfGenerator.createPdf(currentScheduledShifts,getAllEmployee(),getShiftType(),getAllAbsences());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error: Could not create PDF");
        }
    }


    /**
     * Generates a shift plan for the upcoming period.
     *
     * @return List of Shift objects representing the generated shift plan.
     * @throws IllegalArgumentException if there is an issue in generating the shift plan.
     */
    public List<Shift> generateShiftPlan() throws IllegalArgumentException {
        LocalDate firstDayOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        ArrayList<Shift> shiftsToBeScheduled = (ArrayList<Shift>) shiftDbController.getAllShifts().getBody();
        assert shiftsToBeScheduled != null;
        shiftsToBeScheduled.removeIf(shift -> shift.getDate().isBefore(firstDayOfNextMonth));
        ArrayList<Absence> absences = (ArrayList<Absence>) absenceDbController.getAllAbsences().getBody();
        assert absences != null;
        absences.removeIf(absence -> absence.getDate().isBefore(firstDayOfNextMonth));

        currentScheduledShifts = shiftGenerator.generateShiftPlan(shiftsToBeScheduled, employeeDbController.getAllEmployees(), absences);
        for (Shift shift : currentScheduledShifts) {
            shiftDbController.setEmployee(new ObjectId(shift.getId()), shift.getEmployeeId());
        }

        return currentScheduledShifts;
    }

    /**
     * Adds a new shift to the system.
     *
     * @param date The date of the shift.
     * @param time The time of the shift.
     * @param shiftTypeId The type ID of the shift.
     */
    public void addShift(LocalDate date, String time, String shiftTypeId) {
        Shift shift = new Shift(date, time, shiftTypeId);
        shiftDbController.addShift(shift);
    }

    /**
     * Deletes a specific shift from the system.
     *
     * @param shift The Shift object to be deleted.
     */
    public void deleteShift(Shift shift) {
        shiftDbController.deleteShiftById(new ObjectId(shift.getId()));
    }

    /**
     * Retrieves all shifts from the system.
     *
     * @return List of Shift objects.
     */
    public List<Shift> getAllShifts() {
        return shiftDbController.getAllShifts().getBody();
    }

    /**
     * Retrieves the qualifications of an employee.
     *
     * @return List of Strings representing the qualifications of an employee.
     */
    public List<String> getQualificationsFromEmployee() {
        List<Qualification> qualifications = qualificationDbController.getAllQualifications().getBody();
        List<String> qualificationNames = new ArrayList<>();
        assert qualifications != null;
        for (Qualification qualification : qualifications) {
            qualificationNames.add(qualification.getName());
        }
        return qualificationNames;
    }

    /**
     * Retrieves a specific employee by their ID.
     *
     * @param id The ID of the employee.
     * @return The Employee object corresponding to the given ID or null if not found.
     */
    public Employee getEmployee(String id){
        ObjectId idObj = new ObjectId(id);
        ResponseEntity<Optional<Employee>> response = employeeDbController.getEmployeeById(idObj);

        if (response.getStatusCode() == HttpStatus.OK) {
            Optional<Employee> employeeOptional = response.getBody();
            assert Objects.requireNonNull(employeeOptional).isPresent();
            return employeeOptional.orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Deletes a specific employee from the system.
     *
     * @param currentEmployee The Employee object to be deleted.
     */
    public void deleteEmployee(Employee currentEmployee) {
        ObjectId idObj = new ObjectId(currentEmployee.getId());
        absenceDbController.getAllAbsencesByEmployeeId(currentEmployee.getId()).forEach(absence -> absenceDbController.deleteAbsenceById(new ObjectId(absence.getId())));
        employeeDbController.deleteEmployeeById(idObj);
        informListener();
    }

    /**
     * Retrieves all absences of a specific employee.
     *
     * @param employee The employee whose absences are to be retrieved.
     * @return List of Absence objects.
     */
    public List<Absence> getAbsences(Employee employee){
        return absenceDbController.getAllAbsencesByEmployeeId(employee.getId());
    }

    /**
     * Retrieves qualifications of a specific employee.
     *
     * @param employee The employee whose qualifications are to be retrieved.
     * @return List of Qualification objects.
     */
    public List<Qualification> getQualificationsFromEmployee(Employee employee){
        List<Qualification> qualifications = qualificationDbController.getAllQualifications().getBody();
        List<Qualification> employeeQualifications = new ArrayList<>();
        assert qualifications != null;
        for (Qualification qualification : qualifications) {
            if (employee.getQualificationIds().contains(qualification.getId())) {
                employeeQualifications.add(qualification);
            }
        }
        return employeeQualifications;
    }
}
