package CompoudInterestProgramm.repository;

import CompoudInterestProgramm.model.CompoundInterestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Class CompoundInterestInterface provides methods for http request
 */

@Repository
public interface CompoundInterestInterface extends JpaRepository<CompoundInterestModel, Double> {
}
