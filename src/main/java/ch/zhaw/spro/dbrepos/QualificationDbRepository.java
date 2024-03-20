package ch.zhaw.spro.dbrepos;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.spro.models.Qualification;

/**
 * 
 * The QualificationDbRepository interface extends the MongoRepository interface, providing additional methods for interacting with the MongoDB database.
 * 
 * The MongoRepository<Qualification, ObjectId> methods, such as save(Qualification qualification), deleteById(ObjectId id), etc.,
 * are also available through this interface.
 * 
 * The QualificationDbRepository interface is annotated with @Repository to indicate that it is a repository for the Qualification entity.
 */
@Repository
public interface QualificationDbRepository extends MongoRepository<Qualification, ObjectId>{
}


