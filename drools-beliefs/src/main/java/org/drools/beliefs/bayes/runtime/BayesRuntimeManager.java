package org.drools.beliefs.bayes.runtime;

import org.drools.beliefs.bayes.BayesInstance;
import org.drools.beliefs.bayes.JunctionTree;
import org.drools.beliefs.bayes.assembler.BayesPackage;
import org.drools.core.common.InternalKnowledgeRuntime;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.impl.ResourceTypePackage;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.runtime.KieRuntimeManager;
import org.kie.api.io.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class BayesRuntimeManager implements KieRuntimeManager {
    private InternalKnowledgeRuntime runtime;

    private Map<String, BayesInstance> instances;

    public BayesRuntimeManager(InternalKnowledgeRuntime runtime) {
        this.runtime = runtime;
        this.instances = new HashMap<String, BayesInstance>();
    }

    public BayesInstance getInstance(Class cls) {
        // using the two-tone pattern, to ensure only one is created
        BayesInstance instance = instances.get( cls.getName() );
        if ( instance == null ) {
            instance = createInstance(cls);
        }

        return instance;
    }

    private synchronized  BayesInstance createInstance(Class cls) {
        // synchronised using the two-tone pattern, to ensure only one is created
        BayesInstance instance = instances.get( cls.getName() );
        if ( instance != null ) {
            return instance;
        }


        InternalKnowledgePackage kpkg = (InternalKnowledgePackage) runtime.getKieBase().getKiePackage( cls.getPackage().getName() );
        Map<ResourceType, ResourceTypePackage> map = kpkg.getResourceTypePackages();
        BayesPackage bayesPkg  = (BayesPackage) map.get( ResourceType.BAYES );
        JunctionTree jtree =  bayesPkg.getJunctionTree(cls.getSimpleName());

        instance = new BayesInstance( jtree, cls );
        instances.put( cls.getName() , instance );

        return instance;
    }
}
