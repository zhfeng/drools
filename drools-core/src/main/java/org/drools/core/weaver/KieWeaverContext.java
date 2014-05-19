package org.drools.core.weaver;

import java.util.List;

public interface KieWeaverContext {
    /**
     * The index position of the current KieWeaverFactory
     * @return
     */
    int getIndex();

    /**
     * The List is in initialization order
     * @return
     */
    List<KieWeaverFactory> getFactories();
}
