package org.drools.core.phreak;

import org.drools.core.base.DroolsQuery;
import org.drools.core.base.extractors.ArrayElementReader;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.LeftTupleSets;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.LeftInputAdapterNode.LiaNodeMemory;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.reteoo.LeftTupleSink;
import org.drools.core.reteoo.QueryElementNode;
import org.drools.core.reteoo.QueryElementNode.QueryElementNodeMemory;
import org.drools.core.reteoo.QueryElementNode.UnificationNodeViewChangedEventListener;
import org.drools.core.reteoo.SegmentMemory;
import org.drools.core.rule.Declaration;
import org.drools.core.spi.PropagationContext;
import org.kie.api.runtime.rule.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PhreakQueryNode {
    public void doNode(QueryElementNode queryNode,
                       QueryElementNodeMemory qmem,
                       StackEntry stackEntry,
                       LeftTupleSink sink,
                       InternalWorkingMemory wm,
                       LeftTupleSets srcLeftTuples,
                       LeftTupleSets trgLeftTuples,
                       LeftTupleSets stagedLeftTuples) {

        if (srcLeftTuples.getDeleteFirst() != null) {
            doLeftDeletes(queryNode,qmem, wm, srcLeftTuples, trgLeftTuples, stagedLeftTuples);
        }

        if (srcLeftTuples.getUpdateFirst() != null) {
            doLeftUpdates(queryNode, qmem, sink, wm, srcLeftTuples, trgLeftTuples, stagedLeftTuples);
        }

        if (srcLeftTuples.getInsertFirst() != null) {
            doLeftInserts(queryNode, qmem, stackEntry, wm, srcLeftTuples);
        }

        srcLeftTuples.resetAll();
    }

    public void doLeftInserts(QueryElementNode queryNode,
                              QueryElementNodeMemory qmem,
                              StackEntry stackEntry,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getInsertFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            PropagationContext pCtx = (PropagationContext) leftTuple.getPropagationContext();

            InternalFactHandle handle = queryNode.createFactHandle(pCtx,
                                                                   wm,
                                                                   leftTuple);

            Object root = ((InternalFactHandle) leftTuple.get( 0 )).getObject();

            DroolsQuery dquery = queryNode.createDroolsQuery(leftTuple, handle, stackEntry,
                                                             qmem.getSegmentMemory().getPathMemories(),
                                                             qmem,
                                                             qmem.getResultLeftTuples(),
                                                             stackEntry.getSink(), wm);

            initExistingQueries(root, dquery);
            if ( queryNode.isRecursive() ) {
                boolean exists = insertExistingDroolsQueries(dquery);

                if ( exists ) {
                    leftTuple.clearStaged();
                    leftTuple = next;
                    continue;
                }
            }

            LeftInputAdapterNode lian = (LeftInputAdapterNode) qmem.getQuerySegmentMemory().getRootNode();
            LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
            LeftInputAdapterNode.doInsertObject(handle, pCtx, lian, wm, lm, false, dquery.isOpen());

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    private boolean insertExistingDroolsQueries(DroolsQuery dquery) {
        Map<DroolsQuery, Integer> existingQueries = dquery.getExistingQueries();
        Integer i = existingQueries.get(dquery);
        if (  i != null) {
            // this query already exists, so ignore, and increase counter
            existingQueries.put(dquery, i.intValue() + 1 );
            return true;
        } else {
            existingQueries.put(dquery, 1 );
            return false;
        }
    }

    private void initExistingQueries(Object root, DroolsQuery dquery) {
        Map<DroolsQuery, Integer> existingQueries = null;
//        if ( root instanceof DroolsQuery ) {
//            // if caller is a query, then use it's existingQueries Set
//            DroolsQuery parentDQuery = (DroolsQuery) root;
//            existingQueries = parentDQuery.getExistingQueries();
//        }

        if ( existingQueries == null ) {
            // parent is not a Query, so create new existingQueries Set.
            existingQueries = new HashMap<DroolsQuery, Integer>();
            existingQueries.put(dquery, 1);
        } else {
            DroolsQuery parentDQuery = (DroolsQuery) root;
            existingQueries = parentDQuery.getExistingQueries();
        }

        dquery.setExistingQueries(existingQueries);
    }

    public void doLeftUpdates(QueryElementNode queryNode,
                              QueryElementNodeMemory qmem,
                              LeftTupleSink sink,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples,
                              LeftTupleSets trgLeftTuples,
                              LeftTupleSets stagedLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getUpdateFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            InternalFactHandle fh = (InternalFactHandle) leftTuple.getObject();
            DroolsQuery dquery = (DroolsQuery) fh.getObject();

            if ( queryNode.isRecursive() ) {
                deleteExistingQueries(dquery, dquery.getExistingQueries());
            }

            Object[] argTemplate = queryNode.getQueryElement().getArgTemplate(); // an array of declr, variable and literals
            Object[] args = new Object[argTemplate.length]; // the actual args, to be created from the  template

            // first copy everything, so that we get the literals. We will rewrite the declarations and variables next
            System.arraycopy(argTemplate,
                             0,
                             args,
                             0,
                             args.length);

            int[] declIndexes = queryNode.getQueryElement().getDeclIndexes();

            for (int i = 0, length = declIndexes.length; i < length; i++) {
                Declaration declr = (Declaration) argTemplate[declIndexes[i]];

                Object tupleObject = leftTuple.get(declr).getObject();

                Object o;

                if (tupleObject instanceof DroolsQuery) {
                    // If the query passed in a Variable, we need to use it
                    ArrayElementReader arrayReader = (ArrayElementReader) declr.getExtractor();
                    if (((DroolsQuery) tupleObject).getVariables()[arrayReader.getIndex()] != null) {
                        o = Variable.v;
                    } else {
                        o = declr.getValue(wm,
                                           tupleObject);
                    }
                } else {
                    o = declr.getValue(wm,
                                       tupleObject);
                }

                args[declIndexes[i]] = o;
            }

            int[] varIndexes = queryNode.getQueryElement().getVariableIndexes();
            for (int i = 0, length = varIndexes.length; i < length; i++) {
                if (argTemplate[varIndexes[i]] == Variable.v) {
                    // Need to check against the arg template, as the varIndexes also includes re-declared declarations
                    args[varIndexes[i]] = Variable.v;
                }
            }

            dquery.setParameters(args);
            ((UnificationNodeViewChangedEventListener) dquery.getQueryResultCollector()).setVariables(varIndexes);

            Object root = ((InternalFactHandle) leftTuple.get( 0 )).getObject();
            initExistingQueries(root, dquery);

            if ( queryNode.isRecursive() ) {

                boolean exists = insertExistingDroolsQueries(dquery);

                if ( exists ) {
                    leftTuple.clearStaged();
                    leftTuple = next;
                    continue;
                }
            }

            SegmentMemory qsmem = qmem.getQuerySegmentMemory();
            LeftInputAdapterNode lian = (LeftInputAdapterNode) qsmem.getRootNode();
            LiaNodeMemory lmem = (LiaNodeMemory) qsmem.getNodeMemories().getFirst();
            LeftTuple childLeftTuple = fh.getFirstLeftTuple(); // there is only one, all other LTs are peers
            if (childLeftTuple != null) {
                // If the query is open, it will have a child.
                // the query may be open and not have  child due to recursion, which once detected deletes the child.
                LeftInputAdapterNode.doUpdateObject(childLeftTuple, childLeftTuple.getPropagationContext(), wm, lian, false, lmem, qmem.getQuerySegmentMemory());
            } else {
                LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
                LeftInputAdapterNode.doInsertObject(fh, leftTuple.getPropagationContext(), lian, wm, lm, false, dquery.isOpen());
            }


            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    private void deleteExistingQueries(DroolsQuery dquery, Map<DroolsQuery, Integer> existingQueries) {
        Integer i = existingQueries.remove( dquery );
        // as it's an update, atleast one must exist
        // Decrease the counter, put back in if greater than 0.
        int i2 = i.intValue() - 1;
        if ( i2 > 0 ) {
            existingQueries.put(dquery, i2);
        }
    }

    public void doLeftDeletes(QueryElementNode queryNode,
                              QueryElementNodeMemory qmem,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples,
                              LeftTupleSets trgLeftTuples,
                              LeftTupleSets stagedLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getDeleteFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            InternalFactHandle fh = (InternalFactHandle) leftTuple.getObject();
            DroolsQuery dquery = (DroolsQuery) fh.getObject();

            if ( queryNode.isRecursive() ) {
                deleteExistingQueries(dquery, dquery.getExistingQueries());
            }

            deleteQuery(qmem, wm, trgLeftTuples, stagedLeftTuples, leftTuple, fh, dquery);

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    private void deleteQuery(QueryElementNodeMemory qmem, InternalWorkingMemory wm, LeftTupleSets trgLeftTuples, LeftTupleSets stagedLeftTuples, LeftTuple leftTuple, InternalFactHandle fh, DroolsQuery dquery) {
        if (dquery.isOpen()) {
            LeftInputAdapterNode lian = (LeftInputAdapterNode) qmem.getQuerySegmentMemory().getRootNode();
            LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
            LeftTuple childLeftTuple = fh.getFirstLeftTuple(); // there is only one, all other LTs are peers
            LeftInputAdapterNode.doDeleteObject(childLeftTuple, childLeftTuple.getPropagationContext(), qmem.getQuerySegmentMemory(), wm, lian, false, lm);
        } else {
            LeftTuple childLeftTuple = leftTuple.getFirstChild();
            while (childLeftTuple != null) {
                childLeftTuple = RuleNetworkEvaluator.deleteLeftChild(childLeftTuple, trgLeftTuples, stagedLeftTuples);
            }
        }
    }
}
