package org.ihtsdo.otf.tcc.dto.component.refex.type_uuid_boolean;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.store.TerminologyStoreDI;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.api.refex.type_nid_boolean.RefexNidBooleanVersionBI;
import org.ihtsdo.otf.tcc.dto.component.TtkRevision;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Collection;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 13/03/27
 * @author         Enter your name here...    
 */
public class TtkRefexUuidBooleanRevision extends TtkRevision {

   /** Field description */
   public static final long serialVersionUID = 1;

   /** Field description */
   @XmlAttribute
   public UUID uuid1;

   /** Field description */
   @XmlAttribute
   public boolean boolean1;

   /**
    * Constructs ...
    *
    */
   public TtkRefexUuidBooleanRevision() {
      super();
   }

   /**
    * Constructs ...
    *
    *
    * @param another
    *
    * @throws IOException
    */
   public TtkRefexUuidBooleanRevision(RefexNidBooleanVersionBI another) throws IOException {
      super(another);

      TerminologyStoreDI ts = Ts.get();

      this.uuid1    = ts.getUuidPrimordialForNid(another.getNid1());
      this.boolean1 = another.getBoolean1();
   }

   /**
    * Constructs ...
    *
    *
    * @param in
    * @param dataVersion
    *
    * @throws ClassNotFoundException
    * @throws IOException
    */
   public TtkRefexUuidBooleanRevision(DataInput in, int dataVersion)
           throws IOException, ClassNotFoundException {
      super();
      readExternal(in, dataVersion);
   }

   @Override
    protected final void addUuidReferencesForRevisionComponent(Collection<UUID> references) {
        // nothing to add
    }
   /**
    * Compares this object to the specified object. The result is {@code true}
    * if and only if the argument is not {@code null}, is a
    * {@code ERefsetCidStrVersion} object, and contains the same values, field by field,
    * as this {@code ERefsetCidStrVersion}.
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

      if (TtkRefexUuidBooleanRevision.class.isAssignableFrom(obj.getClass())) {
         TtkRefexUuidBooleanRevision another = (TtkRefexUuidBooleanRevision) obj;

         // =========================================================
         // Compare properties of 'this' class to the 'another' class
         // =========================================================
         // Compare c1Uuid
         if (!this.uuid1.equals(another.uuid1)) {
            return false;
         }

         // Compare strValue
         if (this.boolean1 != another.boolean1) {
            return false;
         }

         // Compare their parents
         return super.equals(obj);
      }

      return false;
   }

   /**
    * Method description
    *
    *
    * @param in
    * @param dataVersion
    *
    * @throws ClassNotFoundException
    * @throws IOException
    */
   @Override
   public final void readExternal(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super.readExternal(in, dataVersion);
      uuid1    = new UUID(in.readLong(), in.readLong());
      boolean1 = in.readBoolean();
   }

   /**
    * Returns a string representation of the object.
    *
    * @return
    */
   @Override
   public String toString() {
      StringBuilder buff = new StringBuilder();

      buff.append(this.getClass().getSimpleName()).append(": ");
      buff.append(" c1: ");
      buff.append(informAboutUuid(this.uuid1));
      buff.append(" str: ");
      buff.append("'").append(this.boolean1).append("'");
      buff.append(" ");
      buff.append(super.toString());

      return buff.toString();
   }

   /**
    * Method description
    *
    *
    * @param out
    *
    * @throws IOException
    */
   @Override
   public void writeExternal(DataOutput out) throws IOException {
      super.writeExternal(out);
      out.writeLong(uuid1.getMostSignificantBits());
      out.writeLong(uuid1.getLeastSignificantBits());
      out.writeBoolean(boolean1);
   }

   /**
    * Method description
    *
    *
    * @return
    */
   public boolean getBoolean1() {
      return boolean1;
   }

   /**
    * Method description
    *
    *
    * @return
    */
   public UUID getUuid1() {
      return uuid1;
   }

   /**
    * Method description
    *
    *
    * @param boolean1
    */
   public void setBoolean1(boolean boolean1) {
      this.boolean1 = boolean1;
   }

   /**
    * Method description
    *
    *
    * @param uuid1
    */
   public void setUuid1(UUID uuid1) {
      this.uuid1 = uuid1;
   }
}
