package org.drools.core;

import org.drools.core.weaver.RequiredResourceType;
import org.kie.api.io.ResourceType;

public interface KieFactory<T extends KieFactoryInitContext> {
    void preInit(T ctx);
    void init(T ctx);
    void postInit(T ctx);

    ResourceType getResourceType();

    RequiredResourceType[] getRequiredResourceType();
}
