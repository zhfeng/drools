package org.drools.core;

import java.util.List;

public interface KieFactoryInitContext<T extends KieFactory> {
    /**
     * The index position of the current Factory
     * @return
     */
    int getIndex();

    /**
     * The List is in Factory initialization order
     * @return
     */
    List<T> getFactories();
}
