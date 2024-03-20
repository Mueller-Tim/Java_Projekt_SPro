package ch.zhaw.spro;

import ch.zhaw.spro.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * This class tests the ShiftGenerator class.
 */
class ShiftGeneratorTest {
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Shift> shifts = new ArrayList<>();
    private ArrayList<String> qualificationIds1 = new ArrayList<>();
    private ArrayList<String> qualificationIds2 = new ArrayList<>();
    private ArrayList<String> qualificationIds3 = new ArrayList<>();
    private ArrayList<String> qualificationIds12 = new ArrayList<>();
    private ArrayList<String> qualificationIds13 = new ArrayList<>();
    private ArrayList<String> qualificationIds23 = new ArrayList<>();
    private Qualification qualification1;
    private Qualification qualification2;
    private Qualification qualification3;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private ShiftType shiftType1;
    private ShiftType shiftType2;
    private ShiftType shiftType3;
    private ShiftType shiftType12;
    private ShiftType shiftType13;
    private ShiftType shiftType23;
    private MockShift shift1;
    private MockShift shift2;
    private MockShift shift3;
    private MockShift shift4;
    private MockShift shift5;
    private MockShift shift6;
    private MockShift shift7;
    private MockShift shift8;
    private MockShift shift9;
    private MockShift shift10;
    private Absence absence1;
    private Absence absence2;
    private Absence absence3;
    private Absence absence4;
    private ArrayList<Absence> absences = new ArrayList<>();
    private ShiftGenerator shiftGenerator;

    /**
     * This method is called before each test.
     * It sets up the mocks and the test data.
     */

    @BeforeEach
    void setUp() {
        employees = new ArrayList<>();
        shifts = new ArrayList<>();

        employee1 = Mockito.mock(Employee.class);
        employee2 = Mockito.mock(Employee.class);
        employee3 = Mockito.mock(Employee.class);

        absence1 = Mockito.mock(Absence.class);
        absence2 = Mockito.mock(Absence.class);
        absence3 = Mockito.mock(Absence.class);
        absence4 = Mockito.mock(Absence.class);

        when(absence1.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(absence1.getEmployeeId()).thenReturn("");
        when(absence2.getDate()).thenReturn(LocalDate.of(2023,5,2));
        when(absence2.getEmployeeId()).thenReturn("");
        when(absence3.getDate()).thenReturn(LocalDate.of(2023,5,3));
        when(absence3.getEmployeeId()).thenReturn("");
        when(absence4.getDate()).thenReturn(LocalDate.of(2023,5,4));
        when(absence4.getEmployeeId()).thenReturn("");

        absences = new ArrayList<>();
        absences.add(absence1);
        absences.add(absence2);
        absences.add(absence3);
        absences.add(absence4);

        qualification1 = Mockito.mock(Qualification.class);
        qualification2 = Mockito.mock(Qualification.class);
        qualification3 = Mockito.mock(Qualification.class);

        when(qualification1.getId()).thenReturn("qualification1");
        when(qualification2.getId()).thenReturn("qualification2");
        when(qualification3.getId()).thenReturn("qualification3");

        qualificationIds1 = new ArrayList<>();
        qualificationIds2 = new ArrayList<>();
        qualificationIds3 = new ArrayList<>();
        qualificationIds12 = new ArrayList<>();
        qualificationIds13 = new ArrayList<>();
        qualificationIds23 = new ArrayList<>();

        qualificationIds1.add(qualification1.getId());
        qualificationIds2.add(qualification2.getId());
        qualificationIds3.add(qualification3.getId());
        qualificationIds12.add(qualification1.getId());
        qualificationIds12.add(qualification2.getId());
        qualificationIds13.add(qualification1.getId());
        qualificationIds13.add(qualification3.getId());
        qualificationIds23.add(qualification2.getId());
        qualificationIds23.add(qualification3.getId());

        shiftType1 = Mockito.mock(ShiftType.class);
        shiftType2 = Mockito.mock(ShiftType.class);
        shiftType3 = Mockito.mock(ShiftType.class);
        shiftType12 = Mockito.mock(ShiftType.class);
        shiftType13 = Mockito.mock(ShiftType.class);
        shiftType23 = Mockito.mock(ShiftType.class);

        when(shiftType1.getQualificationIds()).thenReturn(qualificationIds1);
        when(shiftType2.getQualificationIds()).thenReturn(qualificationIds2);
        when(shiftType3.getQualificationIds()).thenReturn(qualificationIds3);
        when(shiftType12.getQualificationIds()).thenReturn(qualificationIds12);
        when(shiftType13.getQualificationIds()).thenReturn(qualificationIds13);
        when(shiftType23.getQualificationIds()).thenReturn(qualificationIds23);

        shift1 = Mockito.mock(MockShift.class);
        shift2 = Mockito.mock(MockShift.class);
        shift3 = Mockito.mock(MockShift.class);
        shift4 = Mockito.mock(MockShift.class);
        shift5 = Mockito.mock(MockShift.class);
        shift6 = Mockito.mock(MockShift.class);
        shift7 = Mockito.mock(MockShift.class);
        shift8 = Mockito.mock(MockShift.class);
        shift9 = Mockito.mock(MockShift.class);
        shift10 = Mockito.mock(MockShift.class);


        shiftGenerator = new ShiftGenerator();
    }

    /**
     * Description: Tests the generateShiftPlan method of the ShiftGenerator class.
     * Equivalence class: Valid inputs.
     * Positive testing.
     * Initial state: Shifts to be scheduled and employees are given.
     * Expected output: List of shifts with employees assigned.
     */
    @Test
    void generateShiftPlan() {
        when(employee1.getQualificationIds()).thenReturn(qualificationIds12);
        when(absence4.getEmployeeId()).thenReturn("employee1");
        when(employee1.getId()).thenReturn("employee1");

        when(employee2.getQualificationIds()).thenReturn(qualificationIds13);
        when(absence3.getEmployeeId()).thenReturn("employee2");
        when(employee2.getId()).thenReturn("employee2");

        when(employee3.getQualificationIds()).thenReturn(qualificationIds23);
        when(absence2.getEmployeeId()).thenReturn("employee3");
        when(employee3.getId()).thenReturn("employee3");


        when(shift1.getShiftType()).thenReturn(shiftType1);
        when(shift1.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift1.getTime()).thenReturn("Morning");
        when(shift1.setEmployee(employee1)).thenCallRealMethod();
        when(shift1.setEmployee(employee2)).thenCallRealMethod();
        when(shift1.setEmployee(employee3)).thenCallRealMethod();
        when(shift1.getEmployee()).thenCallRealMethod();
        when(shift1.getEmployeeId()).thenCallRealMethod();

        when(shift2.getShiftType()).thenReturn(shiftType23);
        when(shift2.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift2.getTime()).thenReturn("Afternoon");
        when(shift2.setEmployee(employee1)).thenCallRealMethod();
        when(shift2.setEmployee(employee2)).thenCallRealMethod();
        when(shift2.setEmployee(employee3)).thenCallRealMethod();
        when(shift2.getEmployee()).thenCallRealMethod();
        when(shift2.getEmployeeId()).thenCallRealMethod();

        when(shift3.getShiftType()).thenReturn(shiftType13);
        when(shift3.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift3.getTime()).thenReturn("Morning");
        when(shift3.setEmployee(employee1)).thenCallRealMethod();
        when(shift3.setEmployee(employee2)).thenCallRealMethod();
        when(shift3.setEmployee(employee3)).thenCallRealMethod();
        when(shift3.getEmployee()).thenCallRealMethod();
        when(shift3.getEmployeeId()).thenCallRealMethod();

        when(shift4.getShiftType()).thenReturn(shiftType1);
        when(shift4.getDate()).thenReturn(LocalDate.of(2023,5,2));
        when(shift4.getTime()).thenReturn("Morning");
        when(shift4.setEmployee(employee1)).thenCallRealMethod();
        when(shift4.setEmployee(employee2)).thenCallRealMethod();
        when(shift4.setEmployee(employee3)).thenCallRealMethod();
        when(shift4.getEmployee()).thenCallRealMethod();
        when(shift4.getEmployeeId()).thenCallRealMethod();

        when(shift5.getShiftType()).thenReturn(shiftType12);
        when(shift5.getDate()).thenReturn(LocalDate.of(2023,5,2));
        when(shift5.getTime()).thenReturn("Afternoon");
        when(shift5.setEmployee(employee1)).thenCallRealMethod();
        when(shift5.setEmployee(employee2)).thenCallRealMethod();
        when(shift5.setEmployee(employee3)).thenCallRealMethod();
        when(shift5.getEmployee()).thenCallRealMethod();
        when(shift5.getEmployeeId()).thenCallRealMethod();

        when(shift6.getShiftType()).thenReturn(shiftType3);
        when(shift6.getDate()).thenReturn(LocalDate.of(2023,5,2));
        when(shift6.getTime()).thenReturn("Morning");
        when(shift6.setEmployee(employee1)).thenCallRealMethod();
        when(shift6.setEmployee(employee2)).thenCallRealMethod();
        when(shift6.setEmployee(employee3)).thenCallRealMethod();
        when(shift6.getEmployee()).thenCallRealMethod();
        when(shift6.getEmployeeId()).thenCallRealMethod();

        when(shift7.getShiftType()).thenReturn(shiftType1);
        when(shift7.getDate()).thenReturn(LocalDate.of(2023,5,3));
        when(shift7.getTime()).thenReturn("Morning");
        when(shift7.setEmployee(employee1)).thenCallRealMethod();
        when(shift7.setEmployee(employee2)).thenCallRealMethod();
        when(shift7.setEmployee(employee3)).thenCallRealMethod();
        when(shift7.getEmployee()).thenCallRealMethod();
        when(shift7.getEmployeeId()).thenCallRealMethod();

        when(shift8.getShiftType()).thenReturn(shiftType23);
        when(shift8.getDate()).thenReturn(LocalDate.of(2023,5,3));
        when(shift8.getTime()).thenReturn("Afternoon");
        when(shift8.setEmployee(employee1)).thenCallRealMethod();
        when(shift8.setEmployee(employee2)).thenCallRealMethod();
        when(shift8.setEmployee(employee3)).thenCallRealMethod();
        when(shift8.getEmployee()).thenCallRealMethod();
        when(shift8.getEmployeeId()).thenCallRealMethod();

        when(shift9.getShiftType()).thenReturn(shiftType3);
        when(shift9.getDate()).thenReturn(LocalDate.of(2023,5,3));
        when(shift9.getTime()).thenReturn("Morning");
        when(shift9.setEmployee(employee1)).thenCallRealMethod();
        when(shift9.setEmployee(employee2)).thenCallRealMethod();
        when(shift9.setEmployee(employee3)).thenCallRealMethod();
        when(shift9.getEmployee()).thenCallRealMethod();
        when(shift9.getEmployeeId()).thenCallRealMethod();

        when(shift10.getShiftType()).thenReturn(shiftType1);
        when(shift10.getDate()).thenReturn(LocalDate.of(2023,5,4));
        when(shift10.getTime()).thenReturn("Morning");
        when(shift10.setEmployee(employee1)).thenCallRealMethod();
        when(shift10.setEmployee(employee2)).thenCallRealMethod();
        when(shift10.setEmployee(employee3)).thenCallRealMethod();
        when(shift10.getEmployee()).thenCallRealMethod();
        when(shift10.getEmployeeId()).thenCallRealMethod();

        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        shifts.add(shift1);
        shifts.add(shift2);
        shifts.add(shift3);
        shifts.add(shift4);
        shifts.add(shift5);
        shifts.add(shift6);
        shifts.add(shift7);
        shifts.add(shift8);
        shifts.add(shift9);
        shifts.add(shift10);

        assertDoesNotThrow(() -> shiftGenerator.generateShiftPlan(shifts, employees, absences));
        for (Shift shift : shifts) {
            assert(((MockShift)shift).getEmployee() != null);
        }

        for (Shift shift : shifts) {
            for (Shift shift2 : shifts) {
                if (shift.equals(shift2)) continue;
                assert(!shift.getDate().equals(shift2.getDate()) || !shift.getTime().equals(shift2.getTime()) || !((MockShift)shift).getEmployee().equals(((MockShift)shift2).getEmployee()));
            }
            switch (shift.getEmployeeId()){
                case "employee1":
                    assert(!(absence4.getDate().equals(shift.getDate())));
                    break;
                case "employee2":
                    assert(!(absence3.getDate().equals(shift.getDate())));
                    break;

                case "employee3":
                    assert(!(absence2.getDate().equals(shift.getDate())));
                    break;
            }
        }

        for (Shift shift : shifts) {
            System.out.println(shift.getDate() + " " + shift.getTime() + " " + ((MockShift)shift).getEmployee().getId());
        }
    }

    /**
     * Description: Tests the generateShiftPlan method of the ShiftGenerator class.
     * Equivalence class: Not Enough Employees to schedule all shifts.
     * Negative testing.
     * Initial state: Shifts to be scheduled and employees are given.
     * Expected output: IllegalArgumentException.
     */
    @Test
    void generateShiftPlanWithNotEnoughEmployees(){
        when(employee1.getQualificationIds()).thenReturn(qualificationIds1);

        when(shift1.getShiftType()).thenReturn(shiftType1);
        when(shift1.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift1.getTime()).thenReturn("Morning");

        when(shift2.getShiftType()).thenReturn(shiftType1);
        when(shift2.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift2.getTime()).thenReturn("Morning");

        employees.add(employee1);

        shifts.add(shift1);
        shifts.add(shift2);

        assertThrows(IllegalArgumentException.class, () -> shiftGenerator.generateShiftPlan(shifts, employees, absences));

    }

    /**
     * Description: Tests the generateShiftPlan method of the ShiftGenerator class.
     * Equivalence class: Employees are all absent during shift to be scheduled.
     * Negative testing.
     * Initial state: Shifts to be scheduled and employees are given.
     * Expected output: IllegalArgumentException.
     */
    @Test
    void generateShiftPlanWithNoAvailableEmployees(){
        when(employee1.getQualificationIds()).thenReturn(qualificationIds1);
        when(employee1.getId()).thenReturn("employee1");
        when(absence1.getEmployeeId()).thenReturn("employee1");

        when(shift1.getShiftType()).thenReturn(shiftType1);
        when(shift1.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift1.getTime()).thenReturn("Morning");

        when(shift2.getShiftType()).thenReturn(shiftType1);
        when(shift2.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift2.getTime()).thenReturn("Afternoon");

        employees.add(employee1);

        shifts.add(shift1);
        shifts.add(shift2);

        assertThrows(IllegalArgumentException.class, () -> shiftGenerator.generateShiftPlan(shifts, employees, absences));
    }

    /**
     * Description: Tests the generateShiftPlan method of the ShiftGenerator class.
     * Equivalence class: Shifts to be scheduled are empty.
     * Negative testing.
     * Initial state: Shifts to be scheduled and employees are given.
     * Expected output: IllegalArgumentException.
     */
    @Test
    void generateShiftPlanWithNoShifts(){
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        assertThrows(IllegalArgumentException.class, () -> shiftGenerator.generateShiftPlan(shifts, employees, absences));
    }

    /**
     * Description: Tests the generateShiftPlan method of the ShiftGenerator class.
     * Equivalence class: Shifts to be scheduled are null.
     * Negative testing.
     * Initial state: Shifts to be scheduled and employees are given.
     * Expected output: IllegalArgumentException.
     */
    @Test
    void generateShiftPlanNoMatchingQualifications(){
        when(employee1.getQualificationIds()).thenReturn(qualificationIds1);

        when(employee2.getQualificationIds()).thenReturn(qualificationIds2);

        when(employee3.getQualificationIds()).thenReturn(qualificationIds3);

        when(shift1.getShiftType()).thenReturn(shiftType12);
        when(shift1.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift1.getTime()).thenReturn("Morning");

        when(shift2.getShiftType()).thenReturn(shiftType23);
        when(shift2.getDate()).thenReturn(LocalDate.of(2023,5,1));
        when(shift2.getTime()).thenReturn("Afternoon");

        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        shifts.add(shift1);
        shifts.add(shift2);

        assertThrows(IllegalArgumentException.class, () -> shiftGenerator.generateShiftPlan(shifts, employees, absences));
    }

    /**
     * Inner class to test shifts without effecting the database.
     */
    class MockShift extends Shift {
        private Employee employee;
        public Employee getEmployee(){
            return employee;
        }
        public String getEmployeeId(){
            return employee.getId();
        }
        public Shift setEmployee(Employee employee){
            this.employee = employee;
            return this;
        }

    }
}