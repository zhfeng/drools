package org.drools.beliefs.bayes.weaver;

//import org.drools.core.weaver.KieWeaver;
//import org.drools.core.weaver.KieWeaverContext;
//import org.drools.core.weaver.KieWeaverFactory;
import org.drools.beliefs.bayes.assembler.BayesPackage;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.internal.io.ResourceTypePackage;
import org.kie.internal.weaver.KieWeaverService;

import java.util.Map;

public class BayesWeaverService implements KieWeaverService<BayesWeaver, BayesPackage> {

    public BayesWeaverService() {

    }

//BayesWeaverFactory    @Override
//    public void preInit(KieWeaverContext ctx) {
//
//    }
//
//    @Override
//    public void init(KieWeaverContext ctx) {
//
//    }
//
//    @Override
//    public void postInit(KieWeaverContext ctx) {
//
//    }


    @Override
    public Class getServiceInterface() {
        return KieWeaverService.class;
    }

//    @Override
//    public RequiredResourceType[] getRequiredResourceType() {
//        return new RequiredResourceType[0];
//    }
//
//    @Override
//    public ResourceType getResourceType() {
//        return ResourceType.BAYES;
//    }
//
//    @Override
//    public BayesWeaver newKieWeaver(KieBase kieBase) {
//        return new BayesWeaver((org.drools.core.impl.InternalKnowledgeBase) kieBase );
//    }


    @Override
    public ResourceType getResourceType() {
        return ResourceType.BAYES;
    }

    @Override
    public void merge(KieBase kieBase, KiePackage kiePkg, BayesPackage bayesPkg) {
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
    public void weave(KieBase kieBase, KiePackage kiePkg, BayesPackage rtPkg) {
        System.out.println( "Hello World " );
    }
}
