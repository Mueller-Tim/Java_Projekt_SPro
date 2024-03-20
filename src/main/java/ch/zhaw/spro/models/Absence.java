package ch.zhaw.spro.models;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * The Absence class represents an absence or leave taken by an employee.
 * It includes information about the employee's identifier and the date of the absence.
 * <p>
 * This class is a part of the persistence layer and is mapped to a MongoDB collection named "absences".
 * </p>
 */
@Document(collection = "absences")
@Getter
public class Absence{

	@Id
	private String id;
	@Getter
	private final String employeeId;
	private final LocalDate date;

	/**
	 * Constructs an Absence object with the specified employee identifier and date.
	 *
	 * @param employeeId The unique identifier of the employee taking the absence.
	 * @param date       The date of the absence.
	 */
	public Absence(String employeeId, LocalDate date) {
		this.employeeId = employeeId;
		this.date = date;
	}

}
