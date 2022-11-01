package CompoudInterestProgramm;

import CompoudInterestProgramm.CompoundInterestModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class CompoundInterestInterface provides methods for http request
 */
public interface CompoundInterestRepository extends JpaRepository<CompoundInterestModel, Integer> {
}
