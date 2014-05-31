package org.ihtsdo.otf.tcc.model.cc.refex.type_string;

//~--- non-JDK imports --------------------------------------------------------

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyVetoException;

import java.io.IOException;

import java.util.*;
import org.apache.mahout.math.list.IntArrayList;
import org.ihtsdo.otf.tcc.api.blueprint.ComponentProperty;
import org.ihtsdo.otf.tcc.api.blueprint.RefexCAB;
import org.ihtsdo.otf.tcc.api.hash.Hashcode;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.refex.type_string.RefexStringAnalogBI;
import org.ihtsdo.otf.tcc.api.refex.type_string.RefexStringVersionBI;
import org.ihtsdo.otf.tcc.dto.component.refex.type_string.TtkRefexStringMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.refex.type_string.TtkRefexStringRevision;
import org.ihtsdo.otf.tcc.model.cc.component.ConceptComponent;
import org.ihtsdo.otf.tcc.model.cc.component.RevisionSet;
import org.ihtsdo.otf.tcc.model.cc.computer.version.VersionComputer;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMember;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMemberVersion;

public class StringMember extends RefexMember<StringRevision, StringMember>
        implements RefexStringAnalogBI<StringRevision> {
   private static VersionComputer<RefexMemberVersion<StringRevision, StringMember>> computer =
      new VersionComputer<>();

   //~--- fields --------------------------------------------------------------

   private String stringValue;

   //~--- constructors --------------------------------------------------------

   public StringMember() {
      super();
   }

   public StringMember(int enclosingConceptNid, TupleInput input) throws IOException {
      super(enclosingConceptNid, input);
   }

   public StringMember(TtkRefexStringMemberChronicle refsetMember, int enclosingConceptNid) throws IOException {
      super(refsetMember, enclosingConceptNid);
      stringValue = refsetMember.getString1();

      if (refsetMember.getRevisionList() != null) {
         revisions = new RevisionSet<StringRevision, StringMember>(primordialStamp);

         for (TtkRefexStringRevision eVersion : refsetMember.getRevisionList()) {
            revisions.add(new StringRevision(eVersion, this));
         }
      }
   }

   //~--- methods -------------------------------------------------------------

   @Override
   protected void addRefsetTypeNids(Set<Integer> allNids) {

      //
   }

   @Override
   protected void addSpecProperties(RefexCAB rcs) {
      rcs.with(ComponentProperty.STRING_EXTENSION_1, getString1());
   }
   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }

      if (StringMember.class.isAssignableFrom(obj.getClass())) {
         StringMember another = (StringMember) obj;

         return this.nid == another.nid;
      }

      return false;
   }

   @Override
   public int hashCode() {
      return Hashcode.compute(new int[] { this.nid });
   }

   @Override
   public StringRevision makeAnalog() {
      StringRevision newR = new StringRevision(getStatus(), getTime(), getAuthorNid(), getModuleNid(), getPathNid(), this);
      return newR;
   }

   @Override
   public StringRevision makeAnalog(org.ihtsdo.otf.tcc.api.coordinate.Status status, long time, int authorNid, int moduleNid, int pathNid) {
      StringRevision newR = new StringRevision(status, time, authorNid, moduleNid, pathNid, this);

      addRevision(newR);

      return newR;
   }

   @Override
   protected boolean refexFieldsEqual(ConceptComponent<StringRevision, StringMember> obj) {
      if (StringMember.class.isAssignableFrom(obj.getClass())) {
         StringMember another = (StringMember) obj;

         return this.stringValue.equals(another.stringValue);
      }

      return false;
   }
   
   
   @Override
    public boolean refexFieldsEqual(RefexVersionBI another) {
        if(RefexStringVersionBI.class.isAssignableFrom(another.getClass())){
            RefexStringVersionBI sv = (RefexStringVersionBI) another;
            return this.stringValue.equals(sv.getString1());
        }
        return false;
    }

   @Override
   protected void readMemberFields(TupleInput input) {
      stringValue = input.readString();
   }

   @Override
   protected final StringRevision readMemberRevision(TupleInput input) {
      return new StringRevision(input, this);
   }

   @Override
   public boolean readyToWriteRefsetMember() {
      assert stringValue != null;

      return true;
   }

   /*
    *  (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();

      buf.append(this.getClass().getSimpleName()).append(": ");
      buf.append(" stringValue: '").append(this.stringValue).append("' ");
      buf.append(super.toString());

      return buf.toString();
   }

   @Override
   protected void writeMember(TupleOutput output) {
      output.writeString(stringValue);
   }

   //~--- get methods ---------------------------------------------------------

   @Override
   public String getString1() {
      return stringValue;
   }

   @Override
   protected RefexType getTkRefsetType() {
      return RefexType.STR;
   }

   @Override
   public int getTypeNid() {
      return RefexType.STR.getTypeToken();
   }

   @Override
   public IntArrayList getVariableVersionNids() {
      return new IntArrayList(2);
   }

   @Override
   protected VersionComputer<RefexMemberVersion<StringRevision, StringMember>> getVersionComputer() {
      return computer;
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<StringMemberVersion> getVersions() {
      if (versions == null) {
         int count = 1;

         if (revisions != null) {
            count = count + revisions.size();
         }

         ArrayList<StringMemberVersion> list = new ArrayList<>(count);

         if (getTime() != Long.MIN_VALUE) {
            list.add(new StringMemberVersion(this, this));
         }

         if (revisions != null) {
            for (StringRevision r : revisions) {
               if (r.getTime() != Long.MIN_VALUE) {
                  list.add(new StringMemberVersion(r, this));
               }
            }
         }

         versions = list;
      }

      return (List<StringMemberVersion>) versions;
   }

   //~--- set methods ---------------------------------------------------------

   @Override
   public void setString1(String str) {
      this.stringValue = str;
      modified();
   }

}
