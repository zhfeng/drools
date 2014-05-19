package org.drools.compiler.assembler;

import org.drools.core.weaver.KieWeaverFactory;

import java.util.List;

public interface KieAssemblerContext {
    /**
     * The index position of the current KieWeaverFactory
     * @return
     */
    int getIndex();

    /**
     * The List is in initialization order
     * @return
     */
    List<KieAssemblerFactory> getFactories();
}
