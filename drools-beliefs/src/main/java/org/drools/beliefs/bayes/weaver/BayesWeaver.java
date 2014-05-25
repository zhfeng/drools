package org.drools.beliefs.bayes.weaver;

import org.drools.beliefs.bayes.assembler.BayesPackage;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.impl.ResourceTypePackage;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.weaver.KieWeaver;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;

import java.util.Map;

public class BayesWeaver implements KieWeaver<BayesPackage> {
    private InternalKnowledgeBase kbase;

    public BayesWeaver(InternalKnowledgeBase kbase) {
        this.kbase = kbase;
    }

    @Override
    public void merge(KiePackage kiePkg, BayesPackage bayesPkg) {
        Map<ResourceType, ResourceTypePackage> map = ((InternalKnowledgePackage)kiePkg).getResourceTypePackages();
        BayesPackage existing  = (BayesPackage) map.get( ResourceType.BAYES );
        if ( existing == null ) {
            existing = new BayesPackage();
            map.put(ResourceType.BAYES, existing);
        }

        for ( String name : bayesPkg.listJunctionTrees() ) {
            existing.addJunctionTree( name, bayesPkg.getJunctionTree( name ) );
        }
    }

    @Override
    public void weave(KiePackage kiePkg, BayesPackage bayesPkg) {
        System.out.println( "Hello World " );
    }
}
