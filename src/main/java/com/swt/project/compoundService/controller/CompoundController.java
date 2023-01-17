package com.swt.project.compoundService.controller;

import com.swt.project.compoundService.entity.CompoundComponent;
import com.swt.project.compoundService.repository.CompoundRepo;
import com.swt.project.compoundService.entity.CompoundModel;
import com.swt.project.authService.repository.UserRepo;
import com.swt.project.compoundService.services.CompoundService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;


/**
 * Class CompoundInterestController provides http request
 */

@CrossOrigin
@RestController
@RequestMapping("/api/compound")
public class CompoundController {
    /**
     * repository connects to Interface
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * repository connects to Interface
     */
    @Autowired
    private CompoundRepo compoundInterestRepository;

    /**
     * repository connects to Interface
     */
    @Autowired
    private CompoundService compoundService;

    /**
     * Get method to retrieve data from server for a specific user
     * @return ResponseEntity
     */
    @GetMapping("/getData")
    public ResponseEntity<List<CompoundModel>> getCompoundInterest() {
        try {
            //validate the user
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long idOfUser = userRepo.findByEmail(username).get().getId();

            //return no content if no data is saved for the user
            if (compoundInterestRepository.findAllByIdUser(idOfUser).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            //return response
            return new ResponseEntity<>(compoundInterestRepository.findAllByIdUser(idOfUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Request cannot be processed ", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Post method to calculate data
     *
     * @param compoundInterest - parameter from type CompoundModel
     * @return ResponseEntity
     */
    @PostMapping("/calcData")
    public ResponseEntity<CompoundModel> calc(@RequestBody CompoundModel compoundInterest) {
        //validate data for calculation
        if(compoundService.validateDataForCalc(compoundInterest)) {
            //calculate the missing component
            compoundInterest = compoundService.calcMissingComponent(compoundInterest);
            //returns the response
            try {
                return new ResponseEntity(compoundService.compoundModelToJsonWithCalcComponent(compoundInterest.getInitialCapital(), compoundInterest.getInterestRate(), compoundInterest.getPeriod(), compoundInterest.getFinalCapital(),compoundInterest.getMethod(), compoundInterest.getCalculatedComponent()), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity("cant calculate data", HttpStatus.CONFLICT);
            }
        }
        //data validate failed
        return new ResponseEntity("data invalid", HttpStatus.BAD_REQUEST);
    }


    /**
     * Post method to save data to the server
     * @param compoundInterest - parameter from type CompoundModel
     * @return ResponseEntity
     */
    @PostMapping("/saveData")
    public ResponseEntity<CompoundModel> saveDataRecord(@RequestBody CompoundModel compoundInterest) {
        if(compoundService.validateDataForSave(compoundInterest)) {
            try {
                //set Date
                compoundInterest.setDate(LocalDate.now().toString());

                //save record to database
                compoundInterestRepository.save(new CompoundModel(userRepo.findByEmail(compoundService.returnUserFromAccessToken()).get().getId(),
                                compoundInterest.getInitialCapital(),
                                compoundInterest.getPeriod(),
                                compoundInterest.getInterestRate(),
                                compoundInterest.getFinalCapital(),
                                compoundInterest.getCalculatedComponent(),
                                compoundInterest.getMethod(),
                                compoundInterest.getDate()));
                return new ResponseEntity(compoundService.compoundModelToJsonWithCalcComponent(compoundInterest.getInitialCapital(),compoundInterest.getInterestRate(),compoundInterest.getPeriod(),compoundInterest.getFinalCapital(),compoundInterest.getMethod(), compoundInterest.getCalculatedComponent()), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity("data cant saved", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity("data cant saved cause missing parameter", HttpStatus.BAD_REQUEST);
    }

    /**
     * delete a data record from the server
     * @param compoundInterest - parameter from type CompoundModel
     * @return ResponseEntity
     */
    @PostMapping(value = "/deleteData")
    public ResponseEntity<CompoundModel> deleteCompoundInterest(@RequestBody CompoundModel compoundInterest) {
            //validate the user
            if (compoundService.checkUser(compoundInterest.getId(), compoundInterestRepository, userRepo)) {
                try {
                    //delete the data record
                    CompoundModel _compoundInterest = compoundInterestRepository.findById(compoundInterest.getId());
                    compoundInterestRepository.delete(_compoundInterest);
                    return new ResponseEntity("data deleted", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity("data cant be deleted", HttpStatus.BAD_REQUEST);
                }
            }
        return new ResponseEntity("data cant be deleted", HttpStatus.BAD_REQUEST);
    }
}