package com.swt.project.compoundService.entity;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Hidden;

import javax.persistence.*;

/**
 * Class CompoundInterest_Model generates the properties and methods
 */

@Entity
@Table(name = "compoundInterest")
@Hidden
public class CompoundModel {

    //### Properties ###
    /**
     * id - key for the datatable
     */
    @ApiParam( hidden = true)
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
     * calculatedComponent - the calculated component
     */
    @ApiParam( hidden = true)
    @Column(name = "calculatedComponent")
    private String calculatedComponent;


    /**
     * idUser - the id of the user
     */
    @ApiParam( hidden = true)
    //@ManyToOne( fetch = FetchType.LAZY)
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
     * @param calculatedComponent - calculation component
     */
    public CompoundModel(long idUser, double initialCapital, double period,
                         double interestRate, double finalCapital, String calculatedComponent) {
        this.idUser = idUser;
        this.initialCapital = initialCapital;
        this.period = period;
        this.interestRate = interestRate;
        this.finalCapital = finalCapital;
        this.calculatedComponent = calculatedComponent;
    }


    //### Getter ###
    /**
     * getId
     * Getter for id
     *
     * @return the id for Database
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

    public String getCalculatedComponent() {
        return calculatedComponent;
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


    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setCalculatedComponent(String calculatedComponent) {
        this.calculatedComponent = calculatedComponent;
    }
}