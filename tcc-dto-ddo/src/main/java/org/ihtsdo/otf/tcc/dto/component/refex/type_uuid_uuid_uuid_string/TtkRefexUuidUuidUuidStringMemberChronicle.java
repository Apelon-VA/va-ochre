package org.ihtsdo.otf.tcc.dto.component.refex.type_uuid_uuid_uuid_string;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.store.TerminologyStoreDI;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.api.refex.RefexChronicleBI;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.refex.type_nid_nid_nid_string
   .RefexNidNidNidStringVersionBI;
import org.ihtsdo.otf.tcc.dto.UtfHelper;
import org.ihtsdo.otf.tcc.dto.component.refex.TtkRefexAbstractMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.transformer.ComponentFields;
import org.ihtsdo.otf.tcc.dto.component.transformer.ComponentTransformerBI;

import static org.ihtsdo.otf.tcc.dto.component.TtkRevision.informAboutUuid;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;

public class TtkRefexUuidUuidUuidStringMemberChronicle
        extends TtkRefexAbstractMemberChronicle<TtkRefexUuidUuidUuidStringRevision> {
   public static final long serialVersionUID = 1;
   @XmlAttribute
   public UUID              uuid1;
   @XmlAttribute
   public UUID              uuid2;
   @XmlAttribute
   public UUID              uuid3;
   @XmlAttribute
   public String            string1;

   public TtkRefexUuidUuidUuidStringMemberChronicle() {
      super();
   }

   public TtkRefexUuidUuidUuidStringMemberChronicle(RefexChronicleBI another)
           throws IOException {
      super((RefexVersionBI) another.getPrimordialVersion());

      TerminologyStoreDI                                  ts   = Ts.get();
      Collection<? extends RefexNidNidNidStringVersionBI> rels =
         another.getVersions();
      int                                               partCount = rels.size();
      Iterator<? extends RefexNidNidNidStringVersionBI> relItr    =
         rels.iterator();
      RefexNidNidNidStringVersionBI rv = relItr.next();

      this.uuid1   = ts.getUuidPrimordialForNid(rv.getNid1());
      this.uuid2   = ts.getUuidPrimordialForNid(rv.getNid2());
      this.uuid3   = ts.getUuidPrimordialForNid(rv.getNid3());
      this.string1 = rv.getString1();

      if (partCount > 1) {
         revisions = new ArrayList<>(partCount - 1);

         while (relItr.hasNext()) {
            rv = relItr.next();
            revisions.add(new TtkRefexUuidUuidUuidStringRevision(rv));
         }
      }
   }

   public TtkRefexUuidUuidUuidStringMemberChronicle(DataInput in, int dataVersion)
           throws IOException, ClassNotFoundException {
      super();
      readExternal(in, dataVersion);
   }

   public TtkRefexUuidUuidUuidStringMemberChronicle(
           TtkRefexUuidUuidUuidStringMemberChronicle another,
           ComponentTransformerBI transformer) {
      super(another, transformer);
      this.uuid1 =
         transformer.transform(another.uuid1, another,
                               ComponentFields.REFEX_COMPONENT_1_UUID);
      this.uuid2 =
         transformer.transform(another.uuid2, another,
                               ComponentFields.REFEX_COMPONENT_2_UUID);
      this.uuid3 =
         transformer.transform(another.uuid3, another,
                               ComponentFields.REFEX_COMPONENT_3_UUID);
      this.string1 = transformer.transform(another.string1, another,
              ComponentFields.REFEX_STRING1);
   }
    @Override
    protected void addUuidReferencesForRefexRevision(Collection<UUID> references) {
        references.add(uuid1);
        references.add(uuid2);
        references.add(uuid3);
    }
   /**
    * Compares this object to the specified object. The result is <tt>true</tt>
    * if and only if the argument is not <tt>null</tt>, is a
    * <tt>ERefsetCidCidStrMember</tt> object, and contains the same values, field by field,
    * as this <tt>ERefsetCidCidStrMember</tt>.
    *
    * @param obj the object to compare with.
    * @return <code>true</code> if the objects are the same;
    *         <code>false</code> otherwise.
    */
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }

      if (TtkRefexUuidUuidUuidStringMemberChronicle.class.isAssignableFrom(
              obj.getClass())) {
         TtkRefexUuidUuidUuidStringMemberChronicle another =
            (TtkRefexUuidUuidUuidStringMemberChronicle) obj;

         // =========================================================
         // Compare properties of 'this' class to the 'another' class
         // =========================================================
         // Compare c1Uuid
         if (!this.uuid1.equals(another.uuid1)) {
            return false;
         }

         // Compare c2Uuid
         if (!this.uuid2.equals(another.uuid2)) {
            return false;
         }

         if (!this.uuid3.equals(another.uuid3)) {
            return false;
         }

         // Compare strValue
         if (!this.string1.equals(another.string1)) {
            return false;
         }

         // Compare their parents
         return super.equals(obj);
      }

      return false;
   }

   /**
    * Returns a hash code for this <code>ERefsetCidCidStrMember</code>.
    *
    * @return a hash code value for this <tt>ERefsetCidCidStrMember</tt>.
    */
   @Override
   public int hashCode() {
      return this.primordialUuid.hashCode();
   }

   @Override
   public TtkRefexUuidUuidUuidStringMemberChronicle makeTransform(
           ComponentTransformerBI transformer) {
      return new TtkRefexUuidUuidUuidStringMemberChronicle(this, transformer);
   }

   @Override
   public final void readExternal(DataInput in, int dataVersion)
           throws IOException, ClassNotFoundException {
      super.readExternal(in, dataVersion);
      uuid1   = new UUID(in.readLong(), in.readLong());
      uuid2   = new UUID(in.readLong(), in.readLong());
      uuid3   = new UUID(in.readLong(), in.readLong());
      string1 = UtfHelper.readUtfV7(in, dataVersion);

      int versionSize = in.readInt();

      if (versionSize > 0) {
         revisions = new ArrayList<>(versionSize);

         for (int i = 0; i < versionSize; i++) {
            revisions.add(new TtkRefexUuidUuidUuidStringRevision(in,
                    dataVersion));
         }
      }
   }

   /**
    * Returns a string representation of the object.
    */
   @Override
   public String toString() {
      StringBuilder buff = new StringBuilder();

      buff.append(this.getClass().getSimpleName()).append(": ");
      buff.append(" c1:");
      buff.append(informAboutUuid(this.uuid1));
      buff.append(" c2:");
      buff.append(informAboutUuid(this.uuid2));
      buff.append(" c3:");
      buff.append(informAboutUuid(this.uuid3));
      buff.append(" str:");
      buff.append("'").append(this.string1).append("'");
      buff.append(" ");
      buff.append(super.toString());

      return buff.toString();
   }

   @Override
   public void writeExternal(DataOutput out) throws IOException {
      super.writeExternal(out);
      out.writeLong(uuid1.getMostSignificantBits());
      out.writeLong(uuid1.getLeastSignificantBits());
      out.writeLong(uuid2.getMostSignificantBits());
      out.writeLong(uuid2.getLeastSignificantBits());
      out.writeLong(uuid3.getMostSignificantBits());
      out.writeLong(uuid3.getLeastSignificantBits());
      UtfHelper.writeUtf(out, string1);

      if (revisions == null) {
         out.writeInt(0);
      } else {
         out.writeInt(revisions.size());

         for (TtkRefexUuidUuidUuidStringRevision rmv : revisions) {
            rmv.writeExternal(out);
         }
      }
   }

   public List<TtkRefexUuidUuidUuidStringRevision> getRevisionList() {
      return revisions;
   }

   public String getString1() {
      return string1;
   }

   @Override
   public RefexType getType() {
      return RefexType.CID_CID_CID_STRING;
   }

   public UUID getUuid1() {
      return uuid1;
   }

   public UUID getUuid2() {
      return uuid2;
   }

   public UUID getUuid3() {
      return uuid3;
   }

   public void setString1(String string1) {
      this.string1 = string1;
   }

   public void setUuid1(UUID uuid1) {
      this.uuid1 = uuid1;
   }

   public void setUuid2(UUID uuid2) {
      this.uuid2 = uuid2;
   }

   public void setUuid3(UUID uuid3) {
      this.uuid3 = uuid3;
   }
}
