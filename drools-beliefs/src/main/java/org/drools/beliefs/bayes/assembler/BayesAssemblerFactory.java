package org.drools.beliefs.bayes.assembler;

import org.drools.compiler.assembler.KieAssembler;
import org.drools.compiler.assembler.KieAssemblerContext;
import org.drools.compiler.assembler.KieAssemblerFactory;
import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;

public class BayesAssemblerFactory implements KieAssemblerFactory<BayesAssembler> {

    public BayesAssemblerFactory() {
    }

    @Override
    public void preInit(KieAssemblerContext ctx) {

    }

    @Override
    public void init(KieAssemblerContext ctx) {

    }

    @Override
    public void postInit(KieAssemblerContext ctx) {

    }

    @Override
    public RequiredResourceType[] getRequiredResourceType() {
        return new RequiredResourceType[0];
    }

    public ResourceType getResourceType() {
        return ResourceType.BAYES;
    }

    @Override
    public BayesAssembler newKieAssembler(KnowledgeBuilder kbuilder) {
        return new BayesAssembler(kbuilder);
    }
}
