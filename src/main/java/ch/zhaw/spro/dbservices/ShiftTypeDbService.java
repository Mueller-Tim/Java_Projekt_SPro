package ch.zhaw.spro.dbservices;


import ch.zhaw.spro.dbrepos.ShiftTypeDbRepository;
import ch.zhaw.spro.models.ShiftType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This service provides an API for managing shift types. It uses Spring annotations to handle HTTP requests and interacts with the {@link ShiftTypeDbRepository}.
 * <p>
 * The following operations are provided:
 * <ul>
 *     <li>{@link #getAllShiftTypes()}: Retrieve all shift types from the database. Returns a list of shift types.</li>
 *     <li>{@link #getShiftTypeById(ObjectId)}: Retrieve a shift type by its ID. Returns an optional shift type.</li>
 * </ul>
 */
@Service
public class ShiftTypeDbService {

    @Autowired
    private ShiftTypeDbRepository shiftTypeDbRepository;
    
    /**
     * Retrieve all shift types from the database.
     * 
     * @return a list of shift types
     */
    public List<ShiftType> getAllShiftTypes() {
        return shiftTypeDbRepository.findAll();
    }

    /**
     * Retrieve a shift type by its ID.
     * 
     * @param id the ID of the shift type to retrieve
     * @return an optional shift type, which will be empty if no shift type with the given ID exists
     */
    public Optional<ShiftType> getShiftTypeById(ObjectId id) {
        return shiftTypeDbRepository.findById(id);
    }

}


