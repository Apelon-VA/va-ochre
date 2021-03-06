package org.ihtsdo.otf.tcc.dto.component.refex;

//~--- non-JDK imports --------------------------------------------------------

import gov.vha.isaac.ochre.api.Get;
import gov.vha.isaac.ochre.api.chronicle.StampedVersion;
import gov.vha.isaac.ochre.model.sememe.SememeChronologyImpl;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.dto.component.TtkComponentChronicle;
import org.ihtsdo.otf.tcc.dto.component.TtkRevision;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;

public abstract class TtkRefexAbstractMemberChronicle<R extends TtkRevision> 
    extends TtkComponentChronicle<R, StampedVersion> {
   public static final long serialVersionUID = 1;

   //~--- fields --------------------------------------------------------------

   @XmlAttribute
   public UUID referencedComponentUuid;
   @XmlAttribute
   public UUID assemblageUuid;

   //~--- constructors --------------------------------------------------------

   public TtkRefexAbstractMemberChronicle() {
      super();
   }

   public TtkRefexAbstractMemberChronicle(RefexVersionBI another) throws IOException {
      super(another);
      Optional<UUID> optionalPrimordialUuid = Get.identifierService().getUuidPrimordialForNid(another.getReferencedComponentNid());
      if (optionalPrimordialUuid.isPresent()) {
          this.referencedComponentUuid = optionalPrimordialUuid.get();
      } else {
          throw new NoSuchElementException("No object for referenced component: " + another.getReferencedComponentNid());
      }
      
      optionalPrimordialUuid = Get.identifierService().getUuidPrimordialForNid(another.getAssemblageNid());
      if (optionalPrimordialUuid.isPresent()) {
          this.assemblageUuid = optionalPrimordialUuid.get();
      } else {
          throw new NoSuchElementException("No object for assemblage: " + another.getAssemblageNid());
      }
   }

   public TtkRefexAbstractMemberChronicle(SememeChronologyImpl<?> another) {
      super(another);
      this.referencedComponentUuid = another.getPrimordialUuid();
      this.assemblageUuid = Get.identifierService().getUuidPrimordialFromConceptSequence(another.getAssemblageSequence()).get();
   }

   public TtkRefexAbstractMemberChronicle(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super();
      readExternal(in, dataVersion);
   }

   //~--- methods -------------------------------------------------------------

   /**
    * Compares this object to the specified object. The result is {@code true}
    * if and only if the argument is not {@code null}, is a
    * {@code ERefset} object, and contains the same values, field by field,
    * as this {@code ERefset}.
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

      if (TtkRefexAbstractMemberChronicle.class.isAssignableFrom(obj.getClass())) {
         TtkRefexAbstractMemberChronicle<?> another = (TtkRefexAbstractMemberChronicle<?>) obj;

         // =========================================================
         // Compare properties of 'this' class to the 'another' class
         // =========================================================
         // Compare refsetUuid
         if (!this.assemblageUuid.equals(another.assemblageUuid)) {
            return false;
         }

         // Compare referencedComponentUuid
         if (!this.referencedComponentUuid.equals(another.referencedComponentUuid)) {
            return false;
         }

         // Compare their parents
         return super.equals(obj);
      }

      return false;
   }

   /**
    * Returns a hash code for this {@code ERefset}.
    *
    * @return a hash code value for this {@code ERefset}.
    */
   @Override
   public int hashCode() {
      return this.primordialUuid.hashCode();
   }

   @Override
   public void readExternal(DataInput in, int dataVersion) throws IOException, ClassNotFoundException {
      super.readExternal(in, dataVersion);
      assemblageUuid = new UUID(in.readLong(), in.readLong());
      referencedComponentUuid = new UUID(in.readLong(), in.readLong());
   }

   /**
    * Returns a string representation of the object.
    */
   @Override
   public String toString() {
      StringBuilder buff = new StringBuilder();

      buff.append(" assemblage:");
      buff.append(informAboutUuid(this.assemblageUuid));
      buff.append(" referenced component:");
      buff.append(informAboutUuid(this.referencedComponentUuid));
      buff.append(" ");
      buff.append(super.toString());

      return buff.toString();
   }

   @Override
   public void writeExternal(DataOutput out) throws IOException {
      super.writeExternal(out);
      out.writeLong(assemblageUuid.getMostSignificantBits());
      out.writeLong(assemblageUuid.getLeastSignificantBits());
      out.writeLong(referencedComponentUuid.getMostSignificantBits());
      out.writeLong(referencedComponentUuid.getLeastSignificantBits());
   }

   //~--- get methods ---------------------------------------------------------
   @Override
   protected final void addUuidReferencesForRevisionComponent(Collection<UUID> references) {
       references.add(this.assemblageUuid);
       references.add(this.referencedComponentUuid);
       addUuidReferencesForRefexRevision(references);
   }
    protected abstract void addUuidReferencesForRefexRevision(Collection<UUID> references);

   public UUID getReferencedComponentUuid() {
      return referencedComponentUuid;
   }

   public UUID getAssemblageUuid() {
      return assemblageUuid;
   }

   public abstract RefexType getType();

   //~--- set methods ---------------------------------------------------------

   public void setReferencedComponentUuid(UUID referencedComponentUuid) {
      this.referencedComponentUuid = referencedComponentUuid;
   }

   public void setAssemblageUuid(UUID assemblageUuid) {
      this.assemblageUuid = assemblageUuid;
   }
}
