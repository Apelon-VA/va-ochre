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
package gov.vha.isaac.ochre.api.tree.hashtree;

import gov.vha.isaac.ochre.collections.ConceptSequenceSet;
import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * Simple implementation that uses less space, but does not have some of the
 * features of the {@code HashTreeWithBitSets} which caches some tree features
 * as {@code BitSet} objects. Meant for use with short-lived and small trees. 
 *
 * @author kec
 */
public class SimpleHashTree extends AbstractHashTree {

    /**
     * NOTE: not a constant time operation.
     * @return root sequences for this tree. 
     */
    @Override
    public int[] getRootSequences() {
        return getRootSequenceStream().toArray();
    }

    @Override
    public IntStream getRootSequenceStream() {
        ConceptSequenceSet parents = new ConceptSequenceSet();
        parentSequence_ChildSequenceArray_Map.forEachKey((int parent) -> {
            parents.add(parent);
            return true;
        });
        parentSequence_ChildSequenceArray_Map.forEachPair((int parent, int[] children) -> {
            IntStream.of(children).forEach((child) -> parents.remove(child));
            return true;
        });
        return parents.stream();
    }

    /**
     * NOTE: not a constant time operation. 
     * @return number of unique nodes in this tree. 
     */
    @Override
    public int size() {
        IntStream.Builder builder = IntStream.builder();
        parentSequence_ChildSequenceArray_Map.forEachPair((int first, int[] second) -> {
            builder.accept(first);
            IntStream.of(second).forEach((sequence) -> builder.add(sequence));
            return true;
        });
        return (int) builder.build().distinct().count();
    }

    public void addChild(int parentSequence, int childSequence) {
        maxSequence = Math.max(parentSequence, maxSequence);
        maxSequence = Math.max(childSequence, maxSequence);
        
        if (parentSequence_ChildSequenceArray_Map.containsKey(parentSequence)) {
            IntStream.Builder builder = IntStream.builder();
            builder.add(childSequence);
            IntStream.of(parentSequence_ChildSequenceArray_Map.get(parentSequence)).forEach((sequence) -> builder.add(sequence));
            childSequence_ParentSequenceArray_Map.put(childSequence, builder.build().distinct().sorted().toArray());
        } else {
            parentSequence_ChildSequenceArray_Map.put(parentSequence, new int[] {childSequence});
        }
        
        if (childSequence_ParentSequenceArray_Map.containsKey(childSequence)) {
            IntStream.Builder builder = IntStream.builder();
            builder.add(parentSequence);
            IntStream.of(childSequence_ParentSequenceArray_Map.get(childSequence)).forEach((sequence) -> builder.add(sequence));
            childSequence_ParentSequenceArray_Map.put(childSequence, builder.build().distinct().sorted().toArray());
        } else {
            childSequence_ParentSequenceArray_Map.put(childSequence, new int[] {parentSequence});
        }
    }
}
