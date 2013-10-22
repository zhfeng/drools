/*
 * Copyright 2010 JBoss Inc
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

package org.drools.core.reteoo.builder;

import org.drools.core.base.ClassObjectType;
import org.drools.core.reteoo.AlphaNode;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.LeftTupleSource;
import org.drools.core.reteoo.NodeTypeEnums;
import org.drools.core.reteoo.QueryElementNode;
import org.drools.core.rule.Query;
import org.drools.core.rule.QueryElement;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.rule.constraint.QueryNameConstraint;
import org.drools.core.spi.ObjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class QueryElementBuilder
    implements
    ReteooComponentBuilder {

    /**
     * @inheritDoc
     */
    public void build(final BuildContext context,
                      final BuildUtils utils,
                      final RuleConditionElement rce) {

        final QueryElement qe = (QueryElement) rce;
        context.pushRuleComponent( qe );
        
        final int currentOffset = context.getCurrentPatternOffset();
        
        qe.getResultPattern().setOffset( currentOffset );
        
        utils.checkUnboundDeclarations( context,
                                        qe.getRequiredDeclarations() );

        QueryElementNode node = (QueryElementNode) utils.attachNode( context,
                                                                     context.getComponentFactory().getNodeFactoryService()
                                                                            .buildQueryElementNode(context.getNextId(),
                                                                                                   context.getTupleSource(),
                                                                                                   qe,
                                                                                                   context.isTupleMemoryEnabled(),
                                                                                                   qe.isOpenQuery(),
                                                                                                   context) );

        if ( node.getAssociations().size() == 1 && context.getRule() instanceof Query ) {
            // popuplate lookup, if nested, so transitive graphs can be check for cycles
            Map<String, List<QueryElementNode>> nestedQueryNodeLookup = context.getRuleBase().getReteooBuilder().getNestedQueryNodeLookup();

            String targetQueryName = node.getQueryElement().getQueryName();
            List<QueryElementNode> list =  nestedQueryNodeLookup.get(targetQueryName);
            if ( list == null ) {
                list = new ArrayList<QueryElementNode>();
                nestedQueryNodeLookup.put( targetQueryName, list);
            }
            list.add( node );

            // now check if it's recursive
            if ( isRecursive(targetQueryName, context.getRule().getName(), nestedQueryNodeLookup) ) {
                node.setRecursive( true );
            }
        }

        context.setTupleSource( node );

        context.popRuleComponent();
        context.incrementCurrentPatternOffset();
    }

    public boolean isRecursive(final String targetQueryName,
                               final String parentQueryName,
                               Map<String, List<QueryElementNode>> nestedQueryNodeLookup) {
        if ( targetQueryName.equals(parentQueryName)) {
            return true;
        }

        List<QueryElementNode> list = nestedQueryNodeLookup.get( parentQueryName );
        if ( list != null ) {
            for ( QueryElementNode parentNode : list ) {
                LeftTupleSource lts = parentNode.getLeftTupleSource();
                while ( lts.getType() != NodeTypeEnums.LeftInputAdapterNode ) {
                    lts = lts.getLeftTupleSource();
                }
                LeftInputAdapterNode lian = ( LeftInputAdapterNode ) lts;
                String grandparentQueryName = ((QueryNameConstraint)((AlphaNode)lian.getParentObjectSource()).getConstraint()).getQueryName();

                if ( isRecursive( targetQueryName, grandparentQueryName, nestedQueryNodeLookup) ) {
                    return true;
                }
            }
        }

//
//        LeftTupleSource lts = node.getLeftTupleSource();
//        while ( lts.getType() != NodeTypeEnums.LeftInputAdapterNode ) {
//            lts = lts.getLeftTupleSource();
//        }
//        LeftInputAdapterNode lian = ( LeftInputAdapterNode ) lts;
//        String parentName = ((QueryNameConstraint)((AlphaNode)lian.getParentObjectSource()).getConstraint()).getQueryName();


        return false;
    }

    /**
     * @inheritDoc
     */
    public boolean requiresLeftActivation(final BuildUtils utils,
                                          final RuleConditionElement rce) {
        return true;
    }

}
