package ch.zhaw.spro.dbcontrollers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.spro.models.Absence;
import ch.zhaw.spro.dbservices.AbsenceDbService;

import java.util.List;

/**
 * This windowcontrollers provides an API for managing absences. It uses Spring annotations to handle HTTP requests and interacts with the {@link AbsenceDbService}.
 * <p>
 * The following endpoints are provided:
 * <ul>
 *     <li>{@code GET /absences}: Retrieve all absences from the database. Returns a list of absences.</li>
 *     <li>{@code GET /absences/{id}/employee}: Retrieve all absences for a specific employee by their ID. Returns a list of absences.</li>
 *     <li>{@code POST /absences}: Add a new absence to the database. Expects an absence object in the request body and returns the created absence.</li>
 *     <li>{@code DELETE /absences/{id}}: Delete an absence by its ID. Does not return a response.</li>
 * </ul>
 */

@RestController
@RequestMapping("/absences")
public class AbsenceDbController {

    @Autowired
    private AbsenceDbService absenceDbService;

    /**
     * Retrieve all absences from the database.
     *
     * @return a list of absences
     */
    @GetMapping
    public ResponseEntity<List<Absence>> getAllAbsences() {
        return new ResponseEntity<>(absenceDbService.getAllAbsences(), HttpStatus.OK);
    }

    /**
     * Retrieve all absences for a specific employee by their ID.
     *
     * @param employeeId the ID of the employee to retrieve absences from
     * @return a list of absences for the specified employee
     */
    @GetMapping("/{id}/employee")
    public List<Absence> getAllAbsencesByEmployeeId(@PathVariable String employeeId) {
        return absenceDbService.getAllAbsencesByEmployeeId(employeeId);
    }

    /**
     * Add a new absence to the database.
     *
     * @param absence the absence object to add
     * @return the created absence
     */
    @PostMapping
    public Absence addAbsence(@RequestBody Absence absence){
        return absenceDbService.addAbsence(absence);
    }

    /**
     * Delete an absence by its ID.
     *
     * @param id the ID of the absence to delete
     */
    @DeleteMapping("/{id}")
    public void deleteAbsenceById(@PathVariable ObjectId id){
        absenceDbService.deleteAbsence(id);
    }
}