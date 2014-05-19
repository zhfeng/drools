package org.drools.core.weaver;

import org.kie.api.io.ResourceType;

public interface RequiredResourceType {
    boolean isOptional();
    ResourceType getResourceType();
}
