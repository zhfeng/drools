package org.drools.core.runtime;

import java.util.List;

public class KieRuntimeManagerContextImpl implements KieRuntimeManagerContext {
    private int                            index;
    private List<KieRuntimeManagerFactory> list;

    public KieRuntimeManagerContextImpl(List<KieRuntimeManagerFactory> list) {
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
    public List<KieRuntimeManagerFactory> getFactories() {
        return list;
    }
}
