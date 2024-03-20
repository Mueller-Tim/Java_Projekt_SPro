package ch.zhaw.spro.dbcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.spro.models.Qualification;
import ch.zhaw.spro.dbservices.QualificationDbService;

import java.util.List;

/**
 * This windowcontrollers provides an API for managing qualifications. It uses Spring annotations to handle HTTP requests and interacts with the {@link QualificationDbService}.
 * <p>
 * The following endpoints are provided:
 * <ul>
 *    <li>{@code GET /qualifications}: Retrieve all qualifications from the database. Returns a list of qualifications.</li>
 * </ul>
 */
@RestController
@RequestMapping("/qualifications")
public class QualificationDbController {

    @Autowired
    private QualificationDbService qualificationDbService;

    /**
     * Returns a list of all qualifications in the SPRO database.
     *
     * @return the list of qualifications
     */
    @GetMapping
    public ResponseEntity<List<Qualification>> getAllQualifications() {
        return new ResponseEntity<>(qualificationDbService.getAllQualifications(), HttpStatus.OK);
    }
}
