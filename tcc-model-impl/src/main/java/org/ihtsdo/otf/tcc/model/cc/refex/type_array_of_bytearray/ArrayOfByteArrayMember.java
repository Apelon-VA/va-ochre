/*
 * Copyright 2011 International Health Terminology Standards Development Organisation.
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
package org.ihtsdo.otf.tcc.model.cc.refex.type_array_of_bytearray;

import org.ihtsdo.otf.tcc.api.blueprint.ComponentProperty;
import org.ihtsdo.otf.tcc.api.blueprint.RefexCAB;
import org.ihtsdo.otf.tcc.api.hash.Hashcode;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.refex.type_array_of_bytearray.RefexArrayOfBytearrayAnalogBI;
import org.ihtsdo.otf.tcc.api.refex.type_array_of_bytearray.RefexArrayOfBytearrayVersionBI;
import org.ihtsdo.otf.tcc.api.uuid.UuidT5Generator;
import org.ihtsdo.otf.tcc.dto.component.refex.type_array_of_bytearray.TtkRefexArrayOfByteArrayMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.refex.type_array_of_bytearray.TtkRefexArrayOfByteArrayRevision;
import org.ihtsdo.otf.tcc.model.cc.component.ConceptComponent;
import org.ihtsdo.otf.tcc.model.cc.component.RevisionSet;
import org.ihtsdo.otf.tcc.model.cc.computer.version.VersionComputer;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMember;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMemberVersion;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kec
 */
public class ArrayOfByteArrayMember extends RefexMember<ArrayOfByteArrayRevision, ArrayOfByteArrayMember>
        implements RefexArrayOfBytearrayAnalogBI<ArrayOfByteArrayRevision> {

    private static VersionComputer<RefexMemberVersion<ArrayOfByteArrayRevision, ArrayOfByteArrayMember>> computer =
            new VersionComputer<>();
    //~--- fields --------------------------------------------------------------
    private byte[][] arrayOfByteArray;

    @Override
    public byte[][] getArrayOfByteArray() {
        return arrayOfByteArray;
    }

    @Override
    public void setArrayOfByteArray(byte[][] byteArray) {
        this.arrayOfByteArray = byteArray;
        modified();
    }


    //~--- constructors --------------------------------------------------------
    public ArrayOfByteArrayMember() {
        super();
    }

    public ArrayOfByteArrayMember(int enclosingConceptNid, DataInputStream input) throws IOException {
        super(enclosingConceptNid, input);
    }

    public ArrayOfByteArrayMember(TtkRefexArrayOfByteArrayMemberChronicle refsetMember, int enclosingConceptNid) throws IOException {
        super(refsetMember, enclosingConceptNid);
        arrayOfByteArray = refsetMember.getArrayOfByteArray1();

        if (refsetMember.getRevisionList() != null) {
            revisions = new RevisionSet(primordialStamp);

            for (TtkRefexArrayOfByteArrayRevision eVersion : refsetMember.getRevisionList()) {
                revisions.add(new ArrayOfByteArrayRevision(eVersion, this));
            }
        }
    }

    //~--- methods -------------------------------------------------------------
    @Override
    protected void addRefsetTypeNids(Set<Integer> allNids) {
        // ;
    }
 
    @Override
    protected void addSpecProperties(RefexCAB rcs) {
        rcs.with(ComponentProperty.ARRAY_OF_BYTEARRAY, arrayOfByteArray);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (RefexArrayOfBytearrayVersionBI.class.equals(obj.getClass())) {
            RefexArrayOfBytearrayVersionBI another = (RefexArrayOfBytearrayVersionBI) obj;

            return this.nid == another.getNid();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Hashcode.compute(new int[]{nid});
    }

    @Override
    public ArrayOfByteArrayRevision makeAnalog() {
        ArrayOfByteArrayRevision newR = new ArrayOfByteArrayRevision(getStatus(), getTime(),
                getAuthorNid(), getModuleNid(), getPathNid(), this);

        return newR;
    }
    
    @Override
    public ArrayOfByteArrayRevision makeAnalog(org.ihtsdo.otf.tcc.api.coordinate.Status status, long time, int authorNid, int moduleNid, int pathNid) {
        ArrayOfByteArrayRevision newR = new ArrayOfByteArrayRevision(status, time,
                authorNid, moduleNid, pathNid, this);

        addRevision(newR);

        return newR;
    }

    @Override
    protected boolean refexFieldsEqual(ConceptComponent<ArrayOfByteArrayRevision, ArrayOfByteArrayMember> obj) {
        if (ArrayOfByteArrayMember.class.isAssignableFrom(obj.getClass())) {
            ArrayOfByteArrayMember another = (ArrayOfByteArrayMember) obj;

            return Arrays.deepEquals(this.arrayOfByteArray, another.arrayOfByteArray);
        }

        return false;
    }

    @Override
    public boolean refexFieldsEqual(RefexVersionBI another) {
        if(RefexArrayOfBytearrayVersionBI.class.isAssignableFrom(another.getClass())){
            RefexArrayOfBytearrayVersionBI bv = (RefexArrayOfBytearrayVersionBI) another;
            return Arrays.deepEquals(this.arrayOfByteArray, bv.getArrayOfByteArray());
        }
        return false;
    }

    @Override
    protected void readMemberFields(DataInputStream in) throws IOException {
      int arrayLength = in.readShort();
      this.arrayOfByteArray = new byte[arrayLength][];
      for (int i = 0; i < arrayLength; i++) {
          int byteArrayLength = in.readInt();
          this.arrayOfByteArray[i] = new byte[byteArrayLength];
          in.readFully(this.arrayOfByteArray[i], 0, byteArrayLength);
      }
    }

    @Override
    protected final ArrayOfByteArrayRevision readMemberRevision(DataInputStream input) throws IOException {
        return new ArrayOfByteArrayRevision(input, this);
    }

    @Override
    public boolean readyToWriteRefsetMember() {
        return true;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
     buff.append("AOBA size: ");
      buff.append(this.arrayOfByteArray.length);
      for (int i = 0; i < this.arrayOfByteArray.length; i++) {
        buff.append(" ").append(i);
        buff.append(": ");
        if(this.arrayOfByteArray[i].length == 16){
            buff.append(UuidT5Generator.getUuidFromRawBytes(this.arrayOfByteArray[i]));
        }else{
            buff.append(this.arrayOfByteArray[i]);
        }
        
      }
      buff.append(" ");
      buff.append(super.toString());

        return buff.toString();
    }

    @Override
    protected void writeMember(DataOutput out) throws IOException {
     out.writeShort(arrayOfByteArray.length);
      for (byte[] bytes: arrayOfByteArray) {
        out.writeInt(bytes.length);  
        out.write(bytes);
      }
    }

    //~--- get methods ---------------------------------------------------------

    @Override
    protected RefexType getTkRefsetType() {
        return RefexType.ARRAY_BYTEARRAY;
    }

    @Override
    public int getTypeNid() {
        return RefexType.ARRAY_BYTEARRAY.getTypeToken();
    }

    @Override
    protected VersionComputer<RefexMemberVersion<ArrayOfByteArrayRevision, ArrayOfByteArrayMember>> getVersionComputer() {
        return computer;
    }

    @Override
    public List<ArrayOfByteArrayMemberVersion> getVersions() {
        if (versions == null) {
            int count = 1;

            if (revisions != null) {
                count = count + revisions.size();
            }

            ArrayList<ArrayOfByteArrayMemberVersion> list = new ArrayList<>(count);

            if (getTime() != Long.MIN_VALUE) {
                list.add(new ArrayOfByteArrayMemberVersion(this, this));
            }

            if (revisions != null) {
                for (ArrayOfByteArrayRevision br : revisions) {
                    if (br.getTime() != Long.MIN_VALUE) {
                        list.add(new ArrayOfByteArrayMemberVersion(br, this));
                    }
                }
            }

            versions = list;
        }

        return (List<ArrayOfByteArrayMemberVersion>) versions;
    }

}
