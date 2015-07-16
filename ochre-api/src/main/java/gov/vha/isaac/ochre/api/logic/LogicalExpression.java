/*
 * Copyright 2015 kec.
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
package gov.vha.isaac.ochre.api.logic;

import gov.vha.isaac.ochre.api.DataTarget;
import gov.vha.isaac.ochre.api.tree.TreeNodeVisitData;


import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * A tree representation of a logical expression, able to represent
 * all EL++ as well as full first order logic for future compatibility. 
 * @author kec
 */
public interface LogicalExpression {
    
    /**
     * 
     * @param dataTarget if the serialization should be targeted for internal or universal use. 
     * @return a array of byte arrays that represent this logical expression 
     * for serialization
     */
    byte[][] getData(DataTarget dataTarget);
    
    /**
     * 
     * @return true if the expression is sufficiently complete to be meaningful. 
     */
    boolean isMeaningful();

    /**
     * 
     * @return the concept sequence this expression is associated with 
     */
    int getConceptSequence();

    /**
     * 
     * @param nodeIndex
     * @return the node corresponding to the node index 
     */
    Node getNode(int nodeIndex);

    /**
     * 
     * @return the number of nodes in this expression
     */
    int getNodeCount();

    /**
     * 
     * @return the root node if this expression 
     */
    Node getRoot();

    /**
     * Present the consumer the nodes of this expression in a depth-first manner, 
     * starting with the root node. 
     * @param consumer the consumer of the nodes. 
     */
    void processDepthFirst(BiConsumer<Node, TreeNodeVisitData> consumer);
    
    /**
     * Process the fragment starting at fragmentRoot in a depth first manner. 
     * @param fragmentRoot
     * @param consumer 
     */
    void processDepthFirst(Node fragmentRoot, BiConsumer<Node, TreeNodeVisitData> consumer);

    /**
     * @param semantic the type of nodes to match
     * @return true if the expression contains at least 1 node that matches
     * the semantic
     */
    boolean contains(NodeSemantic semantic);
    
    /**
     * 
     * @param semantic the type of nodes to match
     * @return the nodes in the expression that match the NodeSemantic
     */
    Stream<Node> getNodesOfType(NodeSemantic semantic);
    
    /**
     * Find isomorphic aspects of this {@code LogicalExpression} (the reference expression) with respect
     * to another (the comparison expression). The {@code IsomorphicResults} will include the maximal 
     * common rooted isomorphic solution, as well as identify additions and deletions. 
     * @param another the other {@code LogicalExpression} to compare with
     * @return The results of the comparison. 
     */
    IsomorphicResults findIsomorphisms(LogicalExpression another);
    
    /**
     * Use to when printing out multiple expressions, and you want to differentiate the 
     * identifiers so that they are unique across all the expressions. 
     * @param nodeIdSuffix the identifier suffix for this expression. 
     * @return a text representation of this expression. 
     */
    String toString(String nodeIdSuffix);
}
