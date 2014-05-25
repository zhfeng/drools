package org.drools.compiler.assembler;

import org.drools.core.weaver.KieWeaverFactory;

import java.util.List;

public class KieAssemblerContextImpl<T extends KieAssemblerFactory> implements KieAssemblerContext<T> {
    private int index;
    private List<T> list;

    public KieAssemblerContextImpl(List<T> list) {
        this.list = list;
    }

    public void incrementIndex() {
        index++;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public List<T> getFactories() {
        return list;
    }
}
