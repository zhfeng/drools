package org.drools.beliefs.bayes;

public class BayesVariableState {
    private BayesVariable variable;
    private double[]      distribution;

    public BayesVariableState(BayesVariable variable, double[] distribution) {
        this.variable = variable;
        this.distribution = distribution;
    }

    public BayesVariable getVariable() {
        return variable;
    }

    public double[] getDistribution() {
        return distribution;
    }
}
