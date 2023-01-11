package com.swt.project.compoundService.services;

import com.swt.project.authService.repository.UserRepo;
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

    //Returns the Username/E-Mail out of the access token
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

    public boolean validateDataForCalc(CompoundModel compoundInterest){
        int marker = 0;
        if(compoundInterest.getInitialCapital() == 0){
            marker++;
        }
        if(compoundInterest.getPeriod() == 0){
            marker++;
        }
        if(compoundInterest.getInterestRate() == 0){
            marker++;
        }
        if(compoundInterest.getFinalCapital() == 0){
            marker++;
        }

        if(marker == 1){
            return true;
        }

        return false;
    }

    public boolean validateDataForSave(CompoundModel compoundInterest){
        int marker = 0;
        if(compoundInterest.getInitialCapital() != 0){
            marker++;
        }
        if(compoundInterest.getPeriod() != 0){
            marker++;
        }
        if(compoundInterest.getInterestRate() != 0){
            marker++;
        }
        if(compoundInterest.getFinalCapital() != 0){
            marker++;
        }

        if(marker == 4){
            return true;
        }

        return false;
    }

    public String compoundModelToJson(double initialCapital, double interestRate, double period, double finalCapital){
        String jsonObject = "{\"initialCapital\":\""+initialCapital+"\",";
        jsonObject += "\"interestRate\":\""+interestRate+"\",";
        jsonObject += "\"period\":\""+period+"\",";
        jsonObject += "\"finalCapital\":\""+finalCapital+"\"}";

        return jsonObject;
    }

    public String compoundModelToJsonWithCalcComponent(double initialCapital, double interestRate, double period, double finalCapital, String calculateComponent){
        String jsonObject = "{\"initialCapital\":\""+initialCapital+"\",";
        jsonObject += "\"interestRate\":\""+interestRate+"\",";
        jsonObject += "\"period\":\""+period+"\",";
        jsonObject += "\"finalCapital\":\""+finalCapital+"\",";
        jsonObject += "\"calculatedComponent\":\""+calculateComponent+"\"}";

        return jsonObject;
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
