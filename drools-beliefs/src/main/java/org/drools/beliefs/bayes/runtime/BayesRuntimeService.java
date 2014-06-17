package org.drools.beliefs.bayes.runtime;

import org.drools.core.common.InternalKnowledgeRuntime;
import org.kie.internal.runtime.KieRuntimeService;
import org.kie.internal.runtime.KnowledgeRuntime;

public class BayesRuntimeService implements KieRuntimeService<BayesRuntime> {

//    @Override
//    public void preInit(KieRuntimeManagerContext ctx) {
//
//    }
//
//    @Override
//    public void init(KieRuntimeManagerContext ctx) {
//
//    }
//
//    @Override
//    public void postInit(KieRuntimeManagerContext ctx) {
//
//    }
//
//    @Override
//    public ResourceType getResourceType() {
//        return ResourceType.BAYES;
//    }
//
//    @Override
//    public RequiredResourceType[] getRequiredResourceType() {
//        return new RequiredResourceType[0];
//    }
//
//    @Override
//    public BayesRuntimeManager newKieRuntime(InternalKnowledgeRuntime runtime) {
//        return new BayesRuntimeManager( runtime );
//    }


//    @Override
//    public BayesRuntime newKieRuntime(InternalKnowledgeRuntime session) {
//        return new BayesRuntimeImpl( session );
//    }
//
//    @Override
//    public Class getServiceInterfaceLookup() {
//        return ResourceType.BAYES.getClass();
//    }


    @Override
    public BayesRuntime newKieRuntime(KnowledgeRuntime session) {
        return new BayesRuntimeImpl( (InternalKnowledgeRuntime) session );
    }

    @Override
    public Class getServiceInterface() {
        return BayesRuntime.class;
    }
}
