package compoundInterestProgramm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class CompoundInterestController provides http request
 */

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
@Api(value = "Customer Related APIs")
public class CompoundInterestController {

    /**
     * repository connects to Interface
     */
    @Autowired
    CompoundInterestRepository compoundInterestRepository;

    /**
     * Get method to retrieve data from server
     *
     * @return ResponseEntity
     */
    @GetMapping("/getCi")
    @ApiOperation(value = "Returns all Data from database")
    public ResponseEntity<List<CompoundInterestModel>> getCompoundInterest() {
        try {
            List<CompoundInterestModel> entry = new ArrayList<CompoundInterestModel>();

            compoundInterestRepository.findAll().forEach(entry::add);

            if (entry.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(entry, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get method to retrieve specific data from server
     *
     * @param id - parameter to get specific record
     * @return ResponseEntity
     */
    @GetMapping("/getCi/{id}")
    @ApiOperation(value = "Returns Data for a specific id from database")
    public ResponseEntity<CompoundInterestModel> setCompoundInterestById(@PathVariable("id") int id) {
        Optional<CompoundInterestModel> tutorialData = compoundInterestRepository.findById(id);

        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Post method to save data to server
     *
     * @param compoundInterest - parameter from type compoundInterest
     * @return ResponseEntity
     */
    @PostMapping("/setCi")
    @ApiOperation(value = "Add a data record to the database")
    public ResponseEntity<CompoundInterestModel> setCompoundInterest( CompoundInterestModel compoundInterest) {
        try {
            CompoundInterestModel _compoundInterest = compoundInterestRepository.save(
                    new CompoundInterestModel(
                            compoundInterest.getInitialCapital(),
                            compoundInterest.getPeriod(),
                            compoundInterest.getInterestRate()));
            return new ResponseEntity<>(_compoundInterest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
