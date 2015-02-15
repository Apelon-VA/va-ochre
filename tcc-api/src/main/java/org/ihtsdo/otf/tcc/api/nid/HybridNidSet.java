/*
 * Copyright 2013 International Health Terminology Standards Development Organisation.
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
package org.ihtsdo.otf.tcc.api.nid;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dylangrald
 */
public class HybridNidSet implements NativeIdSetBI {

    public NativeIdSetBI nidSet;
    private static int threshold = 128000;

    //~--- constructors --------------------------------------------------------
    public HybridNidSet() {
        nidSet = new IntSet();
    }

    /**
     * Need to clone, otherwise get pass by reference problems.
     *
     * @param anotherSet
     */
    public HybridNidSet(HybridNidSet anotherSet) {
        super();
        this.nidSet = anotherSet.nidSet;
    }

    public HybridNidSet(int nid) {
        super();
        nidSet = new IntSet();
        nidSet.add(nid);
    }

    public HybridNidSet(IntSet anotherSet) {
        this.nidSet = anotherSet;
        if (this.nidSet.size() < threshold) {
            this.nidSet = new IntSet(anotherSet);
        }
    }

    public HybridNidSet(ConcurrentBitSet bitSet) {
        super();
        if (bitSet.cardinality() > threshold) {
            this.nidSet = bitSet;
        } else {
            this.nidSet = new IntSet(bitSet.getSetValues());
        }

    }

    public HybridNidSet(int[] values) {
        super();
        if (values.length > threshold) {
            nidSet = new ConcurrentBitSet();
            nidSet.addAll(values);
        } else {
            nidSet = new IntSet(values);
        }

    }

    public int getThreshold() {
        return HybridNidSet.threshold;
    }

    @Override
    public NativeIdSetItrBI getSetBitIterator() {
        return this.nidSet.getSetBitIterator();
    }

    @Override
    public NativeIdSetItrBI getAllBitIterator() {
        return this.nidSet.getAllBitIterator();
    }

    @Override
    public void and(NativeIdSetBI other) {
        nidSet.and(other);
        if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void andNot(NativeIdSetBI other) {
        nidSet.andNot(other);
        if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean contains(int nid) {
        return nidSet.contains(nid);
    }

    @Override
    public int[] getSetValues() {
        return nidSet.getSetValues();
    }

    @Override
    public void add(int nid) {
        nidSet.add(nid);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void remove(int nid) {
        nidSet.remove(nid);
        if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void addAll(int[] nids) {
        this.nidSet.addAll(nids);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void removeAll(int[] nids) {
        nidSet.removeAll(nids);
        if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void clear() {
        nidSet.clear();
        nidSet = new IntSet();
    }

    @Override
    public int size() {
        return nidSet.size();
    }

    @Override
    public int getMax() {
        return nidSet.getMax();
    }

    @Override
    public int getMin() {
        return nidSet.getMin();
    }

    @Override
    public boolean contiguous() {
        return nidSet.contiguous();
    }

    @Override
    public void union(NativeIdSetBI other) {
        nidSet.union(other);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void xor(NativeIdSetBI other) {
        nidSet.xor(other);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getAllBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getAllBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean isMember(int nid) {
        return nidSet.isMember(nid);
    }

    @Override
    public void setMember(int nid) {
        nidSet.setMember(nid);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setNotMember(int nid) {
        nidSet.setNotMember(nid);
        if (this.nidSet.size() < threshold && nidSet.getClass().isAssignableFrom(ConcurrentBitSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new IntSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void or(NativeIdSetBI other) {
        nidSet.union(other);
        if (this.nidSet.size() > threshold && nidSet.getClass().isAssignableFrom(IntSet.class)) {
            NativeIdSetItrBI iter = nidSet.getSetBitIterator();
            nidSet = new ConcurrentBitSet();
            try {
                while (iter.next()) {
                    nidSet.add(iter.nid());
                }
            } catch (IOException ex) {
                Logger.getLogger(HybridNidSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.nidSet.isEmpty();
    }

    @Override
    public void setAll(int max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMaxPossibleId() {
        return this.nidSet.getMaxPossibleId();
    }

    @Override
    public int getMinPossibleId() {
        return this.nidSet.getMinPossibleId();
    }

    @Override
    public void setMaxPossibleId(int nid) {
        this.nidSet.setMaxPossibleId(nid);
    }
}
