package org.ihtsdo.otf.tcc.dto.component.refex.type_uuid_uuid;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.api.store.TerminologyStoreDI;
import org.ihtsdo.otf.tcc.api.refex.type_nid_nid.RefexNidNidVersionBI;
import org.ihtsdo.otf.tcc.dto.component.TtkRevision;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Collection;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;

public class TtkRefexUuidUuidRevision extends TtkRevision {
   public static final long serialVersionUID = 1;

   //~--- fields --------------------------------------------------------------

   @XmlAttribute
   public UUID uuid1;
   @XmlAttribute
   public UUID uuid2;

   //~--- constructors --------------------------------------------------------

   public TtkRefexUuidUuidRevision() {
      super();
   }

   public TtkRefexUuidUuidRevision(RefexNidNidVersionBI another) throws IOException {
      super(another);

      TerminologyStoreDI ts = Ts.get();

      this.uuid1 = ts.getUuidPrimordialForNid(another.getNid1());
      this.uuid2 = ts.getUuidPrimordialForNid(another.getNid2());
   }

   public TtkRefexUuidUuidRevision(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super();
      readExternal(in, dataVersion);
   }

   //~--- methods -------------------------------------------------------------
   @Override
   protected final void addUuidReferencesForRevisionComponent(Collection<UUID> references) {
       references.add(uuid1);
       references.add(uuid2);
   }

   /**
    * Compares this object to the specified object. The result is {@code true}
    * if and only if the argument is not {@code null}, is a
    * {@code ERefsetCidCidVersion} object, and contains the same values, field by field,
    * as this {@code ERefsetCidCidVersion}.
    *
    * @param obj the object to compare with.
    * @return {@code true} if the objects are the same;
    *         {@code false} otherwise.
    */
   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }

      if (TtkRefexUuidUuidRevision.class.isAssignableFrom(obj.getClass())) {
         TtkRefexUuidUuidRevision another = (TtkRefexUuidUuidRevision) obj;

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

         // Compare their parents
         return super.equals(obj);
      }

      return false;
   }

   @Override
   public void readExternal(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super.readExternal(in, dataVersion);
      uuid1 = new UUID(in.readLong(), in.readLong());
      uuid2 = new UUID(in.readLong(), in.readLong());
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
   }

   //~--- get methods ---------------------------------------------------------

   public UUID getUuid1() {
      return uuid1;
   }

   public UUID getUuid2() {
      return uuid2;
   }

   //~--- set methods ---------------------------------------------------------

   public void setUuid1(UUID uuid1) {
      this.uuid1 = uuid1;
   }

   public void setUuid2(UUID uuid2) {
      this.uuid2 = uuid2;
   }
}
