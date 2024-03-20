package ch.zhaw.spro;

import ch.zhaw.spro.models.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * This class contains JUnit tests for the Employee class, ensuring that valid and invalid inputs
 * are handled correctly according to predefined rules for employee details.
 */
class EmployeeTests {
	private final String validFirstName = "John";
	private final String validLastName = "Doe";
	private final String validStreet = "Technikumstrasse 9";
	private final String validCity = "8400 Winterthur";
	private final String validAhvNumber = "756.1234.5678.90";
	private final byte[] validImage = new byte[1];

	/**
	 * description: test if no IllegalArgumentException has been thrown,when the first name starts with an uppercase
	 * 	 				letter, followed by one or more lowercase letters.
	 * initial condition: nothing
	 * type: positive test
	 * input: valid lastname, street, city, ahv number and image
	 * output: does not throw an IllegalArgumentException
	 */
	@Test
	void testValidFistName() {
		assertDoesNotThrow(() -> new Employee("Aa", validLastName, validStreet,
				validCity, validAhvNumber, validImage));
		assertDoesNotThrow(() -> new Employee("Üaüaüa", validLastName, validStreet,
				validCity, validAhvNumber, validImage));

	}


/**
	 * description: test if an IllegalArgumentException has been thrown, when the first name doesn't start with an
	 * 				uppercase letter, followed by one or more lowercase letters.
	 * initial condition: nothing
	 * type: negative test
	 * input: valid lastname, street, city, ahv number and image
	 * output: Throws an IllegalArgumentException
	 */
	@Test
	void testInvalidFistName() {

		assertThrows(IllegalArgumentException.class, () -> new Employee("1", validLastName,
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(" ", validLastName,
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee("A", validLastName,
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee("a", validLastName,
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee("aa", validLastName,
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee("aA", validLastName,
				validStreet, validCity, validAhvNumber, validImage));
	}


/**
	 * description: test if no IllegalArgumentException has been thrown,when the lastname starts with an uppercase
	 * 	 				letter, followed by one or more lowercase letters.
	 * initial condition: nothing
	 * type: positive test
	 * input: valid first name, street, city, ahv number and image
	 * output: does not throw an IllegalArgumentException
	 */
	@Test
	void testValidLastName() {
		assertDoesNotThrow(() -> new Employee(validFirstName, "Aa", validStreet,
				validCity, validAhvNumber, validImage));
		assertDoesNotThrow(() -> new Employee(validFirstName, "Üaüaüa", validStreet,
				validCity, validAhvNumber, validImage));

	}


/**
	 * description: test if an IllegalArgumentException has been thrown, when the lastname doesn't start with an
	 * 				uppercase letter, followed by one or more lowercase letters.
	 * initial condition: nothing
	 * type: negative test
	 * input: valid first name, street, city, ahv number and image
	 * output: Throws an IllegalArgumentException
	 */
	@Test
	void testInvalidLastName() {

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, "1",
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, " ",
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, "A",
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, "a",
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, "aa",
				validStreet, validCity, validAhvNumber, validImage));

		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, "aA",
				validStreet, validCity, validAhvNumber, validImage));
	}



/**
	 * description: test if no IllegalArgumentException has been thrown, when the street has at least two letters
	 * 				followed by a space and a number greater than 0.
	 * initial condition: nothing
	 * type: positive test
	 * input: valid first name, lastname, city, ahv number and image
	 * output: does not throw an IllegalArgumentException
	 */
	@Test
	void testValidStreet() {

		assertDoesNotThrow(() -> new Employee(validFirstName, validLastName, "Üaaüa 32",
				validCity, validAhvNumber, validImage));
		assertDoesNotThrow(() -> new Employee(validFirstName, validLastName, "Aa 1",
				validCity, validAhvNumber, validImage));
	}


/**
	 * description: test if an IllegalArgumentException has been thrown, when the street has not at least two letters
	 * 				followed by a space and a number greater than 0.
	 * initial condition: nothing
	 * type: negative test
	 * input: valid first name, lastname, city, ahv number and image
	 * output: Throws an IllegalArgumentException
	 */
	@Test
	void testInvalidStreet() {
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"Aa a", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"Aa 3 a", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"Aa3", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"A 3", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"a 3", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"aA 3", validCity, validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				"Aa 0", validCity, validAhvNumber, validImage));
	}


/**
	 * description: test if no IllegalArgumentException has been thrown, when the format: '1234 City' is used,
	 *				where 'City' contains at least two characters, starts with an uppercase letter, and only contains
	 *				letters.
	 * initial condition: nothing
	 * type: positive test
	 * input: valid first name, lastname, street, ahv number and image
	 * output: does not throw an IllegalArgumentException
	 */
	@Test
	void testValidCity() {
		assertDoesNotThrow(() -> new Employee(validFirstName, validLastName, validStreet,
				"1233 Winterthur", validAhvNumber, validImage));
		assertDoesNotThrow(() -> new Employee(validFirstName, validLastName, validStreet,
				"1233 Üa", validAhvNumber, validImage));
	}


/**
	 * description: test if an IllegalArgumentException has been thrown, when the format: '1234 City' is not used,
	 *				where 'City' contains at least two characters, starts with an uppercase letter, and only contains
	 *				letters.
	 * initial condition: nothing
	 * type: negative test
	 * input: valid first name, lastname, street, ahv number and image
	 * output: Throws an IllegalArgumentException
	 */
	@Test
	void testInvalidCity() {
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "123 City", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "12 City", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "1 City", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "City", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "City 12", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "1233 City c", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "1233 c", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "1233 cC", validAhvNumber, validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, "1233 C", validAhvNumber, validImage));
	}


/**
	 * description: test if no IllegalArgumentException has been thrown, when the Invalid ahv number format
	 * 				756.XXXX.XXXX.XX, where X represents digits, is used.
	 * initial condition: nothing
	 * type: positive test
	 * input: valid first name, lastname, street, city and image
	 * output: does not throw an IllegalArgumentException
	 */
	@Test
	void testValidAhvNumber() {
		assertDoesNotThrow(() -> new Employee(validFirstName, validLastName, validStreet,
				validCity, "756.1234.5678.90", validImage));
	}

/**
	 * description: test if an IllegalArgumentException has been thrown, when the Invalid ahv number format
	 * 				756.XXXX.XXXX.XX, where X represents digits, is not used.
	 * initial condition: nothing
	 * type: negative test
	 * input: valid first name, lastname, street, city and image
	 * output: Throws an IllegalArgumentException
	 */
	@Test
	void testInvalidAhvNumber() {
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "753.1234.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "726.1234.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "156.1234.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "7563.1234.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.12334.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.1234.56783.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.1234.5678.902", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.124.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.1234.567.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.1234.5678.0", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "7561234.5678.90", validImage));
		assertThrows(IllegalArgumentException.class, () -> new Employee(validFirstName, validLastName,
				validStreet, validCity, "756.1234.5678.90.2", validImage));
	}
}