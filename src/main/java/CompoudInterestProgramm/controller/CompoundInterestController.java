package CompoudInterestProgramm.controller;

import CompoudInterestProgramm.repository.CompoundInterestInterface;
import CompoudInterestProgramm.model.CompoundInterestModel;
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
public class CompoundInterestController {

    /**
     * repository connects to Interface
     */
    @Autowired
    CompoundInterestInterface repository;

    /**
     * Get method to retrieve data from server
     *
     * @return ResponseEntity
     */
    @GetMapping("/getCi")
    public ResponseEntity<List<CompoundInterestModel>> getCompoundInterest() {
        try {
            List<CompoundInterestModel> entry = new ArrayList<CompoundInterestModel>();

            repository.findAll().forEach(entry::add);

            if (entry.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(entry, HttpStatus.OK);
        } catch (Exception e) {
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
    public ResponseEntity<CompoundInterestModel> setCompoundInterestById(@PathVariable("id") double id) {
        Optional<CompoundInterestModel> tutorialData = repository.findById(id);

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
    public ResponseEntity<CompoundInterestModel> setCompoundInterest(@RequestBody CompoundInterestModel compoundInterest) {
        try {
            CompoundInterestModel _compoundInterest = repository.save(
                    new CompoundInterestModel(
                            compoundInterest.getInitialCapital(),
                            compoundInterest.getPeriod(),
                            compoundInterest.getInterestRate(),
                            compoundInterest.getFinalCapital()));
            return new ResponseEntity<>(_compoundInterest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
