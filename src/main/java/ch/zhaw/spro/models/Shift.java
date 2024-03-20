package ch.zhaw.spro.models;

import ch.zhaw.spro.dbcontrollers.ShiftTypeDbController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a shift in the scheduling system.
 * It is used to model the details of a work shift including its date, time, type, and the associated employee.
 */
@Document(collection = "shifts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Shift {

    @Id
    private String id;
    private LocalDate date;
    private String time;
    private String shiftTypeId;
    private String employeeId;

    /**
     * Constructs a new Shift with the specified date, time, shift type ID, and employee ID.
     *
     * @param date         The date of the shift.
     * @param time         The time of the shift.
     * @param shiftTypeId  The ID of the shift type.
     * @param employeeId   The ID of the employee assigned to the shift.
     */
    public Shift(LocalDate date, String time, String shiftTypeId, String employeeId) {
        this.date = date;
        this.time = time;
        this.shiftTypeId = shiftTypeId;
        this.employeeId = employeeId;
    }

    /**
     * Constructs a new Shift with the specified date, time, and shift type ID. The employee ID is not set.
     *
     * @param date        The date of the shift.
     * @param time        The time of the shift.
     * @param shiftTypeId The ID of the shift type.
     */
    public Shift(LocalDate date, String time, String shiftTypeId) {
        this.date = date;
        this.time = time;
        this.shiftTypeId = shiftTypeId;
    }

    /**
     * Retrieves the {@link ShiftType} object associated with this shift.
     *
     * @return The {@link ShiftType} object, or null if not found.
     */
    public ShiftType getShiftType(){
        return Objects.requireNonNull(SystemDirector.runningSpringBootApplication.getBean(ShiftTypeDbController.class).
                getShiftTypeById(new ObjectId(this.shiftTypeId)).getBody()).orElse(null);
    }

    /**
     * Sets the employee for this shift and returns the updated shift object.
     *
     * @param employee The {@link Employee} object to be associated with this shift.
     * @return The updated {@link Shift} object.
     */
    public Shift setEmployee(Employee employee){
        this.employeeId = employee.getId();
        return this;
    }

}
