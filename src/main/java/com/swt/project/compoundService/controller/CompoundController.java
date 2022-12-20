package com.swt.project.compoundService.controller;

import com.swt.project.compoundService.repository.CompoundRepo;
import com.swt.project.compoundService.entity.CompoundModel;
import com.swt.project.authService.repository.UserRepo;
import com.swt.project.compoundService.services.CompoundService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class CompoundInterestController provides http request
 */

@CrossOrigin
@RestController
@RequestMapping("/api/compound")
public class CompoundController {

    @Autowired
    private UserRepo userRepo;
    /**
     * repository connects to Interface
     */
    @Autowired
    private CompoundRepo compoundInterestRepository;

    @Autowired
    private CompoundService compoundService;

    /**
     * Get method to retrieve data from server
     *
     * @return ResponseEntity
     */
    @GetMapping("/getData")
    public ResponseEntity<List<CompoundModel>> getCompoundInterest() {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long idOfUser = userRepo.findByEmail(username).get().getId();

            if (compoundInterestRepository.findAllByIdUser(idOfUser).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(compoundInterestRepository.findAllByIdUser(idOfUser), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Post method to calculate data
     *
     * @param compoundInterest - parameter from type compoundInterest
     * @return ResponseEntity
     */
    @PostMapping("/calcData")
    public ResponseEntity<CompoundModel> calc(@RequestBody CompoundModel compoundInterest) {

        if(compoundService.validateDataForCalc(compoundInterest)) {
            if (compoundInterest.getInitialCapital() == 0) {
                compoundInterest.setInitialCapital(calcInitialCapital(compoundInterest));
                //compoundInterest.setCalculatedComponent("initial capital");
            }

            if (compoundInterest.getInterestRate() == 0) {
                compoundInterest.setInterestRate(calcInterestRate(compoundInterest));
                //compoundInterest.setCalculatedComponent("interest rate");
            }

            if (compoundInterest.getPeriod() == 0) {
                compoundInterest.setPeriod(calcPeriod(compoundInterest));
                //compoundInterest.setCalculatedComponent("period");
            }

            if (compoundInterest.getFinalCapital() == 0) {
                compoundInterest.setFinalCapital(calcFinalCapital(compoundInterest));
                //compoundInterest.setCalculatedComponent("final capital");
            }

            try {
                return new ResponseEntity<>(compoundInterest, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity("cant calculate data", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity("data invalid", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Post method to save data to the server
     *
     * @param compoundInterest - parameter from type compoundInterest
     * @return ResponseEntity
     */
    @PostMapping("/saveData")
    public ResponseEntity<CompoundModel> saveDataRecord(@RequestBody CompoundModel compoundInterest) {
        if(compoundService.validateDataForSave(compoundInterest)) {
            try {
                compoundInterest.setDate(LocalDate.now().toString());
                CompoundModel _compoundInterest =
                        compoundInterestRepository.save(new CompoundModel(
                                userRepo.findByEmail(compoundService.returnUserFromAccessToken()).get().getId(),
                                compoundInterest.getInitialCapital(),
                                compoundInterest.getPeriod(),
                                compoundInterest.getInterestRate(),
                                compoundInterest.getFinalCapital(),
                                compoundInterest.getDate()));
                return new ResponseEntity<>(_compoundInterest, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity("data cant saved", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity("data cant saved cause missing parameter", HttpStatus.BAD_REQUEST);
    }

    /**
     * delete a data record from the server
     *
     * @return ResponseEntity
     */
    @PostMapping(value = "/deleteData")
    public ResponseEntity<List<CompoundModel>> deleteCompoundInterest(long id) {
            if (compoundService.checkUser(id, compoundInterestRepository, userRepo)) {
                try {
                    CompoundModel compoundInterest = compoundInterestRepository.findById(id);
                    compoundInterestRepository.delete(compoundInterest);
                    return new ResponseEntity("data deleted", HttpStatus.OK);
                } catch (Exception e) {
                    System.out.println(e);
                    return new ResponseEntity("data cant be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        return new ResponseEntity("no permission", HttpStatus.FORBIDDEN);
    }

    //Calculation methods

    //Calculate the initial capital
    public double calcInitialCapital(CompoundModel compoundInterest){
        double calc;
        calc = compoundInterest.getFinalCapital() / Math.pow(1 + (compoundInterest.getInterestRate()/100),compoundInterest.getPeriod());

        return calc;
    }
    //Calculate the period
    public double calcPeriod(CompoundModel compoundInterest){
        double calc;
        calc = Math.log(compoundInterest.getFinalCapital()/compoundInterest.getInitialCapital())/Math.log(1+compoundInterest.getInterestRate()/100);

        return calc;
    }
    //Calculate the interest rate
    public double calcInterestRate(CompoundModel compoundInterest){
        double calc;
        calc =(Math.pow((Math.sqrt(compoundInterest.getFinalCapital()/compoundInterest.getInitialCapital())),compoundInterest.getPeriod())-1)*100;
        return calc;
    }
    //Calculate the final capital
    public double calcFinalCapital(CompoundModel compoundInterest){
        double calc;
        calc = compoundInterest.getInitialCapital() + (compoundInterest.getInitialCapital() * (compoundInterest.getInterestRate()/100) * compoundInterest.getPeriod());

        return calc;
    }
}