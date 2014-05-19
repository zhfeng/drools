package org.drools.core.weaver;

import java.util.List;

public class KieWeaverContextImpl implements KieWeaverContext {
    private int                    index;
    private List<KieWeaverFactory> list;

    public KieWeaverContextImpl(List<KieWeaverFactory> list) {
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
    public List<KieWeaverFactory> getFactories() {
        return list;
    }
}
