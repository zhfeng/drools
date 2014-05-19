package org.drools.beliefs.bayes.integration;

import org.drools.beliefs.bayes.BayesInstance;
import org.drools.beliefs.bayes.BayesVariableConstructor;

import javax.inject.Named;

public class Garden {
//    private BayesInstance bayesInstance;

//    @PropertyChangeMask
//    private long propertyChangedMask;
//
//    private Garden previous;

//    @BayesVariableMapping
//    private boolean wetGrass;
//
//    @BayesVariableMapping
//    private boolean cloudy;
//
//    @BayesVariableMapping
//    private boolean sprinkler;
//
//    @BayesVariableMapping
//    private boolean rain;

    private boolean wetGrass;
    private boolean cloudy;
    private boolean sprinkler;
    private boolean rain;

    public Garden(BayesInstance bayesInstance, boolean wetGrass, boolean cloudy, boolean sprinkler, boolean rain) {
        this.wetGrass = wetGrass;
        this.cloudy = cloudy;
        this.sprinkler = sprinkler;
        this.rain = rain;
    }


}
