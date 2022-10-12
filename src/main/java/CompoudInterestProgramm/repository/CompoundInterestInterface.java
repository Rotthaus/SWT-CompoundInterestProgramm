package CompoudInterestProgramm.repository;

import CompoudInterestProgramm.model.CompoundInterestModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class CompoundInterestInterface provides methods for http request
 */
public interface CompoundInterestInterface extends JpaRepository<CompoundInterestModel, Double> {
}
