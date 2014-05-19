package org.drools.beliefs.bayes.weaver;

import org.drools.beliefs.bayes.assembler.BayesPackage;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.weaver.KieWeaver;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;

public class BayesWeaver implements KieWeaver<BayesPackage> {
    private InternalKnowledgeBase kbase;

    public BayesWeaver(InternalKnowledgeBase kbase) {
        this.kbase = kbase;
    }

    public void weave(KiePackage kiePkg, BayesPackage bayesPkg) {
        System.out.println( "Hello World " );
    }
}
