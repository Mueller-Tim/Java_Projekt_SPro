package ch.zhaw.spro.dbcontrollers;

import ch.zhaw.spro.models.ShiftType;
import ch.zhaw.spro.dbservices.ShiftTypeDbService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

/**

This controller provides an API for managing shift types. It uses Spring annotations to handle HTTP requests and interacts with the {@link ShiftTypeDbService}.
<p>
The following endpoints are provided:
<ul>
<li>{@code GET /shiftTypes}: Retrieve all shift types from the database. Returns a list of shift types.</li>
<li>{@code GET /shiftTypes/{id}}: Retrieve a specific shift type by its ID. Returns the shift type.</li>
</ul>
*/
@RestController
@RequestMapping("/shiftTypes")
public class ShiftTypeDbController {

    @Autowired
    private ShiftTypeDbService shiftTypeDbService;

    @GetMapping
    public ResponseEntity<List<ShiftType>> getAllShiftTypes() {
        return new ResponseEntity<>(shiftTypeDbService.getAllShiftTypes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ShiftType>> getShiftTypeById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(shiftTypeDbService.getShiftTypeById(id), HttpStatus.OK);
    }
}
