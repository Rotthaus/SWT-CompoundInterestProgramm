package com.swt.project.compoundService.services;

import com.swt.project.authService.repository.UserRepo;
import com.swt.project.compoundService.entity.CompoundComponent;
import com.swt.project.compoundService.entity.CompoundMethod;
import com.swt.project.compoundService.entity.CompoundModel;
import com.swt.project.compoundService.repository.CompoundRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Configuration
public class CompoundService {


    //Returns the Username/E-Mail out of the access token
    public String returnUserFromAccessToken(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return username;
    }

    //validates the user
    public boolean checkUser(long id, CompoundRepo compoundInterestRepository, UserRepo userRepo){
        try {
            CompoundModel compoundInterest = compoundInterestRepository.findById(id);
            if (compoundInterest.getIdUser() == userRepo.findByEmail(returnUserFromAccessToken()).get().getId()) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    //validates the data for the calculation
    public boolean validateDataForCalc(CompoundModel compoundInterest){
        if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT) || compoundInterest.getMethod().equals(CompoundMethod.ACCUMULATION)) {
            int marker = 0;
            if (compoundInterest.getInitialCapital() == 0) {
                marker++;
            }
            if (compoundInterest.getPeriod() == 0) {
                marker++;
            }
            if (compoundInterest.getInterestRate() == 0) {
                marker++;
            }
            if (compoundInterest.getFinalCapital() == 0) {
                marker++;
            }

            if (marker == 1) {
                return true;
            }
        }
        return false;
    }

    //validates the data to save in database
    public boolean validateDataForSave(CompoundModel compoundInterest){
        for (CompoundComponent component : CompoundComponent.values()) {
            if(component.equals(compoundInterest.getCalculatedComponent())){
                if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT) || compoundInterest.getMethod().equals(CompoundMethod.ACCUMULATION)) {
                    int marker = 0;
                    if (compoundInterest.getInitialCapital() != 0) {
                        marker++;
                    }
                    if (compoundInterest.getPeriod() != 0) {
                        marker++;
                    }
                    if (compoundInterest.getInterestRate() != 0) {
                        marker++;
                    }
                    if (compoundInterest.getFinalCapital() != 0) {
                        marker++;
                    }

                    if (marker == 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //creates a json object
    public String compoundModelToJson(double initialCapital, double interestRate, double period, double finalCapital){
        String jsonObject = "{\"initialCapital\":\""+initialCapital+"\",";
        jsonObject += "\"interestRate\":\""+interestRate+"\",";
        jsonObject += "\"period\":\""+period+"\",";
        jsonObject += "\"finalCapital\":\""+finalCapital+"\"}";

        return jsonObject;
    }

    //creates a json object
    public String compoundModelToJsonWithCalcComponent(double initialCapital, double interestRate, double period, double finalCapital, CompoundMethod method, CompoundComponent calculateComponent){
        String jsonObject = "{\"initialCapital\":\""+initialCapital+"\",";
        jsonObject += "\"interestRate\":\""+interestRate+"\",";
        jsonObject += "\"period\":\""+period+"\",";
        jsonObject += "\"finalCapital\":\""+finalCapital+"\",";
        jsonObject += "\"calculatedComponent\":\""+calculateComponent+"\",";
        jsonObject += "\"method\":\""+method+"\"}";

        return jsonObject;
    }


    //Analyse the body the request and calculate the wanted component
    public CompoundModel calcMissingComponent(CompoundModel compoundInterest){
        if (compoundInterest.getInitialCapital() == 0) {
            compoundInterest.setInitialCapital(calcInitialCapital(compoundInterest));
            compoundInterest.setCalculatedComponent(CompoundComponent.INITIALCAPITAL);
        }
        //calculate the interestRate
        if (compoundInterest.getInterestRate() == 0) {
            compoundInterest.setInterestRate(calcInterestRate(compoundInterest));
            compoundInterest.setCalculatedComponent(CompoundComponent.INTERESTRATE);
        }
        //calculate the period
        if (compoundInterest.getPeriod() == 0) {
            compoundInterest.setPeriod(calcPeriod(compoundInterest));
            compoundInterest.setCalculatedComponent(CompoundComponent.PERIOD);
        }
        //calculate the finalCapital
        if (compoundInterest.getFinalCapital() == 0) {
            compoundInterest.setFinalCapital(calcFinalCapital(compoundInterest));
            compoundInterest.setCalculatedComponent(CompoundComponent.FINALCAPITAL);
        }

        return compoundInterest;
    }

    //Calculation methods

    //Calculate the initial capital
    public double calcInitialCapital(CompoundModel compoundInterest){
        if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT)) {
            //PAYOUT
            return compoundInterest.getFinalCapital() / (1 + compoundInterest.getInterestRate()/100 * compoundInterest.getPeriod());
        } else {
            //ACCUMULATION
            return compoundInterest.getFinalCapital() / Math.pow((1 + compoundInterest.getInterestRate()/100),compoundInterest.getPeriod());
        }
    }
    //Calculate the interest rate
    public double calcInterestRate(CompoundModel compoundInterest){
        if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT)) {
            //PAYOUT
            return (compoundInterest.getFinalCapital() / compoundInterest.getInitialCapital() - 1) / compoundInterest.getPeriod()/100;
        } else {
            //ACCUMULATION
            return (Math.pow(compoundInterest.getFinalCapital() / compoundInterest.getInitialCapital(), 1.0 / compoundInterest.getPeriod()) - 1)*100;
        }
    }
    //Calculate the period
    public double calcPeriod(CompoundModel compoundInterest){
        if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT)) {
            //PAYOUT
            return (compoundInterest.getFinalCapital()-compoundInterest.getInitialCapital())/((compoundInterest.getInitialCapital()*compoundInterest.getInterestRate())/100);
        } else {
            //ACCUMULATION
            return Math.log(compoundInterest.getFinalCapital() / compoundInterest.getInitialCapital()) / Math.log(1 + compoundInterest.getInterestRate()/100);
        }
    }
    //Calculate the final capital
    public double calcFinalCapital(CompoundModel compoundInterest){
        if(compoundInterest.getMethod().equals(CompoundMethod.PAYOUT)) {
            //PAYOUT
            return compoundInterest.getInitialCapital() * (1+((compoundInterest.getInterestRate() * compoundInterest.getPeriod())/100));
        } else {
            //ACCUMULATION
            return compoundInterest.getInitialCapital() * Math.pow(1 + compoundInterest.getInterestRate()/100, compoundInterest.getPeriod());
        }
    }
}
