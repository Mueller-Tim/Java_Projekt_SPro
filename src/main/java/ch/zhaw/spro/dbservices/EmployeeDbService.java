package ch.zhaw.spro.dbservices;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.spro.dbrepos.EmployeeDbRepository;
import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.Qualification;

import java.util.List;
import java.util.Optional;

/**
 * This service provides an API for managing employees. It uses Spring annotations to handle HTTP requests and interacts with the {@link EmployeeDbRepository}.
 * <p>
 * The following operations are provided:
 * <ul>
 *     <li>{@link #getAllEmployees()}: Retrieve all employees from the database. Returns a list of employees.</li>
 *     <li>{@link #getEmployeeById(ObjectId)}: Retrieve an employee by its ID. Returns an optional employee.</li>
 *     <li>{@link #deleteEmployee(ObjectId)}: Delete an employee by its ID. Does not return a response.</li>
 *     <li>{@link #addEmployee(Employee)}: Add a new employee to the database. Expects an employee object in the request body and returns the created employee.</li>
 *      <li> and more... </li>
 * </ul>
 */
@Service
public class EmployeeDbService {

    @Autowired
    private EmployeeDbRepository employeeDbRepository;

    /**
     * Returns a list of all employees.
     *
     * @return a list of employees
     */
    public List<Employee> getAllEmployees(){
        return employeeDbRepository.findAll();
    }

    /**
     * Returns an employee with the given ID.
     *
     * @param id the ID of the employee to return
     * @return an optional employee
     */
    public Optional<Employee> getEmployeeById(ObjectId id) {
        return employeeDbRepository.findById(id);
    }

    /**
     * Deletes an employee with the given ID.
     *
     * @param id the ID of the employee to delete
     */
    public void deleteEmployee(ObjectId id){
        employeeDbRepository.deleteById(id);
    }

    /**
     * Adds an employee to the repository.
     *
     * @param employee the employee to add
     * @return the added employee
     */
    public Employee addEmployee(Employee employee){
        return employeeDbRepository.save(employee);
    }

    /**
     * Returns a list of qualifications for an employee with the given ID.
     *
     * @param id the ID of the employee to retrieve qualifications for
     * @return a list of qualifications
     */
    public List<Qualification> getQualificationsByEmployeeId(ObjectId id) {
        return employeeDbRepository.getQualificationsById(id);
    }

    /**
     * Updates the qualifications of an employee with the given ID.
     *
     * @param id the ID of the employee to update qualifications for
     * @param qualificationIds the IDs of the qualifications to update
     * @return the updated employee
     */
    public Employee updateQualificationsInEmployee(ObjectId id, List<String> qualificationIds) {
        Employee employee = employeeDbRepository.findById(id).orElse(null);
        if(employee != null){
            employee.clearQualifications();
            for (String qualificationId : qualificationIds) {
                employee.setQualifications(qualificationId);
            }
            return employeeDbRepository.save(employee);
        } else return null;
    }
}
