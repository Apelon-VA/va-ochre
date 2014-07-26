package org.ihtsdo.otf.tcc.model.cc.identifier;

//~--- non-JDK imports --------------------------------------------------------

import org.ihtsdo.otf.tcc.api.coordinate.Status;
import org.ihtsdo.otf.tcc.api.id.LongIdBI;
import org.ihtsdo.otf.tcc.dto.component.identifier.TtkIdentifierLong;
import org.ihtsdo.otf.tcc.model.cc.component.ConceptComponent.IDENTIFIER_PART_TYPES;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

public class IdentifierVersionLong extends IdentifierVersion implements LongIdBI {
   private long longDenotation;

   //~--- constructors --------------------------------------------------------

   public IdentifierVersionLong() {
      super();
   }

   public IdentifierVersionLong(TtkIdentifierLong idv) throws IOException {
      super(idv);
      longDenotation = idv.getDenotation();
   }

   public IdentifierVersionLong(DataInputStream input) throws IOException {
      super(input);
      longDenotation = input.readLong();
   }
   
   public IdentifierVersionLong(Status status, long time, int authorNid, int moduleNid,
           int pathNid, int authorityNid, long denotation) {
      super(status, time, authorNid, moduleNid, pathNid, authorityNid);
   }

   //~--- methods -------------------------------------------------------------

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }

      if (IdentifierVersionLong.class.isAssignableFrom(obj.getClass())) {
         IdentifierVersionLong another = (IdentifierVersionLong) obj;

         return this.getStamp() == another.getStamp();
      }

      return false;
   }

   @Override
   public int hashCode() {
      return super.hashCode();
   }

   @Override
   public final boolean readyToWriteIdentifier() {
      return true;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();

      buf.append(this.getClass().getSimpleName()).append(": ");
      buf.append("denotation:").append(this.longDenotation);
      buf.append(" ");
      buf.append(super.toString());

      return buf.toString();
   }

   @Override
   protected void writeSourceIdToBdb(DataOutput output) throws IOException {
      output.writeLong(longDenotation);
   }
   
   //~--- get methods ---------------------------------------------------------

   @Override
   public Long getDenotation() {
      return longDenotation;
   }

   @Override
   public IDENTIFIER_PART_TYPES getType() {
      return IDENTIFIER_PART_TYPES.LONG;
   }

   //~--- set methods ---------------------------------------------------------
}
