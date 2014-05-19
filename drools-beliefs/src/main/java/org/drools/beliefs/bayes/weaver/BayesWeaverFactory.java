package org.drools.beliefs.bayes.weaver;

import org.drools.core.weaver.KieWeaver;
import org.drools.core.weaver.KieWeaverContext;
import org.drools.core.weaver.KieWeaverFactory;
import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;

public class BayesWeaverFactory implements KieWeaverFactory<BayesWeaver> {
    @Override
    public void preInit(KieWeaverContext ctx) {

    }

    @Override
    public void init(KieWeaverContext ctx) {

    }

    @Override
    public void postInit(KieWeaverContext ctx) {

    }

    @Override
    public RequiredResourceType[] getRequiredResourceType() {
        return new RequiredResourceType[0];
    }

    @Override
    public ResourceType getResourceType() {
        return null;
    }

    @Override
    public BayesWeaver newKieWeaver(KieBase kieBase) {
        return null;
    }
}
