package org.drools.beliefs.bayes;

import org.drools.beliefs.graph.impl.GraphImpl;
import org.drools.beliefs.graph.impl.ListGraphStore;

public class BayesNetwork extends GraphImpl<BayesVariable> {

    private String name;

    public BayesNetwork(String name) {
        super(new ListGraphStore<BayesVariable>());
        this.name = name;
    }

    public BayesNetwork() {
        super(new ListGraphStore<BayesVariable>());
    }

    public String getName() {
        return name;
    }

}
