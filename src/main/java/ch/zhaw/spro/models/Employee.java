package ch.zhaw.spro.models;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an employee in a company or organization. This class stores and manages
 * personal and professional details of an employee. It includes features to validate
 * and set employee details such as name, address, AHV number, and qualifications.
 */
@Getter
@Document(collection = "employees")
public class Employee {

	@Id
	private String id;
	private final String firstName;
	private String lastName;
	private String street;
	private String city;
	private final String ahvNumber;
	private final byte[] image;
	private String[] qualifications;
	@Transient
	private static final String NAME_REGEX = "[A-ZÜÖÄ][a-züöä]+";
	@Transient
	private static final String NUMBER_REGEX = "[1-9]+";



	/**
	 * Constructs an Employee object with the specified attributes.
	 *
	 * @param firstName The first name of the employee.
	 * @param lastName  The last name of the employee.
	 * @param street    The address of the employee.
	 * @param city      The city where the employee resides.
	 * @param ahvNumber The AHV (Social Security) number of the employee.
	 * @param image     The image representing the employee.
	 *
	 * @throws IllegalArgumentException if any of the provided attributes do not meet the validation criteria.
	 * This exception is thrown in the following cases:
	 * - If the AHV number format is invalid. It should follow the format "756.XXXX.XXXX.XX" where X represents digits.
	 * - If the first name does not match the required pattern. It must start with an uppercase letter,
	 * followed by one or more lowercase letters.
	 * - If the last name format is invalid. It must start with an uppercase letter,
	 * followed by one or more lowercase letters.
	 * - If the street format is invalid. The street should have at least two letters
	 * followed by a space and a number greater than 0.
	 * - If the city format is invalid. The city should have the format "1234 City"
	 * where "City" contains at least two characters, starts with an uppercase letter, and only contains letters.
	 */
	public Employee(String firstName, String lastName, String street, String city, String ahvNumber, byte[] image) {
		this.firstName = validateName(firstName);
		setLastName(lastName);
		setStreet(street);
		setCity(city);
		this.ahvNumber = validateAhvNr(ahvNumber);
		qualifications = new String[6];
		this.image = image;
	}

	/**
	 * Sets the last name of the employee.
	 *
	 * @param lastName The last name of the employee to be set.
	 * @throws IllegalArgumentException if the provided last name does not meet the validation criteria.
	 * It must start with an uppercase letter, followed by one or more lowercase letters.
	 */
	public void setLastName(String lastName) {
		this.lastName = validateName(lastName);
	}

	/**
	 * Sets the street address of the employee.
	 *
	 * @param street The street address to be set.
	 * @throws IllegalArgumentException if the provided street address does not meet the validation criteria.
	 * The street should have at least two letters followed by a space and a number greater than 0.
	 */
	public void setStreet(String street) {
		this.street = validateStreet(street);
	}

	/**
	 * Sets the city where the employee resides.
	 *
	 * @param city The city to be set.
	 * @throws IllegalArgumentException if the provided city does not meet the validation criteria.
	 * The city should have the format "1234 City" where "City" contains at least two characters,
	 * starts with an uppercase letter, and only contains letters.
	 */
	public void setCity(String city) {
		this.city = validateCity(city);
	}

	public void setQualifications(Qualification qualification) {
		for (int i = 0; i < qualifications.length; i++) {
			if (qualifications[i] == null) {
				qualifications[i] = qualification.getId();
				break;
			}
		}
	}

	public void setQualifications(String qualificationId) {
		for (int i = 0; i < qualifications.length; i++) {
			if (qualifications[i] == null || qualifications[i].equals(qualificationId)) {
				qualifications[i] = qualificationId;
				break;
			}
		}
	}
	public List<String> getQualificationIds(){

		return Arrays.stream(qualifications).toList();
	}

	public void clearQualifications(){
		qualifications = new String[6];
	}

	private String validateAhvNr(String ahvNumber){
		int firstDot = 3;
		int secondDot = 8;
		int thirdDot = 13;
		boolean validation = ahvNumber.charAt(firstDot) == '.' && ahvNumber.charAt(secondDot) == '.' && ahvNumber.charAt(thirdDot) == '.';
        String ahvNr = ahvNumber.replace(".", "");
		int ahvNrLength = 13;
		char firstAhvNr = '7';
		char secondAhvNr = '5';
		char thirdAhvNr = '6';
		if (ahvNr.length() != ahvNrLength || ahvNr.charAt(0) != firstAhvNr || ahvNr.charAt(1) != secondAhvNr || ahvNr.charAt(2) != thirdAhvNr) {
			validation = false;
		}

		for (char c : ahvNr.toCharArray()) {
			if (!Character.isDigit(c)) {
				validation = false;
			}
		}
		if(!validation){
			throw new IllegalArgumentException("Invalid AHV number format. Please use the format 756.XXXX.XXXX.XX " +
					"where X represents digits.");
		}
		return ahvNumber;
	}

	private String validateName(String name){
		if(!name.matches(NAME_REGEX)){
			throw new IllegalArgumentException("Name must start with an uppercase letter, followed by one or more " +
					"lowercase letters.");
		}
		return name;
	}

	private String validateStreet(String street) {
		boolean validation = street.contains(" ");
        String[] splitStreet = street.split(" ");
		for (int i = 0; i < splitStreet.length - 1; i++) {
            if (!splitStreet[0].matches(NAME_REGEX)) {
                validation = false;
                break;
            }
		}
		if (!splitStreet[splitStreet.length - 1].matches(NUMBER_REGEX)) {
			validation = false;
		}
		if(!validation){
			throw new IllegalArgumentException("The street name should have at least two letters " +
					"followed by a space and a number greater than 0.");
		}
		return street;
	}

	private String validateCity(String city) {
		boolean validation = true;
		int minCityLength = 7;
		if(city.length() >= minCityLength){
			int startCityName = 5;
			String cityName = city.substring(startCityName);
			if (city.charAt(4) != ' ' || !cityName.matches(NAME_REGEX)) {
				validation = false;
			}
			int plzLength = 4;
			for (int i = 0; i < plzLength; i++) {
				if (!Character.isDigit(city.charAt(i))) {
					validation = false;
				}
			}
		} else{
			validation = false;
		}

		if(!validation){
			throw new IllegalArgumentException("Invalid city format. The city should have the format: '1234 City', " +
					"where 'City' contains at least two characters, " +
					"starts with an uppercase letter, and only contains letters.");
		}
		return city;
	}
}
