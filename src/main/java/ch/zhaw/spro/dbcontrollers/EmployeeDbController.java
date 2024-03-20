package ch.zhaw.spro.dbcontrollers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.spro.dbservices.EmployeeDbService;
import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.Qualification;

import java.util.List;
import java.util.Optional;


/**

* This windowcontrollers provides an API for managing employees. It uses Spring annotations to handle HTTP requests and interacts with the {@link EmployeeDbService}.
* <p>
* The following endpoints are provided:
* <ul>
*    <li>{@code GET /employees}: Retrieve all employees from the database. Returns a list of employees.</li>
*    <li>{@code GET /employees/{id}}: Retrieve an employee by their ID. Returns an Optional of the Employee, which is empty if no employee was found.</li>
*    <li>{@code GET /employees/{id}/qualifications}: Retrieve all qualifications for an employee by their ID. Returns a list of qualifications.</li>
*    <li>{@code POST /employees}: Create a new employee. Returns the created employee.</li>
*    <li>{@code POST /employees/{id}/qualifications}: Add a qualification to an employee. Returns the updated employee.</li>
*    <li>{@code DELETE /employees/{id}}: Delete an employee by their ID. </li>
* </ul>
*/
@RestController
@RequestMapping("/employees")
public class EmployeeDbController {

    @Autowired
    private EmployeeDbService employeeDbService;

    /**
     * Get all employees in the system.
     * @return a list of all employees.
     */
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeDbService.getAllEmployees();
    }

    /**
     * Get an employee by id.
     * @param id the ObjectId of the employee.
     * @return an Optional of the Employee, which is empty if no employee was found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(employeeDbService.getEmployeeById(id), HttpStatus.OK);
    }

    /**
     * Get all qualifications for an employee by their id.
     * @param id the ObjectId of the employee.
     * @return a list of qualifications.
     */
    @GetMapping("/{id}/qualifications")
    public List<Qualification> getQualificationsByEmployeeId(@PathVariable ObjectId id){
        return employeeDbService.getQualificationsByEmployeeId(id);
    }

    /**
     * Add a new employee to the system.
     * @param employee the Employee to add.
     * @return the added Employee with the updated id.
     */
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee){
        return employeeDbService.addEmployee(employee);
    }

    /**
     * Delete an employee by id.
     * @param id the ObjectId of the employee.
     */
    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable ObjectId id){
        employeeDbService.deleteEmployee(id);
    }

    /**
     * Update qualifications for an employee.
     * @param id the ObjectId of the employee.
     * @param qualificationIds a list of ObjectIds for the qualifications.
     * @return the updated Employee.
     */
    @PostMapping("/{id}/setEmployee")
    public Employee setQualificationsForEmployee(@PathVariable ObjectId id, @PathVariable List<String> qualificationIds){
        return employeeDbService.updateQualificationsInEmployee(id, qualificationIds);
    }
}