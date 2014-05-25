package org.drools.core.runtime;

import org.drools.core.KieFactory;
import org.drools.core.common.InternalKnowledgeRuntime;
import org.drools.core.weaver.KieWeaver;
import org.kie.api.KieBase;

public interface KieRuntimeManagerFactory<T> extends KieFactory<KieRuntimeManagerContext> {
    T newKieRuntime(InternalKnowledgeRuntime session);
}
