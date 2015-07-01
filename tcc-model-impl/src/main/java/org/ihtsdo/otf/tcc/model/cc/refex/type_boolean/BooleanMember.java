package org.ihtsdo.otf.tcc.model.cc.refex.type_boolean;

//~--- non-JDK imports --------------------------------------------------------
import gov.vha.isaac.ochre.api.Get;
import org.ihtsdo.otf.tcc.api.blueprint.ComponentProperty;
import org.ihtsdo.otf.tcc.api.blueprint.RefexCAB;
import org.ihtsdo.otf.tcc.api.hash.Hashcode;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.refex.type_boolean.RefexBooleanAnalogBI;
import org.ihtsdo.otf.tcc.api.refex.type_boolean.RefexBooleanVersionBI;
import org.ihtsdo.otf.tcc.dto.component.refex.type_boolean.TtkRefexBooleanMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.refex.type_boolean.TtkRefexBooleanRevision;
import org.ihtsdo.otf.tcc.model.cc.component.ConceptComponent;
import org.ihtsdo.otf.tcc.model.cc.component.RevisionSet;
import org.ihtsdo.otf.tcc.model.version.VersionComputer;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMember;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMemberVersion;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BooleanMember extends RefexMember<BooleanRevision, BooleanMember>
        implements RefexBooleanAnalogBI<BooleanRevision> {

    private static VersionComputer<RefexMemberVersion<BooleanRevision, BooleanMember>> computer
            = new VersionComputer<>();
    //~--- fields --------------------------------------------------------------
    protected boolean booleanValue;

    //~--- constructors --------------------------------------------------------
    public BooleanMember() {
        super();
    }

    public BooleanMember(TtkRefexBooleanMemberChronicle refsetMember, int enclosingConceptNid) throws IOException {
        super(refsetMember, enclosingConceptNid);
        booleanValue = refsetMember.getBooleanValue();

        if (refsetMember.getRevisionList() != null) {
            revisions = new RevisionSet(primordialStamp);

            for (TtkRefexBooleanRevision eVersion : refsetMember.getRevisionList()) {
                revisions.add(new BooleanRevision(eVersion, this));
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
        rcs.with(ComponentProperty.BOOLEAN_EXTENSION_1, getBoolean1());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (BooleanMember.class.isAssignableFrom(obj.getClass())) {
            BooleanMember another = (BooleanMember) obj;

            return this.nid == another.nid;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Hashcode.compute(new int[]{nid});
    }

    @Override
    public BooleanRevision makeAnalog() {
        BooleanRevision newR = new BooleanRevision(getStatus(), getTime(),
                getAuthorNid(), getModuleNid(), getPathNid(), this);

        return newR;
    }

    @Override
    public BooleanRevision makeAnalog(org.ihtsdo.otf.tcc.api.coordinate.Status status, long time, int authorNid, int moduleNid, int pathNid) {
        BooleanRevision newR = new BooleanRevision(status, time,
                authorNid, moduleNid, pathNid, this);

        addRevision(newR);

        return newR;
    }

    @Override
    protected boolean refexFieldsEqual(ConceptComponent<BooleanRevision, BooleanMember> obj) {
        if (BooleanMember.class.isAssignableFrom(obj.getClass())) {
            BooleanMember another = (BooleanMember) obj;

            return this.booleanValue = another.booleanValue;
        }

        return false;
    }

    @Override
    public boolean refexFieldsEqual(RefexVersionBI another) {
        if (RefexBooleanVersionBI.class.isAssignableFrom(another.getClass())) {
            RefexBooleanVersionBI bv = (RefexBooleanVersionBI) another;
            return this.booleanValue = bv.getBoolean1();
        }
        return false;
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
        StringBuilder buf = new StringBuilder();

        buf.append(this.getClass().getSimpleName()).append(":{");
        buf.append(" booleanValue:").append(this.booleanValue);
        buf.append("; ");
        buf.append(super.toString());

        return buf.toString();
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public boolean getBoolean1() {
        return this.booleanValue;
    }

    @Override
    protected RefexType getTkRefsetType() {
        return RefexType.BOOLEAN;
    }

    @Override
    public int getTypeNid() {
        return RefexType.BOOLEAN.getTypeToken();
    }

    @Override
    protected VersionComputer<RefexMemberVersion<BooleanRevision, BooleanMember>> getVersionComputer() {
        return computer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BooleanMemberVersion> getVersions() {
        if (versions == null) {
            int count = 1;

            if (revisions != null) {
                count = count + revisions.size();
            }

            ArrayList<BooleanMemberVersion> list = new ArrayList<>(count);

            if (getTime() != Long.MIN_VALUE) {
                list.add(new BooleanMemberVersion(this, this, primordialStamp));
                for (int stampAlias : Get.commitService().getAliases(primordialStamp)) {
                    list.add(new BooleanMemberVersion(this, this, stampAlias));
                }
            }

            if (revisions != null) {
                for (BooleanRevision br : revisions) {
                    if (br.getTime() != Long.MIN_VALUE) {
                        list.add(new BooleanMemberVersion(br, this, br.stamp));
                        for (int stampAlias : Get.commitService().getAliases(br.stamp)) {
                            list.add(new BooleanMemberVersion(br, this, stampAlias));
                        }
                    }
                }
            }

            versions = list;
        }

        return (List<BooleanMemberVersion>) versions;
    }

    //~--- set methods ---------------------------------------------------------
    @Override
    public void setBoolean1(boolean l) throws PropertyVetoException {
        this.booleanValue = l;
        modified();
    }

}
