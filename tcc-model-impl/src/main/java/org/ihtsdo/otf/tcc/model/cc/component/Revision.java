package org.ihtsdo.otf.tcc.model.cc.component;

//~--- non-JDK imports --------------------------------------------------------

import gov.vha.isaac.ochre.api.Get;
import gov.vha.isaac.ochre.api.LookupService;
import gov.vha.isaac.ochre.api.IdentifierService;
import gov.vha.isaac.ochre.api.State;
import gov.vha.isaac.ochre.api.commit.CommitStates;
import gov.vha.isaac.ochre.api.component.sememe.SememeChronology;
import gov.vha.isaac.ochre.api.component.sememe.version.SememeVersion;
import gov.vha.isaac.ochre.api.coordinate.StampCoordinate;
import gov.vha.isaac.ochre.api.snapshot.calculator.RelativePositionCalculator;
import gov.vha.isaac.ochre.collections.StampSequenceSet;
import org.ihtsdo.otf.tcc.api.AnalogBI;
import org.ihtsdo.otf.tcc.api.AnalogGeneratorBI;
import org.ihtsdo.otf.tcc.api.chronicle.ComponentChronicleBI;
import org.ihtsdo.otf.tcc.api.chronicle.ComponentVersionBI;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.coordinate.EditCoordinate;
import org.ihtsdo.otf.tcc.api.coordinate.Position;
import org.ihtsdo.otf.tcc.api.coordinate.Status;
import org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate;
import org.ihtsdo.otf.tcc.api.hash.Hashcode;
import org.ihtsdo.otf.tcc.api.id.IdBI;
import org.ihtsdo.otf.tcc.api.refex.RefexChronicleBI;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.store.TerminologySnapshotDI;
import org.ihtsdo.otf.tcc.api.time.TimeHelper;
import org.ihtsdo.otf.tcc.model.cc.PersistentStore;

import java.beans.PropertyVetoException;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public abstract class Revision<V extends Revision<V, C>, C extends ConceptComponent<V, C>>
        implements ComponentVersionBI, AnalogBI, AnalogGeneratorBI<V> {

    protected static final Logger logger = Logger.getLogger(ConceptComponent.class.getName());
    public static SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
    //~--- fields --------------------------------------------------------------
    public C primordialComponent;
    public int stamp;
    

    //~--- constructors --------------------------------------------------------
    public Revision() {
        super();
    }

    public Revision(int stamp, C primordialComponent) {
        super();
        assert primordialComponent != null;
        assert stamp != 0;
        this.stamp = stamp;
        this.primordialComponent = primordialComponent;
        primordialComponent.clearVersions();
        assert stamp != Integer.MAX_VALUE;
//        this.primordialComponent.getEnclosingConcept().modified(); //TODO-AKF-KEC: modified
    }

    public Revision(DataInputStream input, C conceptComponent) throws IOException {
        this(input.readInt(), conceptComponent);
        conceptComponent.clearVersions();
        assert stamp != 0;
    }

    public Revision(Status status, long time, int authorNid, int moduleNid, int pathNid,
            C primordialComponent) {
        this.stamp = PersistentStore.get().getStamp(status, time, authorNid, moduleNid, pathNid);
        assert stamp != 0;
        assert primordialComponent != null;
        assert stamp != Integer.MAX_VALUE;
        this.primordialComponent = primordialComponent;
        primordialComponent.clearVersions();
//TODO-AKF-KEC        this.primordialComponent.getEnclosingConcept().modified();
    }

    //~--- methods -------------------------------------------------------------
    
    public boolean isLatestVersionActive(StampCoordinate coordinate) {
        RelativePositionCalculator calc = RelativePositionCalculator.getCalculator(coordinate);
        StampSequenceSet latestStampSequences = calc.getLatestStampSequencesAsSet(this.getVersionStampSequences());
        return !latestStampSequences.isEmpty();
    }
    public IntStream getVersionStampSequences() {
        return this.primordialComponent.getVersionStampSequences();
    }
    
    
    @Override
    public boolean addAnnotation(@SuppressWarnings("rawtypes") RefexChronicleBI annotation)
            throws IOException {
        return primordialComponent.addAnnotation(annotation);
    }

    abstract protected void addComponentNids(Set<Integer> allNids);

    protected String assertionString() {
        try {
            return PersistentStore.get().getConcept(primordialComponent.enclosingConceptNid).toLongString();
        } catch (IOException ex) {
            Logger.getLogger(ConceptComponent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return toString();
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (Revision.class.isAssignableFrom(obj.getClass())) {
            Revision<V, C> another = (Revision<V, C>) obj;

            if (this.stamp == another.stamp) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final int hashCode() {
        if(primordialComponent == null){
            System.out.println(primordialComponent.getClass());
        }
        return Hashcode.compute(primordialComponent.nid);
    }

    public boolean makeAdjudicationAnalogs(EditCoordinate ec, ViewCoordinate vc) throws IOException {
        return primordialComponent.makeAdjudicationAnalogs(ec, vc);
    }

    /**
     * 1. Analog, an object, concept or situation which in some way resembles a
     * different situation 2. Analogy, in language, a comparison between
     * concepts
     *
     * @param status
     * @param pathNid
     * @param time
     * @return
     */
    @Override
    public abstract V makeAnalog(org.ihtsdo.otf.tcc.api.coordinate.Status status, long time, int authorNid, int moduleNid, int pathNid);

    protected void modified() {
        if (primordialComponent != null) {
            primordialComponent.modified();
        }
    }

    public final boolean readyToWrite() {
        assert primordialComponent != null : assertionString();
        assert stamp != Integer.MAX_VALUE : assertionString();
        assert (stamp > 0) || (stamp == -1);

        return true;
    }

    public abstract boolean readyToWriteRevision();

    @Override
    public boolean stampIsInRange(int min, int max) {
        return (stamp >= min) && (stamp <= max);
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append(" stamp:");
        buf.append(stamp);

        try {
            buf.append(" s:").append(getStatus());
            buf.append(" t: ");
            long time = getTime();
            if (time == Long.MAX_VALUE) {
                buf.append("uncommitted");
            } else if (time == Long.MIN_VALUE) {
                buf.append("canceled");
            } else {
                buf.append(TimeHelper.formatDate(time));
            }
            
            buf.append(" a:");
            ConceptComponent.addNidToBuffer(buf, getAuthorNid());
            buf.append(" m:");
            ConceptComponent.addNidToBuffer(buf, getModuleNid());
            buf.append(" p:");
            ConceptComponent.addNidToBuffer(buf, getPathNid());
            buf.append(" ms:");
            buf.append(time);
        } catch (Throwable e) {
            buf.append(" !!! Invalid stamp. !!! ");
            buf.append(e.getLocalizedMessage());
        }

        buf.append(" };");

        return buf.toString();
    }
    
    public String toSimpleString(){
        StringBuilder buf = new StringBuilder();
        buf.append("- stamp: ").append(stamp);
        buf.append("- primordial component nid: ").append(primordialComponent.nid);
        buf.append("- primordial component stamp: ").append(primordialComponent.primordialStamp);
        return buf.toString();
    }

    @Override
    public abstract String toUserString();

    @Override
    public String toUserString(TerminologySnapshotDI snapshot) throws IOException, ContradictionException {
        return toUserString();
    }

    /**
     * Test method to check to see if two objects are equal in all respects.
     *
     * @param another
     * @return either a zero length String, or a String containing a description
     * of the validation failures.
     * @throws IOException
     */
    public String validate(Revision<?, ?> another) throws IOException {
        assert another != null;

        StringBuilder buf = new StringBuilder();

        if (this.stamp != another.stamp) {
            buf.append("\t\tRevision.sapNid not equal: \n\t\t\tthis.sapNid = ").append(this.stamp).append(
                    "\n\t\t\tanother.sapNid = ").append(another.stamp).append("\n");
        }

        if (!this.primordialComponent.equals(another.primordialComponent)) {
            buf.append(
                    "\t\tRevision.primordialComponent not equal: " + "\n\t\t\tthis.primordialComponent = ").append(
                    this.primordialComponent).append("\n\t\t\tanother.primordialComponent = ").append(
                    another.primordialComponent).append("\n");
        }

        return buf.toString();
    }

    @Override
    public boolean versionsEqual(ViewCoordinate vc1, ViewCoordinate vc2, Boolean compareAuthoring) {
        return primordialComponent.versionsEqual(vc1, vc2, compareAuthoring);
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public Collection<? extends IdBI> getAdditionalIds() {
        return primordialComponent.getAdditionalIds();
    }

    @Override
    public Collection<? extends IdBI> getAllIds() {
        return primordialComponent.getAllIds();
    }

    @Override
    public Set<Integer> getAllNidsForVersion() throws IOException {
        HashSet<Integer> allNids = new HashSet<>();

        allNids.add(primordialComponent.nid);
        allNids.add(getAuthorNid());
        allNids.add(getPathNid());
        addComponentNids(allNids);

        return allNids;
    }

    public Set<Integer> getAllStamps() throws IOException {
        return primordialComponent.getAllStamps();
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getAnnotations() {
        return primordialComponent.getAnnotations();
    }

    @Override
    public int getAuthorNid() {
        return PersistentStore.get().getAuthorNidForStamp(stamp);
    }

    @Override
    public ComponentChronicleBI getChronicle() {
        return (ComponentChronicleBI) primordialComponent;
    }

    public int getConceptNid() {
        return primordialComponent.enclosingConceptNid;
    }
    public int getEnclosingConceptNid() {
       return primordialComponent.enclosingConceptNid;
    }
    @Override
    public int getAssociatedConceptNid() {
       return getConceptNid();
    }
    @Override
    public Collection<? extends RefexVersionBI<?>> getAnnotationsActive(ViewCoordinate xyz)
            throws IOException {
        return primordialComponent.getAnnotationsActive(xyz);
    }

    @Override
    public <T extends RefexVersionBI<?>> Collection<T> getAnnotationsActive(ViewCoordinate xyz,
            Class<T> cls)
            throws IOException {
        return primordialComponent.getAnnotationsActive(xyz, cls);
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getAnnotationsActive(ViewCoordinate xyz,
            int refexNid)
            throws IOException {
        return primordialComponent.getAnnotationsActive(xyz, refexNid);
    }

    @Override
    public <T extends RefexVersionBI<?>> Collection<T> getAnnotationsActive(ViewCoordinate xyz,
            int refexNid, Class<T> cls)
            throws IOException {
        return primordialComponent.getAnnotationsActive(xyz, refexNid, cls);
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersActive(ViewCoordinate xyz, int refsetNid)
            throws IOException {
        return primordialComponent.getRefexMembersActive(xyz, refsetNid);
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersActive(ViewCoordinate xyz) throws IOException {
        return primordialComponent.getRefexMembersActive(xyz);
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersInactive(ViewCoordinate xyz) throws IOException {
        return getChronicle().getRefexMembersInactive(xyz);
    }

    @Override
    public int getModuleNid() {
        return PersistentStore.get().getModuleNidForStamp(stamp);
    }

    @Override
    public final int getNid() {
        return primordialComponent.getNid();
    }

    @Override
    public int getPathNid() {
        return PersistentStore.get().getPathNidForStamp(stamp);
    }

    @Override
    public Position getPosition() throws IOException {
        return new Position(getTime(), PersistentStore.get().getPath(getPathNid()));
    }

    public Set<Position> getPositions() throws IOException {
        return primordialComponent.getPositions();
    }

    @Override
    public UUID getPrimordialUuid() {
        return primordialComponent.getPrimordialUuid();
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getRefexMembers(int refsetNid) throws IOException {
        return primordialComponent.getRefexMembers(refsetNid);
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getRefexes() throws IOException {
        return primordialComponent.getRefexes();
    }
    
    @Override
    public int getStamp() {
        return stamp;
    }

    public final int getStatusAtPositionNid() {
        return stamp;
    }

    @Override
    public Status getStatus() {
        return PersistentStore.get().getStatusForStamp(stamp);
    }

    @Override
    public long getTime() {
        return PersistentStore.get().getTimeForStamp(stamp);
    }

    @Override
    public List<UUID> getUuidList() {
        return primordialComponent.getUuidList();
    }


    public final C getVersioned() {
        return primordialComponent;
    }

    @Override
    public boolean hasCurrentAnnotationMember(ViewCoordinate xyz, int refsetNid) throws IOException {
        return primordialComponent.hasCurrentAnnotationMember(xyz, refsetNid);
    }

    @Override
    public boolean hasCurrentRefexMember(ViewCoordinate xyz, int refsetNid) throws IOException {
        return primordialComponent.hasCurrentRefexMember(xyz, refsetNid);
    }

    public boolean isUncommitted() {
        return getTime() == Long.MAX_VALUE;
    }

    @Override
    public CommitStates getCommitState() {
        if (isUncommitted()) {
            return CommitStates.UNCOMMITTED;
        }
        return CommitStates.COMMITTED;
    }

    
    @Override
    public boolean isActive() throws IOException {
        return getStatus() == Status.ACTIVE;
    }

    //~--- set methods ---------------------------------------------------------
    @Override
    public void setAuthorNid(int authorNid) {
//        TODO-AKF-KEC: do we want to keep this check?
//        if (getTime() != Long.MAX_VALUE) {
//            throw new UnsupportedOperationException("Cannot change status if time != Long.MAX_VALUE; "
//                    + "Use makeAnalog instead.");
//        }

        if (authorNid != getPathNid()) {
            this.stamp = PersistentStore.get().getStamp(getStatus(), Long.MAX_VALUE, authorNid, getModuleNid(),
                    getPathNid());
            modified();
        }
    }

    @Override
    public final void setModuleNid(int moduleNid) {
        //        TODO-AKF-KEC: do we want to keep this check?
//        if (getTime() != Long.MAX_VALUE) {
//            throw new UnsupportedOperationException("Cannot change status if time != Long.MAX_VALUE; "
//                    + "Use makeAnalog instead.");
//        }

        try {
            this.stamp = PersistentStore.get().getStamp(getStatus(), Long.MAX_VALUE, getAuthorNid(), moduleNid,
                    getPathNid());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        modified();
    }

    @Override
    public final void setNid(int nid) throws PropertyVetoException {
        throw new PropertyVetoException("nid", null);
    }

    @Override
    public final void setPathNid(int pathId) {
        //        TODO-AKF-KEC: do we want to keep this check?
//        if (getTime() != Long.MAX_VALUE) {
//            throw new UnsupportedOperationException("Cannot change status if time != Long.MAX_VALUE; "
//                    + "Use makeAnalog instead.");
//        }

        this.stamp = PersistentStore.get().getStamp(getStatus(), Long.MAX_VALUE, getAuthorNid(), getModuleNid(), pathId);
    }

    public void setStatusAtPosition(Status status, long time, int authorNid, int moduleNid, int pathNid) {
        this.stamp = PersistentStore.get().getStamp(status, time, authorNid, moduleNid, pathNid);
        modified();
    }

    @Override
    public final void setStatus(org.ihtsdo.otf.tcc.api.coordinate.Status nid) {
//        TODO-AKF-KEC: do we want to keep this check?
//        if (getTime() != Long.MAX_VALUE) {
//            throw new UnsupportedOperationException("Cannot change status if time != Long.MAX_VALUE; "
//                    + "Use makeAnalog instead.");
//        }

        try {
            this.stamp = PersistentStore.get().getStamp(nid, Long.MAX_VALUE, getAuthorNid(), getModuleNid(),
                    getPathNid());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        modified();
    }

    @Override
    public final void setTime(long time) {
//        
//        if (getTime() != Long.MAX_VALUE) {
//            throw new UnsupportedOperationException("Cannot change status if time != Long.MAX_VALUE; "
//                    + "Use makeAnalog instead.");
//        }

        if (time != getTime()) {
            try {
                Status status = getStatus();
                int authorNid = getAuthorNid(); //HERE
                int moduleNid = getModuleNid();
                int pathNid = getPathNid();
                this.stamp = PersistentStore.get().getStamp(status, time, authorNid, moduleNid,
                        pathNid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            modified();
        }
    }

    public boolean isIndexed() {
        return primordialComponent.isIndexed();
    }

    public void setIndexed() {
        primordialComponent.setIndexed();
    }

    @Override
    public int getStampSequence() {
        return getStamp();
    }

    @Override
    public State getState() {
        return getStatus().getState();
    }

    @Override
    public int getAuthorSequence() {
        return Get.identifierService().getConceptSequence(getAuthorNid());
    }

    @Override
    public int getModuleSequence() {
        return Get.identifierService().getConceptSequence(getModuleNid());
    }

    @Override
    public int getPathSequence() {
       return Get.identifierService().getConceptSequence(getPathNid());
    }

    public List<SememeChronology<? extends SememeVersion<?>>> getSememeList() {
        return primordialComponent.getSememeList();
    }
    public List<SememeChronology<? extends SememeVersion<?>>> getSememeListFromAssemblage(int assemblageSequence) {
        return primordialComponent.getSememeListFromAssemblage(assemblageSequence);
    }
    public <SV extends SememeVersion> List<SememeChronology<SV>> getSememeListFromAssemblageOfType(int assemblageSequence, Class<SV> type) {
        return primordialComponent.getSememeListFromAssemblageOfType(assemblageSequence, type);
    }
    
}
