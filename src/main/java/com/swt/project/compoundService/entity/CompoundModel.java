package com.swt.project.compoundService.entity;

import io.swagger.annotations.ApiParam;

import javax.persistence.*;

/**
 * Class CompoundInterest_Model generates the properties and methods
 */

@Entity
@Table(name = "compoundInterest")
public class CompoundModel {

    //### Properties ###
    /**
     * id - key for the data record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
     * finalCapital - the final Capital
     */
    @Column(name = "finalCapital")
    private double finalCapital;


    /**
     * setCalculatedComponent - the calculated Component
     */
    @Column(name = "CalculatedComponent")
    private CompoundComponent calculatedComponent;

    /**
     * method - the method of calculation
     */
    @Column(name = "method")
    private CompoundMethod method;

    /**
     * date - the creation date
     */
    @Column(name = "date")
    private String date;

    /**
     * idUser - the id of the user
     */
    @JoinColumn (name = "idUser")
    private long idUser;

    /**
     * Constructor without parameters
     */
    //### Constructor ###
    public CompoundModel() {

    }

    /**
     * Constructor wit parameters
     * @param initialCapital - initial capital
     * @param period - period
     * @param interestRate - interest rate
     * @param finalCapital - final capital
     * @param calculatedComponent - calculated component
     * @param method - method
     * @param date - date
     */
    public CompoundModel(long idUser, double initialCapital, double period,
                         double interestRate, double finalCapital, CompoundComponent calculatedComponent, CompoundMethod method, String date) {
        this.idUser = idUser;
        this.initialCapital = initialCapital;
        this.period = period;
        this.interestRate = interestRate;
        this.finalCapital = finalCapital;
        this.calculatedComponent = calculatedComponent;
        this.method = method;
        this.date = date;
    }


    //### Getter ###
    /**
     * getId
     * Getter for id of the data record
     *
     * @return the id for data record
     */
    public long getId() {
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
     * getIdUser
     * Getter for Id of the User
     *
     * @return the Id of ther User
     */
    public long getIdUser() {
        return idUser;
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

    /**
     * getMethod
     * Getter for the method
     *
     * @return the method
     */
    public CompoundMethod getMethod() {
        return method;
    }

    /**
     * getCalculatedComponent
     * Getter for calculated Component
     *
     * @return the calculated component
     */
    public CompoundComponent getCalculatedComponent() {
        return calculatedComponent;
    }

    /**
     * getDate
     * Getter for date
     *
     * @return the date
     */
    public String getDate() {
        return date;
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
     * Setter for finalCapital
     *
     * @param finalCapital - the final capital
     */
    public void setFinalCapital(double finalCapital) {
        this.finalCapital = finalCapital;
    }

    /**
     * setIdUser
     * Setter for user id
     *
     * @param idUser - the id of the user
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /**
     * setDate
     * Setter for date
     *
     * @param date - the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * setCalculatedComponent
     * Setter for calculated Component
     *
     * @param calculatedComponent - the calculated component
     */
    public void setCalculatedComponent(CompoundComponent calculatedComponent) {
        this.calculatedComponent = calculatedComponent;
    }

    /**
     * setMethod
     * Setter for method
     *
     * @param method - the method
     */
    public void setMethod(CompoundMethod method) {
        this.method = method;
    }
}