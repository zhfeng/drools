package org.drools.core.weaver;

import org.drools.core.KieFactory;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;

public interface KieWeaverFactory<T extends KieWeaver> extends KieFactory<KieWeaverContext> {
        //    void preInit(KieWeaverContext ctx);
//    void init(KieWeaverContext ctx);
//    void postInit(KieWeaverContext ctx);
//
//    RequiredResourceType[] getRequiredResourceType();
//
//    ResourceType getResourceType();

    T newKieWeaver(KieBase kieBase);
}
