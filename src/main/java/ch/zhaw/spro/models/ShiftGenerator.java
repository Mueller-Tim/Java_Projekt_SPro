package ch.zhaw.spro.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * This class is responsible for generating a shift plan based on the given shifts and employees.
 */
public class ShiftGenerator {
    private HashMap<Shift, ArrayList<Employee>> availableEmployees;
    private HashMap<Employee, ArrayList<Absence>> employeeAbsences;

    /**
     * Generates a shift plan based on the given shifts and employees.
     * @param shifts The shifts to be scheduled
     * @param employees The employees to be assigned to the shifts
     * @return The scheduled shifts with assigned employees
     */
    public List<Shift> generateShiftPlan(List<Shift> shifts, List<Employee> employees, List<Absence> absences) throws IllegalArgumentException{
        if (shifts.isEmpty()) {
            throw new IllegalArgumentException("No shifts to schedule");
        }
        for (Shift shift : shifts) {
            shift.setEmployeeId(null);
        }
        initialize(shifts);
        assembleEmployeesAbsences(employees, absences);
        assembleQualifiedEmployees(shifts, employees);
        checkForAvailableEmployees();
        return scheduleShifts();
    }

    private void checkForAvailableEmployees() {
        Shift shift;
        List<Employee> employeesInShift;
        Iterator<Shift> iteratorShifts = availableEmployees.keySet().iterator();
        Iterator<Employee> iteratorEmployees;
        Employee employee;
        while (iteratorShifts.hasNext()) {
            shift = iteratorShifts.next();
            employeesInShift = availableEmployees.get(shift);
            iteratorEmployees = employeesInShift.iterator();
            while (iteratorEmployees.hasNext()) {
                employee = iteratorEmployees.next();
                for (Absence absence : employeeAbsences.get(employee)) {
                    if (absence.getDate().equals(shift.getDate())) {
                        iteratorEmployees.remove();
                        break;
                    }
                }
            }
        }
    }

    private void initialize(List<Shift> shifts) {
        availableEmployees = new HashMap<>();
        for (Shift shift : shifts) {
            availableEmployees.put(shift, new ArrayList<>());
        }
    }

    private void assembleQualifiedEmployees(List<Shift> shifts, List<Employee> employees) {
        for (Shift shift : shifts) {
            List<String> shiftQualifications = shift.getShiftType().getQualificationIds();
            for (Employee employee : employees) {
                if(employee.getQualificationIds().containsAll(shiftQualifications)){
                    availableEmployees.get(shift).add(employee);
                }

            }
        }
    }

    private void assembleEmployeesAbsences(List<Employee> employees, List<Absence> absences) {
        employeeAbsences = new HashMap<>();
        for (Employee employee : employees) {
            employeeAbsences.put(employee, new ArrayList<>());
        }
        for (Absence absence : absences) {
            for (Employee employee : employees) {
                if (absence.getEmployeeId().equals(employee.getId())) {
                    employeeAbsences.get(employee).add(absence);
                }
            }
        }
    }

    private void checkIsSchedulingPossible() throws IllegalArgumentException{
        for (Shift shift : availableEmployees.keySet()) {
            if(availableEmployees.get(shift).isEmpty()){
                throw new IllegalArgumentException("No employees available for shift " + shift.getDate() + " " + shift.getTime() );
            }
        }
    }

    private ArrayList<Shift> scheduleShifts(){
        ArrayList<Shift> scheduledShifts = new ArrayList<>();
        while(!availableEmployees.isEmpty()) {
            checkIsSchedulingPossible();
            assignEmployeeWithLeastShifts(getShiftWithLeastAvailableEmployees(), scheduledShifts);
        }
        return scheduledShifts;
    }

    private Shift getShiftWithLeastAvailableEmployees() throws IllegalArgumentException{
        Shift shiftWithLeastAvailableEmployees = null;
        int numberOfEmployeesForShift = Integer.MAX_VALUE;
        for (Shift shift: availableEmployees.keySet()){
            if (availableEmployees.get(shift).size() < numberOfEmployeesForShift){
                numberOfEmployeesForShift = availableEmployees.get(shift).size();
                shiftWithLeastAvailableEmployees = shift;
            }
        }
        if (shiftWithLeastAvailableEmployees == null){
            throw new IllegalArgumentException("No shifts to schedule");
        }
        return shiftWithLeastAvailableEmployees;
    }

    private void assignEmployeeWithLeastShifts(Shift shift, List<Shift> scheduledShifts){
        HashMap<Employee, Integer> employeeNumberShifts = new HashMap<>();

        for (Employee employee: availableEmployees.get(shift)){
            employeeNumberShifts.put(employee, 0);
        }

        for (Employee employee: availableEmployees.get(shift)){
            for (Shift scheduledShift: scheduledShifts){
                if (employee.getId().equals(scheduledShift.getEmployeeId())){
                    employeeNumberShifts.put(employee, employeeNumberShifts.get(employee) + 1);
                }
            }
        }

        Employee employeeWithLeastShifts = null;
        int numberOfShifts = Integer.MAX_VALUE;
        for (Employee employee: employeeNumberShifts.keySet()){
            if (employeeNumberShifts.get(employee) < numberOfShifts){
                numberOfShifts = employeeNumberShifts.get(employee);
                employeeWithLeastShifts = employee;
            }
        }
        availableEmployees.remove(shift);
        shift.setEmployee(Objects.requireNonNull(employeeWithLeastShifts));
        scheduledShifts.add(shift);

        removeEmployeeFromConcurrentShift(employeeWithLeastShifts, shift);

    }

    private void removeEmployeeFromConcurrentShift(Employee employee, Shift shift){
        LocalDate shiftDate = shift.getDate();
        String time = shift.getTime();

        for (Shift concurrentShift: availableEmployees.keySet()){
            if (concurrentShift.getDate().equals(shiftDate) && concurrentShift.getTime().equals(time)){
                availableEmployees.get(concurrentShift).remove(employee);
            }
        }
    }


}

