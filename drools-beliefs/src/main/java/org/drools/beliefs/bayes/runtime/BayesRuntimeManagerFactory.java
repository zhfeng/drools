package org.drools.beliefs.bayes.runtime;

import org.drools.core.common.InternalKnowledgeRuntime;
import org.drools.core.runtime.KieRuntimeManagerContext;
import org.drools.core.runtime.KieRuntimeManagerFactory;
import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;

public class BayesRuntimeManagerFactory  implements KieRuntimeManagerFactory<BayesRuntimeManager> {

    @Override
    public void preInit(KieRuntimeManagerContext ctx) {

    }

    @Override
    public void init(KieRuntimeManagerContext ctx) {

    }

    @Override
    public void postInit(KieRuntimeManagerContext ctx) {

    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.BAYES;
    }

    @Override
    public RequiredResourceType[] getRequiredResourceType() {
        return new RequiredResourceType[0];
    }

    @Override
    public BayesRuntimeManager newKieRuntime(InternalKnowledgeRuntime runtime) {
        return new BayesRuntimeManager( runtime );
    }
}
