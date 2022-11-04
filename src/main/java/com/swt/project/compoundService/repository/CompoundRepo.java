package com.swt.project.compoundService.repository;
import com.swt.project.authService.entity.Users;
import com.swt.project.compoundService.entity.CompoundModel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Class CompoundInterestInterface provides methods for http request
 */
public interface CompoundRepo extends JpaRepository<CompoundModel, Integer> {
    public List <CompoundModel> findAllByIdUser(long idUser);

    public CompoundModel findById(long id);
}
