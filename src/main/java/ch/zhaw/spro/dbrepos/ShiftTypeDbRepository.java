package ch.zhaw.spro.dbrepos;


import ch.zhaw.spro.models.ShiftType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * The ShiftTypeDbRepository interface extends the MongoRepository interface, providing additional methods for interacting with the MongoDB database.
 * 
 * The MongoRepository<ShiftType, ObjectId> methods, such as save(ShiftType shiftType), deleteById(ObjectId id), etc.,
 * are also available through this interface.
 * 
 * The ShiftTypeDbRepository interface is annotated with @Repository to indicate that it is a repository for the ShiftType entity.
 */
@Repository
public interface ShiftTypeDbRepository extends MongoRepository<ShiftType, ObjectId>{
}