/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.core.base;

import org.drools.core.common.LeftTupleSets;
import org.drools.core.common.WorkingMemoryAction;
import org.drools.core.phreak.StackEntry;
import org.drools.core.reteoo.LeftTupleSink;
import org.drools.core.reteoo.PathMemory;
import org.drools.core.reteoo.QueryElementNode.QueryElementNodeMemory;
import org.drools.core.rule.Query;
import org.kie.api.runtime.rule.Variable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DroolsQuery extends ArrayElements {
    private final String                           name;
    private       InternalViewChangedEventListener resultsCollector;
    private       Query                            query;
    private       boolean                          open;
    private       Variable[]                       vars;
    private       WorkingMemoryAction              action;
    private       LeftTupleSets                    resultLeftTuples;
    private       QueryElementNodeMemory           qmem;
    private       List<PathMemory>                 pmems;
    private       StackEntry                       stackEntry;
    private       LeftTupleSink                    sink;
    private       Map<DroolsQuery, Integer>        existingQueries;

    //    public DroolsQuery(DroolsQuery droolsQuery) {
    //        super( new Object[droolsQuery.getElements().length] );
    //
    //        this.name = droolsQuery.getName();
    //        this.resultsCollector = droolsQuery.getQueryResultCollector();
    //        this.open = droolsQuery.isOpen();
    //        final Object[] params = getElements();
    //        System.arraycopy( droolsQuery.getElements(), 0, params, 0, params.length );
    //        originalDroolsQuery = droolsQuery;
    //    }

    public DroolsQuery(final String name,
                       final Object[] params,
                       final InternalViewChangedEventListener resultsCollector,
                       final boolean open) {
        this(name, params, resultsCollector, open, null, null, null, null, null);
    }

    public DroolsQuery(final String name,
                       final Object[] params,
                       final InternalViewChangedEventListener resultsCollector,
                       final boolean open,
                       final StackEntry stackEntry,
                       final List<PathMemory> pmems,
                       final LeftTupleSets resultLeftTuples,
                       final QueryElementNodeMemory qmem,
                       final LeftTupleSink sink) {
        setParameters(params);
        this.name = name;
        this.resultsCollector = resultsCollector;
        this.stackEntry = stackEntry;
        this.open = open;
        this.pmems = pmems;
        this.resultLeftTuples = resultLeftTuples;
        this.qmem = qmem;
        this.sink = sink;
    }

    public void setParameters(final Object[] params) {
        setElements(params);
        // build the indexes to the Variables  
        if (params != null) {
            vars = new Variable[params.length];
            for (int i = 0; i < params.length; i++) {
                if (params[i] == Variable.v) {
                    vars[i] = Variable.v;
                    params[i] = null;
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Variable[] getVariables() {
        return this.vars;
    }

    public LeftTupleSets getResultLeftTupleSets() {
        return resultLeftTuples;
    }

    public StackEntry getStackEntry() {
        return this.stackEntry;
    }

    public List<PathMemory> getRuleMemories() {
        return this.pmems;
    }

    public QueryElementNodeMemory getQueryNodeMemory() {
        return qmem;
    }

    public LeftTupleSink getLeftTupleSink() {
        return this.sink;
    }

    public void setQuery(Query query) {
        // this is set later, as we don't yet know which Query will match this DroolsQuery propagation
        this.query = query;
    }

    public Query getQuery() {
        return this.query;
    }

    public InternalViewChangedEventListener getQueryResultCollector() {
        return this.resultsCollector;
    }

    public boolean isOpen() {
        return open;
    }

    public WorkingMemoryAction getAction() {
        return action;
    }

    public void setAction(WorkingMemoryAction action) {
        this.action = action;
    }

    public Map<DroolsQuery, Integer> getExistingQueries() {
        return existingQueries;
    }

    public void setExistingQueries(Map<DroolsQuery, Integer> existingQueries) {
        this.existingQueries = existingQueries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Arrays.hashCode(vars);
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        DroolsQuery that = (DroolsQuery) o;

        if (!name.equals(that.name)) { return false; }
        if (!Arrays.equals(vars, that.vars)) { return false; }

        Object[] elements = getElements();
        if (!Arrays.equals(elements, that.getElements())) { return false; }

        return true;
    }

    @Override
    public String toString() {
        return "DroolsQuery [name=" + name + ", resultsCollector=" + resultsCollector +
               ", query=" + query + ", open=" + open +
               ", args=" + Arrays.toString(getElements()) +
               ", vars=" + Arrays.toString(vars) + "]";
    }


}
