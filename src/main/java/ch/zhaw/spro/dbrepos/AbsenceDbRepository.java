package ch.zhaw.spro.dbrepos;


import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.spro.models.Absence;

/**
 * The AbsenceDbRepository interface provides a MongoDB repository for storing and retrieving Absence data.
 * This interface extends the MongoRepository interface, providing additional methods for interacting with the MongoDB database.
 * 
 * The repository includes the following methods:
 * 
 * 1. List<Absence> findByEmployeeId(String employeeId):
 *    This method finds all Absence instances associated with the specified employee ID.
 * 
 * 2. The MongoRepository<Absence, ObjectId> methods, such as save(Absence absence), deleteById(ObjectId id), etc.,
 *    are also available through this interface.
 * 
 * The AbsenceDbRepository interface is annotated with @Repository to indicate that it is a repository for the Absence entity.
 */
@Repository
public interface AbsenceDbRepository extends MongoRepository<Absence, ObjectId>{
    /**
    * Finds all absences associated with the specified employee ID.
    *
    * @param employeeId the ID of the employee for which to retrieve absence records
    * @return a List of absences representing the employee's absences
    */
    List<Absence> findByEmployeeId(String employeeId);
}

