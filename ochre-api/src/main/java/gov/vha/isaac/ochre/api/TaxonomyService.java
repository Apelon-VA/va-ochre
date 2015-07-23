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
package gov.vha.isaac.ochre.api;

import gov.vha.isaac.ochre.api.component.sememe.SememeChronology;
import gov.vha.isaac.ochre.api.component.sememe.version.LogicGraphSememe;
import gov.vha.isaac.ochre.api.coordinate.TaxonomyCoordinate;
import gov.vha.isaac.ochre.api.tree.Tree;
import gov.vha.isaac.ochre.collections.ConceptSequenceSet;
import java.util.stream.IntStream;
import org.jvnet.hk2.annotations.Contract;

/**
 *
 * @author kec
 */
@Contract
public interface TaxonomyService {
    
    TaxonomySnapshotService getSnapshot(TaxonomyCoordinate tc);
    
    Tree getTaxonomyTree(TaxonomyCoordinate tc);
    
    boolean isChildOf(int childId, int parentId, TaxonomyCoordinate tc);
    
    boolean isKindOf(int childId, int parentId, TaxonomyCoordinate tc);
    
    /**
     * Method to determine if a concept was ever a kind of another, without
     * knowing a TaxonomyCoordinate. 
     * @param childId
     * @param parentId
     * @return true if child was ever a kind of the parent. 
     */
    boolean wasEverKindOf(int childId, int parentId);
    
    ConceptSequenceSet getKindOfSequenceSet(int rootId, TaxonomyCoordinate tc);
     
    ConceptSequenceSet getChildOfSequenceSet(int parentId, TaxonomyCoordinate tc);
     
    IntStream getAllRelationshipOriginSequences(int destinationId, TaxonomyCoordinate tc);
    
    IntStream getAllRelationshipOriginSequences(int destinationId);

    IntStream getAllRelationshipDestinationSequences(int originId, TaxonomyCoordinate tc);
    
    IntStream getAllRelationshipDestinationSequences(int originId);
    
    IntStream getAllRelationshipDestinationSequencesOfType(int originId, ConceptSequenceSet typeSequenceSet, TaxonomyCoordinate tc);
    
    IntStream getAllRelationshipDestinationSequencesOfType(int originId, ConceptSequenceSet typeSequenceSet);

    IntStream getAllRelationshipOriginSequencesOfType(int destinationId, ConceptSequenceSet typeSequenceSet, TaxonomyCoordinate tc);
    
    IntStream getAllRelationshipOriginSequencesOfType(int destinationId, ConceptSequenceSet typeSequenceSet);

    IntStream getTaxonomyChildSequences(int parentId, TaxonomyCoordinate tc);
    
    IntStream getTaxonomyChildSequences(int parentId);
    
    IntStream getTaxonomyParentSequences(int childId, TaxonomyCoordinate tc);
    
    IntStream getTaxonomyParentSequences(int childId);
    
    IntStream getRoots(TaxonomyCoordinate sc);
    
    /**
     * Update the taxonomy by extracting relationships from the logical definitions
     * in the {@code logicGraphChronology}. This method will be called by a commit
     * listener, so developers do not have to update the taxonomy themselves, unless
     * developing an alternative taxonomy service implementation. 
     * @param logicGraphChronology Chronology of the logical definitions
     */
    void updateTaxonomy(SememeChronology<LogicGraphSememe<?>> logicGraphChronology);
}
