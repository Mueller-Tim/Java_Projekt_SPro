package ch.zhaw.spro.dbservices;


import ch.zhaw.spro.dbrepos.QualificationDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.spro.models.Qualification;

import java.util.List;

/**

This service provides an API for managing qualifications. It uses Spring annotations to handle HTTP requests and interacts with the {@link QualificationDbRepository}.
<p>
The following operations are provided:
<ul>
    <li>{@link #getAllQualifications()}: Retrieve all qualifications from the database. Returns a list of qualifications.</li>
</ul>
*/
@Service
public class QualificationDbService {

    @Autowired
    private QualificationDbRepository qualificationDbRepository;
    
    /**
    * Retrieves all qualifications from the database.
    * 
    * @return a list of qualifications.
    */
    public List<Qualification> getAllQualifications() {
        return qualificationDbRepository.findAll();
    }
}


