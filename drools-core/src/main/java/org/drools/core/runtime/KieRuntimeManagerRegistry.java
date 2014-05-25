package org.drools.core.runtime;

import org.drools.core.util.BaseFactoryRegistry;
import org.drools.core.weaver.KieWeaverContext;
import org.drools.core.weaver.KieWeaverFactory;

import java.util.List;

public class KieRuntimeManagerRegistry extends BaseFactoryRegistry<KieRuntimeManagerFactory, KieRuntimeManagerContext> {

    private static final KieRuntimeManagerRegistry INSTANCE = new KieRuntimeManagerRegistry();

    public static KieRuntimeManagerRegistry getInstance() {
        return INSTANCE;
    }

    private KieRuntimeManagerRegistry() {
        super("kruntimemanager.properties", "Kie Runtime Manager");
    }

    @Override
    public KieRuntimeManagerContext newKieFactoryInitContext(List<KieRuntimeManagerFactory> list) {
        return null;
    }

}
