package ch.zhaw.spro.dbrepos;


import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.Qualification;

/**
 * 
 * The EmployeeDbRepository interface extends the MongoRepository interface, providing additional methods for interacting with the MongoDB database.
 * 
 * The repository includes the following methods:
 * 
 * 1. List<Qualification> getQualificationsById(ObjectId id):
 *    This method retrieves a list of qualifications for the employee with the given ID.
 * 
 * 2. The MongoRepository<Employee, ObjectId> methods, such as save(Employee employee), deleteById(ObjectId id), etc.,
 *    are also available through this interface.
 * 
 * The EmployeeDbRepository interface is annotated with @Repository to indicate that it is a repository for the Employee entity.
 */
@Repository
public interface EmployeeDbRepository extends MongoRepository<Employee, ObjectId>{
    /**
     * Retrieves a list of qualifications for the employee with the given ID.
     * 
     * @param id The ID of the employee to retrieve qualifications for.
     * @return A list of qualifications for the employee with the given ID.
     */
    List<Qualification> getQualificationsById(ObjectId id);
}
