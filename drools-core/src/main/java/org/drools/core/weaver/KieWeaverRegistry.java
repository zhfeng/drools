package org.drools.core.weaver;

import org.drools.core.util.BaseFactoryRegistry;
import java.util.List;

public class KieWeaverRegistry extends BaseFactoryRegistry<KieWeaverFactory, KieWeaverContext> {

    private static final KieWeaverRegistry INSTANCE = new KieWeaverRegistry();

    public static KieWeaverRegistry getInstance() {
        return INSTANCE;
    }

    private KieWeaverRegistry() {
        super( "kweaver.properties", "Kie Weaver");
    }

    @Override
    public KieWeaverContext newKieFactoryInitContext(List<KieWeaverFactory> list) {
        return null;
    }

}
