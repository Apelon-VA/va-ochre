package org.ihtsdo.otf.tcc.dto.component.refex.type_boolean;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.refex.type_boolean.RefexBooleanVersionBI;
import org.ihtsdo.otf.tcc.dto.component.TtkRevision;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;

public class TtkRefexBooleanRevision extends TtkRevision {
   public static final long serialVersionUID = 1;

   //~--- fields --------------------------------------------------------------

    @XmlAttribute
  public boolean booleanValue;

   //~--- constructors --------------------------------------------------------

   public TtkRefexBooleanRevision() {
      super();
   }

   public TtkRefexBooleanRevision(RefexBooleanVersionBI another) throws IOException {
      super(another);
      this.booleanValue = another.getBoolean1();
   }

   public TtkRefexBooleanRevision(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super();
      readExternal(in, dataVersion);
   }
   //~--- methods -------------------------------------------------------------
   @Override
   protected final void addUuidReferencesForRevisionComponent(Collection<UUID> references) {
       // nothing to add
   }
   /**
    * Compares this object to the specified object. The result is {@code true}
    * if and only if the argument is not {@code null}, is a
    * {@code ERefsetBooleanVersion} object, and contains the same values, field by field,
    * as this {@code ERefsetBooleanVersion}.
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

      if (TtkRefexBooleanRevision.class.isAssignableFrom(obj.getClass())) {
         TtkRefexBooleanRevision another = (TtkRefexBooleanRevision) obj;

         // =========================================================
         // Compare properties of 'this' class to the 'another' class
         // =========================================================
         // Compare booleanValue
         if (this.booleanValue != another.booleanValue) {
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
      booleanValue = in.readBoolean();
   }

   /**
    * Returns a string representation of the object.
    */
   @Override
   public String toString() {
      StringBuilder buff = new StringBuilder();

      buff.append(this.getClass().getSimpleName()).append(": ");
      buff.append(this.booleanValue);
      buff.append(" ");
      buff.append(super.toString());

      return buff.toString();
   }

   @Override
   public void writeExternal(DataOutput out) throws IOException {
      super.writeExternal(out);
      out.writeBoolean(booleanValue);
   }

   //~--- get methods ---------------------------------------------------------

   public boolean getBooleanValue() {
      return booleanValue;
   }

   public boolean isBooleanValue() {
      return booleanValue;
   }

   //~--- set methods ---------------------------------------------------------

   public void setBooleanValue(boolean booleanValue) {
      this.booleanValue = booleanValue;
   }
}
