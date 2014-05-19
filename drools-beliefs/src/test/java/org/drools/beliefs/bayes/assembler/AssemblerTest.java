package org.drools.beliefs.bayes.assembler;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.internal.io.ResourceFactory;

import static org.junit.Assert.assertNotNull;

public class AssemblerTest {

    @Test
    public void testBayesPackageAssembly() throws Exception {
        KnowledgeBuilderImpl kbuilder = new KnowledgeBuilderImpl();
        kbuilder.add( ResourceFactory.newClassPathResource("Sprinkler.xmlbif", AssemblerTest.class), ResourceType.BAYES );

        InternalKnowledgePackage kpkg = kbuilder.getPackageRegistry("bayes").getPackage();
        BayesPackage bkpg = (BayesPackage) kpkg.getResourceTypePackages().get( ResourceType.BAYES );
        assertNotNull(bkpg.getJunctionTree("Sprinkler"));
    }
}
