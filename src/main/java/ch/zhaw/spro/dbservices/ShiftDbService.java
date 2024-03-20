package ch.zhaw.spro.dbservices;


import ch.zhaw.spro.dbrepos.ShiftDbRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.spro.models.Shift;

import java.util.List;

/**
This service provides an API for managing shifts. It uses Spring annotations to handle HTTP requests and interacts with the {@link ShiftDbRepository}.
<p>
The following operations are provided:
<ul>
    <li>{@link #getAllShifts()}: Retrieve all shifts from the database. Returns a list of shifts.</li>
    <li>{@link #updateEmployeeInShift(ObjectId, String)}: Update an employee in a shift by its ID. Expects the shift ID and the new employee ID. Returns the updated shift.</li>
    <li>{@link #deleteShift(ObjectId)}: Delete a shift by its ID. Does not return a response.</li>
    <li>{@link #addShift(Shift)}: Add a new shift to the database. Expects a shift object in the request body and returns the created shift.</li>
</ul>
*/
@Service
public class ShiftDbService {

    @Autowired
    private ShiftDbRepository shiftDbRepository;

    /**
     * Retrieves all shifts from the database.
     * @return a list of shifts
     */
    public List<Shift> getAllShifts() {
        return shiftDbRepository.findAll();
    }

    /**
     * Updates an employee in a shift by its ID.
     * @param id the shift ID
     * @param employeeId the new employee ID
     * @return the updated shift
     */
    public Shift updateEmployeeInShift(ObjectId id, String employeeId){
        Shift shift = shiftDbRepository.findById(id).orElse(null);
        if(shift != null){
            shift.setEmployeeId(employeeId);
            return shiftDbRepository.save(shift);
        } else return null;
    }

    /**
     * Deletes a shift by its ID.
     * @param id the shift ID
     */
    public void deleteShift(ObjectId id){
        shiftDbRepository.deleteById(id);
    }

    /**
     * Adds a new shift to the database.
     * @param shift the shift object to be added
     * @return the created shift
     */
    public Shift addShift(Shift shift){
        return shiftDbRepository.save(shift);
    }
}

