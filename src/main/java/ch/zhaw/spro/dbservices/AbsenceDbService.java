package ch.zhaw.spro.dbservices;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.spro.models.Absence;
import ch.zhaw.spro.dbrepos.AbsenceDbRepository;

import java.util.List;

/**
 * This service provides an API for managing absences. It uses Spring annotations to handle HTTP requests and interacts with the {@link AbsenceDbRepository}.
 * <p>
 * The following operations are provided:
 * <ul>
 *     <li>{@link #getAllAbsences()}: Retrieve all absences from the database. Returns a list of absences.</li>
 *     <li>{@link #deleteAbsence(ObjectId)}: Delete an absence by its ID. Does not return a response.</li>
 *     <li>{@link #addAbsence(Absence)}: Add a new absence to the database. Expects an absence object in the request body and returns the created absence.</li>
 *     <li>{@link #getAllAbsencesByEmployeeId(String employeeId)}: Retrieves all absences associated with the specified employee ID. Returns a list of absences.</li>
 * </ul>
 */
@Service
public class AbsenceDbService {

    @Autowired
    private AbsenceDbRepository absenceDbRepository;
    
    /**
     * Retrieves all absences in the system.
     * 
     * @return a list of all absences
     */
    public List<Absence> getAllAbsences() {
        return absenceDbRepository.findAll();
    }

    /**
     * Deletes an absence with the specified ID.
     * 
     * @param id the ID of the absence to delete
     */
    public void deleteAbsence(ObjectId id){
        absenceDbRepository.deleteById(id);
    }

    /**
     * Adds a new absence to the system.
     * 
     * @param absence the absence to add
     * @return the added absence, including its newly generated ID
     */
    public Absence addAbsence(Absence absence){
        return absenceDbRepository.save(absence);
    }

    /**
     * Retrieves all absences associated with the specified employee ID.
     * 
     * @param employeeId the employee ID to filter by
     * @return a list of absences associated with the specified employee ID
     */
    public List<Absence> getAllAbsencesByEmployeeId(String employeeId) {
        return absenceDbRepository.findByEmployeeId(employeeId);
    }

}

