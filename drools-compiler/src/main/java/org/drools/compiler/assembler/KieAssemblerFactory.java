package org.drools.compiler.assembler;

import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;

public interface KieAssemblerFactory<T extends KieAssembler> {
    void preInit(KieAssemblerContext ctx);
    void init(KieAssemblerContext ctx);
    void postInit(KieAssemblerContext ctx);

    RequiredResourceType[] getRequiredResourceType();

    ResourceType getResourceType();

    T newKieAssembler(KnowledgeBuilder kbuilder);
}
