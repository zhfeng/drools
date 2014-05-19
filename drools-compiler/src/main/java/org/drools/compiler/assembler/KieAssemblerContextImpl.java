package org.drools.compiler.assembler;

import org.drools.core.weaver.KieWeaverFactory;

import java.util.List;

public class KieAssemblerContextImpl implements KieAssemblerContext {
    private int index;
    private List<KieAssemblerFactory> list;

    public KieAssemblerContextImpl(List<KieAssemblerFactory> list) {
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
    public List<KieAssemblerFactory> getFactories() {
        return list;
    }
}
