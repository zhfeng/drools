package org.drools.compiler.assembler;

import org.kie.api.io.Resource;
import org.kie.api.io.ResourceConfiguration;
import org.kie.api.io.ResourceType;

public interface KieAssembler {
    void addResource(Resource resource, ResourceType type, ResourceConfiguration configuration) throws Exception;
}
