package ch.zhaw.spro.dbcontrollers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.spro.models.Shift;
import ch.zhaw.spro.dbservices.ShiftDbService;

import java.util.List;

/**
 * This controller provides an API for managing shifts. It uses Spring annotations to handle HTTP requests and interacts with the {@link ShiftDbService}.
 * <p>
 * The following endpoints are provided:
 * <ul>
 *    <li>{@code GET /shifts}: Retrieve all shifts from the database. Returns a list of shifts.</li>
 *    <li>{@code POST /shifts}: Add a new shift to the database. The shift object must be provided in the request body. Returns the created shift.</li>
 *    <li>{@code POST /shifts/{id}/setEmployee}: Set the employee of a specific shift. The employeeId must be provided in the request body. Returns the updated shift.</li>
 *    <li>{@code DELETE /shifts/{id}}: Delete a specific shift from the database. Returns no content.</li>
 * </ul>
 */
@RestController
@RequestMapping("/shifts")
public class ShiftDbController {

    @Autowired
    private ShiftDbService shiftDbService;

    /**
     * Retrieves all shifts from the database.
     *
     * @return A list of shifts.
     */
    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        return new ResponseEntity<>(shiftDbService.getAllShifts(), HttpStatus.OK);
    }

    /**
     * Adds a new shift to the database.
     *
     * @param shift The shift object to be added.
     * @return The created shift.
     */
    @PostMapping
    public Shift addShift(@RequestBody Shift shift){
        return shiftDbService.addShift(shift);
    }

    /**
     * Sets the employee of a specific shift.
     *
     * @param id     The ID of the shift to be updated.
     * @param employeeId The ID of the employee to be set.
     * @return The updated shift.
     */
    @PostMapping("/{id}/setEmployee")
    public Shift setEmployee(@PathVariable ObjectId id, @PathVariable String employeeId){
        return shiftDbService.updateEmployeeInShift(id, employeeId);
    }

    /**
     * Deletes a specific shift from the database.
     *
     * @param id The ID of the shift to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteShiftById(@PathVariable ObjectId id){
        shiftDbService.deleteShift(id);
    }

}