package ch.zhaw.spro.dbrepos;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.spro.models.Shift;

/**
 * 
 * The ShiftDbRepository interface extends the MongoRepository interface, providing additional methods for interacting with the MongoDB database.
 * 
 * The MongoRepository<Shift, ObjectId> methods, such as save(Shift shift), deleteById(ObjectId id), etc.,
 * are also available through this interface.
 * 
 * The ShiftDbRepository interface is annotated with @Repository to indicate that it is a repository for the Shift entity.
 */
@Repository
public interface ShiftDbRepository extends MongoRepository<Shift, ObjectId>{
    
}