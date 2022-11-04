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
}
