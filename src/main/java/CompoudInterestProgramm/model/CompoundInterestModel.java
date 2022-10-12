package CompoudInterestProgramm.model;

import javax.persistence.*;

/**
 * Class CompoundInterest_Model generates the properties and methods
 */
@Entity
@Table(name = "compoundInterest")
public class CompoundInterestModel {

    //### Properties ###
    /**
     * id - key for the datatable
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private double id;

    /**
     * initialCapital - the initial capital
     */
    @Column(name = "initialCapital")
    private double initialCapital;

    /**
     * period - the duration
     */
    @Column(name = "period")
    private double period;

    /**
     * interestRate - the rate of the interest
     */
    @Column(name = "interestRate")
    private double interestRate;

    /**
     * finalCapital - the final value after specific duration
     */
    @Column(name = "finalCapital")
    private double finalCapital;

    /**
     * Constructor without parameters
     */
    //### Constructor ###
    public CompoundInterestModel() {

    }

    /**
     * Constructor wit parameters
     * @param initialCapital - initial capital
     * @param period - period
     * @param interestRate - interest rate
     * @param finalCapital - final capital
     */
    public CompoundInterestModel(double initialCapital, double period,
                                  double interestRate, double finalCapital) {
        this.initialCapital = initialCapital;
        this.period = period;
        this.interestRate = interestRate;
        this.finalCapital = finalCapital;

    }

    //### Getter ###
    /**
     * getId
     * Getter for id
     *
     * @return the id for Database
     */
    public double getId() {
        return id;
    }

    /**
     * getInitialCapital
     * Getter for initialCapital
     *
     * @return the initial Capital
     */
    public double getInitialCapital() {
        return initialCapital;
    }

    /**
     * getPeriod
     * Getter for period
     *
     * @return the period
     */
    public double getPeriod() {
        return period;
    }

    /**
     * getInterestRate
     * Getter for interest Rate
     *
     * @return the interest Rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * getFinalCapital
     * Getter for final Capital
     *
     * @return the final Capital
     */
    public double getFinalCapital() {
        return finalCapital;
    }

    //### Setter ###
    /**
     * setInitialCapital
     * Setter for initial Capital
     *
     * @param initialCapital - the interest Capital
     */
    public void setInitialCapital(double initialCapital) {
        this.initialCapital = initialCapital;
    }

    /**
     * setPeriod
     * Setter for period
     *
     * @param period - the duration
     */
    public void setPeriod(double period) {
        this.period = period;
    }

    /**
     * setInterestRate
     * Setter for interest Rate
     *
     * @param interestRate - the interest rate
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * setFinalCapital
     * Setter for final Capital
     *
     * @param finalCapital - the final capital
     */
    public void setFinalCapital(double finalCapital) {
        this.finalCapital = finalCapital;
    }

}