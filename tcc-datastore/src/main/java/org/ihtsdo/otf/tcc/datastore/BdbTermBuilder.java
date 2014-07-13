package org.ihtsdo.otf.tcc.datastore;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.ihtsdo.otf.tcc.api.chronicle.ComponentChronicleBI;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.blueprint.ConceptAttributeAB;
import org.ihtsdo.otf.tcc.api.blueprint.DescriptionCAB;
import org.ihtsdo.otf.tcc.api.blueprint.InvalidCAB;
import org.ihtsdo.otf.tcc.api.blueprint.RefexCAB;
import org.ihtsdo.otf.tcc.api.blueprint.ComponentProperty;
import org.ihtsdo.otf.tcc.api.blueprint.RefexDynamicCAB;
import org.ihtsdo.otf.tcc.api.blueprint.RelationshipCAB;
import org.ihtsdo.otf.tcc.api.blueprint.TerminologyBuilderBI;
import org.ihtsdo.otf.tcc.api.blueprint.ConceptCB;
import org.ihtsdo.otf.tcc.api.blueprint.MediaCAB;
import org.ihtsdo.otf.tcc.api.conattr.ConceptAttributeChronicleBI;
import org.ihtsdo.otf.tcc.api.conattr.ConceptAttributeVersionBI;
import org.ihtsdo.otf.tcc.api.concept.ConceptChronicleBI;
import org.ihtsdo.otf.tcc.api.coordinate.EditCoordinate;
import org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate;
import org.ihtsdo.otf.tcc.api.description.DescriptionChronicleBI;
import org.ihtsdo.otf.tcc.api.description.DescriptionVersionBI;
import org.ihtsdo.otf.tcc.api.media.MediaChronicleBI;
import org.ihtsdo.otf.tcc.api.media.MediaVersionBI;
import org.ihtsdo.otf.tcc.api.refex.RefexChronicleBI;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.relationship.RelationshipChronicleBI;
import org.ihtsdo.otf.tcc.api.relationship.RelationshipVersionBI;
import org.ihtsdo.otf.tcc.api.refex.RefexType;
import org.ihtsdo.otf.tcc.api.refexDynamic.RefexDynamicChronicleBI;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.model.cc.P;
import org.ihtsdo.otf.tcc.model.cc.attributes.ConceptAttributes;
import org.ihtsdo.otf.tcc.model.cc.attributes.ConceptAttributesRevision;
import org.ihtsdo.otf.tcc.model.cc.component.RevisionSet;
import org.ihtsdo.otf.tcc.model.cc.concept.ConceptChronicle;
import org.ihtsdo.otf.tcc.model.cc.description.Description;
import org.ihtsdo.otf.tcc.model.cc.description.DescriptionRevision;
import org.ihtsdo.otf.tcc.model.cc.media.Media;
import org.ihtsdo.otf.tcc.model.cc.media.MediaRevision;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMember;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMemberFactory;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexRevision;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.RefexDynamicMember;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.RefexDynamicMemberFactory;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.RefexDynamicRevision;
import org.ihtsdo.otf.tcc.model.cc.relationship.Relationship;
import org.ihtsdo.otf.tcc.model.cc.relationship.RelationshipRevision;

public class BdbTermBuilder implements TerminologyBuilderBI {

    EditCoordinate ec;
    ViewCoordinate vc;

    public BdbTermBuilder(EditCoordinate ec, ViewCoordinate vc) {
        this.ec = ec;
        this.vc = vc;
    }

    @Override
    public RefexChronicleBI<?> construct(RefexCAB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        RefexMember<?, ?> refex = getRefex(blueprint);
        if (refex != null) {
            return updateRefex(refex, blueprint);
        }
        refex = createRefex(blueprint);
        return refex;
    }
    
    @Override
    public RefexDynamicChronicleBI<?> construct(RefexDynamicCAB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        RefexDynamicMember refex = getRefex(blueprint);
        if (refex != null) {
            return updateRefex(refex, blueprint);
        }
        refex = createRefex(blueprint);
        return refex;
    }

    public ConceptAttributes getConAttr(ConceptAttributeAB blueprint) throws IOException, InvalidCAB {
        ConceptAttributes cac = (ConceptAttributes) P.s.getConcept(blueprint.getComponentUuid()).getConceptAttributes();
        if (cac == null) {
            throw new InvalidCAB("ConAttrAB can only be used for amendment, not creation."
                    + " Use ConceptCB instead. " + blueprint);
        }
        return cac;
    }

    private RefexChronicleBI<?> updateRefex(RefexMember<?, ?> member,
            RefexCAB blueprint) throws InvalidCAB, IOException, ContradictionException {
        for (int pathNid : ec.getEditPaths().getSetValues()) {
            RefexRevision refexRevision
                    = member.makeAnalog(blueprint.getStatus(),
                            Long.MAX_VALUE,
                            ec.getAuthorNid(),
                            ec.getModuleNid(),
                            pathNid);
            try {
                blueprint.setPropertiesExceptStamp(refexRevision);
            } catch (PropertyVetoException ex) {
                throw new InvalidCAB("Refex: " + member
                        + "\n\nRefexAmendmentSpec: " + blueprint, ex);
            }
        }
        for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
            construct(annotBp);
        }
        for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
            construct(annotBp);
        }
        return member;
    }
    
    private RefexDynamicChronicleBI<?> updateRefex(RefexDynamicMember member,
            RefexDynamicCAB blueprint) throws InvalidCAB, IOException, ContradictionException {
        for (int pathNid : ec.getEditPaths().getSetValues()) {
            RefexDynamicRevision refexRevision
                    = member.makeAnalog(blueprint.getStatus(),
                            Long.MAX_VALUE,
                            ec.getAuthorNid(),
                            ec.getModuleNid(),
                            pathNid);
            try {
                blueprint.writeTo(refexRevision, false);
            } catch (PropertyVetoException ex) {
                throw new InvalidCAB("Refex: " + member
                        + "\n\nRefexAmendmentSpec: " + blueprint, ex);
            }
        }
        for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
            construct(annotBp);
        }
        for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
            construct(annotBp);
        }
        return member;
    }

    private RefexMember<?, ?> getRefex(RefexCAB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getMemberUUID()) && Integer.MAX_VALUE
                != P.s.getConceptNidForNid(P.s.getNidForUuids(blueprint.getMemberUUID()))) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getMemberUUID());
            if (component == null) {
                return null;
            }
            if (blueprint.getMemberType()
                    == RefexType.classToType(component.getClass())) {
                return (RefexMember<?, ?>) component;
            } else {
                throw new InvalidCAB(
                        "Component exists of different type. Class to type:  "
                        + RefexType.classToType(component.getClass())
                        + "\ncomponent: "
                        + component + "\n\nRefexCAB: " + blueprint);
            }
        }
        return null;
    }
    
    private RefexDynamicMember getRefex(RefexDynamicCAB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getMemberUUID()) && Integer.MAX_VALUE
                != P.s.getConceptNidForNid(P.s.getNidForUuids(blueprint.getMemberUUID()))) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getMemberUUID());
            if (component == null) {
                return null;
            }
            return (RefexDynamicMember) component;
        }
        return null;
    }

    @Override
    public RefexChronicleBI<?> constructIfNotCurrent(RefexCAB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        RefexMember<?, ?> refex = getRefex(blueprint);
        if (refex != null) {
            if (refex.getStamp() == -1) {
                return reCreateRefex(refex, blueprint);
            } else {
                boolean current = false;
                for (RefexVersionBI refexv : refex.getVersions(vc)) {
                    if (blueprint.validate(refexv)) {
                        current = true;
                        break;
                    }
                }
                if (current) {
                    return refex;
                }
                return updateRefex(refex, blueprint);
            }
        }

        return createRefex(blueprint);
    }

    private RefexChronicleBI<?> reCreateRefex(RefexMember<?, ?> refex,
            RefexCAB blueprint)
            throws IOException, InvalidCAB {
        return RefexMemberFactory.reCreate(blueprint, refex, ec);
    }

    private RefexMember<?, ?> createRefex(RefexCAB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        
        ConceptChronicle refexColCon = (ConceptChronicle) P.s.getConcept(blueprint.getRefexCollectionNid());
        
        if (blueprint.hasProperty(ComponentProperty.ENCLOSING_CONCEPT_ID)) {
            P.s.setConceptNidForNid(blueprint.getInt(ComponentProperty.ENCLOSING_CONCEPT_ID),
                    P.s.getNidForUuids(blueprint.getComponentUuid()));
        } else if (refexColCon.isAnnotationStyleRefex()) {
            int rcNid = P.s.getNidForUuids(blueprint.getReferencedComponentUuid());

            int enclosingConceptNid = P.s.getConceptNidForNid(rcNid);
            P.s.setConceptNidForNid(enclosingConceptNid, blueprint.getComponentNid());
        } else {
            int enclosingConceptNid = refexColCon.getNid();
            int blueprintNid = Ts.get().getConceptNidForNid(blueprint.getComponentNid());
            P.s.setConceptNidForNid(enclosingConceptNid, blueprint.getComponentNid());
        }

        RefexMember<?, ?> newRefex = RefexMemberFactory.create(blueprint, ec);

        for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
            annotBp.setReferencedComponent(newRefex);
            construct(annotBp);
        }
        for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
            annotBp.setReferencedComponent(newRefex);
            construct(annotBp);
        }
        return newRefex;
    }
    
    private RefexDynamicMember createRefex(RefexDynamicCAB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        
        ConceptChronicle refexColCon = (ConceptChronicle) P.s.getConcept(blueprint.getRefexAssemblageNid());
        
        if (blueprint.hasProperty(ComponentProperty.ENCLOSING_CONCEPT_ID)) {
            P.s.setConceptNidForNid(blueprint.getInt(ComponentProperty.ENCLOSING_CONCEPT_ID),
                    P.s.getNidForUuids(blueprint.getComponentUuid()));
        } else if (refexColCon.isAnnotationStyleRefex()) {
            int rcNid = P.s.getNidForUuids(blueprint.getReferencedComponentUuid());

            int enclosingConceptNid = P.s.getConceptNidForNid(rcNid);
            P.s.setConceptNidForNid(enclosingConceptNid, blueprint.getComponentNid());
        } else {
            int enclosingConceptNid = refexColCon.getNid();
            int blueprintNid = Ts.get().getConceptNidForNid(blueprint.getComponentNid());
            P.s.setConceptNidForNid(enclosingConceptNid, blueprint.getComponentNid());
        }

        RefexDynamicMember newRefex = RefexDynamicMemberFactory.create(blueprint, ec);

        for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
            annotBp.setReferencedComponent(newRefex);
            construct(annotBp);
        }
        for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
            annotBp.setReferencedComponent(newRefex);
            construct(annotBp);
        }
        return newRefex;
    }

    private RelationshipChronicleBI getRel(RelationshipCAB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getComponentUuid())) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getComponentUuid());
            if (component == null) {
                return null;
            }
            if (component instanceof RelationshipChronicleBI) {
                return (RelationshipChronicleBI) component;
            } else {
                throw new InvalidCAB(
                        "Component exists of different type: "
                        + component + "\n\nRelCAB: " + blueprint);
            }
        }
        return null;
    }

    @Override
    public RelationshipChronicleBI construct(RelationshipCAB blueprint) throws IOException, InvalidCAB, ContradictionException {
        RelationshipChronicleBI relc = getRel(blueprint);

        if (relc == null) {
            ConceptChronicle c = (ConceptChronicle) P.s.getConcept(blueprint.getSourceNid());
            Relationship r = new Relationship();
            Bdb.gVersion.incrementAndGet();
            r.enclosingConceptNid = c.getNid();
            r.nid = Bdb.uuidToNid(blueprint.getComponentUuid());
            Bdb.getMemoryCache().setCNidForNid(c.getNid(), r.nid);
            r.setPrimordialUuid(blueprint.getComponentUuid());
            try {
                r.setDestinationNid(blueprint.getTargetNid());
            } catch (PropertyVetoException ex) {
                throw new IOException(ex);
            }
            r.setTypeNid(blueprint.getTypeNid());
            r.setRefinabilityNid(blueprint.getRefinabilityNid());
            r.setCharacteristicNid(blueprint.getCharacteristicNid());
            r.primordialStamp = Integer.MIN_VALUE;
            r.setGroup(blueprint.getGroup());
            for (int p : ec.getEditPaths().getSetValues()) {
                if (r.primordialStamp == Integer.MIN_VALUE) {
                    r.primordialStamp
                            = Bdb.getStampDb().getStamp(blueprint.getStatus(), Long.MAX_VALUE,
                                    ec.getAuthorNid(), ec.getModuleNid(), p);
                } else {
                    if (r.revisions == null) {
                        r.revisions = new RevisionSet(r.primordialStamp);
                    }
                    r.revisions.add((RelationshipRevision) r.makeAnalog(blueprint.getStatus(),
                            Long.MAX_VALUE,
                            ec.getAuthorNid(),
                            ec.getModuleNid(),
                            p));
                }
            }
            c.getRelationshipsOutgoing().add(r);
            for (int p : ec.getEditPaths().getSetValues()) {
                for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                    construct(annotBp);
                }
                for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                    construct(annotBp);
                }
            }
            return r;
        } else {
            Relationship r = (Relationship) relc;
            for (int p : ec.getEditPaths().getSetValues()) {
                RelationshipRevision rv = r.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p);
                if (r.getDestinationNid() != blueprint.getTargetNid()) {
                    throw new InvalidCAB(
                            "r.getDestinationNid() != spec.getDestNid(): "
                            + r.getDestinationNid() + " : " + blueprint.getTargetNid());
                }
                rv.setTypeNid(blueprint.getTypeNid());
                rv.setRefinabilityNid(blueprint.getRefinabilityNid());
                rv.setCharacteristicNid(blueprint.getCharacteristicNid());
                rv.setGroup(blueprint.getGroup());
            }
            for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                construct(annotBp);
            }
            for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                construct(annotBp);
            }
        }
        return relc;
    }

    @Override
    public RelationshipChronicleBI constructIfNotCurrent(RelationshipCAB blueprint) throws IOException, InvalidCAB, ContradictionException {
        RelationshipChronicleBI relc = getRel(blueprint);
        if (relc == null) {
            return construct(blueprint);
        }
        Collection<? extends RelationshipVersionBI> relvs = relc.getVersions(vc);
        for (RelationshipVersionBI rv : relvs) {
            if (!blueprint.validate(rv)) {
                return construct(blueprint);
            }
        }
        return relc;
    }

    private DescriptionChronicleBI getDesc(DescriptionCAB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getComponentUuid())) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getComponentUuid());
            if (component == null) {
                return null;
            }
            if (component instanceof DescriptionChronicleBI) {
                return (DescriptionChronicleBI) component;
            } else {
                throw new InvalidCAB(
                        "Component exists of different type: "
                        + component + "\n\nDescCAB: " + blueprint);
            }
        }
        return null;
    }

    @Override
    public DescriptionChronicleBI constructIfNotCurrent(DescriptionCAB blueprint) throws IOException,
            InvalidCAB, ContradictionException {
        DescriptionChronicleBI desc = getDesc(blueprint);
        if (desc == null) {
            return construct(blueprint);
        }
        Collection<? extends DescriptionVersionBI> descvs = desc.getVersions(vc);
        for (DescriptionVersionBI dv : descvs) {
            if (!blueprint.validate(dv)) {
                return construct(blueprint);
            }
        }
        return desc;
    }

    @Override
    public DescriptionChronicleBI construct(DescriptionCAB blueprint) throws IOException, InvalidCAB, ContradictionException {
        DescriptionChronicleBI desc = getDesc(blueprint);

        if (desc == null) {
            int conceptNid = blueprint.getConceptNid();
            ConceptChronicle c = (ConceptChronicle) P.s.getConcept(blueprint.getConceptNid());
            Description d = new Description();
            Bdb.gVersion.incrementAndGet();
            d.enclosingConceptNid = c.getNid();
            d.nid = Bdb.uuidToNid(blueprint.getComponentUuid());
            Bdb.getMemoryCache().setCNidForNid(c.getNid(), d.nid);
            d.setPrimordialUuid(blueprint.getComponentUuid());
            d.setTypeNid(blueprint.getTypeNid());
            d.primordialStamp = Integer.MIN_VALUE;
            d.setLang(blueprint.getLang());
            d.setText(blueprint.getText());
            d.setInitialCaseSignificant(blueprint.isInitialCaseSignificant());
            for (int p : ec.getEditPaths().getSetValues()) {
                if (d.primordialStamp == Integer.MIN_VALUE) {
                    d.primordialStamp
                            = Bdb.getStampDb().getStamp(blueprint.getStatus(), Long.MAX_VALUE, ec.getAuthorNid(),
                                    ec.getModuleNid(), p);
                } else {
                    if (d.revisions == null) {
                        d.revisions = new RevisionSet(d.primordialStamp);
                    }
                    d.revisions.add((DescriptionRevision) d.makeAnalog(blueprint.getStatus(),
                            Long.MAX_VALUE,
                            ec.getAuthorNid(),
                            ec.getModuleNid(),
                            p));
                }
            }
            c.getDescriptions().add(d);
            for (int p : ec.getEditPaths().getSetValues()) {
                for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                    construct(annotBp);
                }
                for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                    construct(annotBp);
                }
            }
            return d;
        } else {
            Description d = (Description) desc;
            for (int p : ec.getEditPaths().getSetValues()) {
                DescriptionRevision dr = d.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p);
                dr.setTypeNid(blueprint.getTypeNid());
                dr.setText(blueprint.getText());
                dr.setLang(blueprint.getLang());
                dr.setStatus(blueprint.getStatus());
                dr.setInitialCaseSignificant(blueprint.isInitialCaseSignificant());
                for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                    construct(annotBp);
                }
                for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                    construct(annotBp);
                }
            }
        }
        return desc;
    }

    private MediaChronicleBI getMedia(MediaCAB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getComponentUuid())) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getComponentUuid());
            if (component == null) {
                return null;
            }
            if (component instanceof MediaChronicleBI) {
                return (MediaChronicleBI) component;
            } else {
                throw new InvalidCAB(
                        "Component exists of different type: "
                        + component + "\n\nMediaCAB: " + blueprint);
            }
        }
        return null;
    }

    @Override
    public MediaChronicleBI constructIfNotCurrent(MediaCAB blueprint) throws IOException,
            InvalidCAB, ContradictionException {
        MediaChronicleBI mediaC = getMedia(blueprint);
        if (mediaC == null) {
            return construct(blueprint);
        }
        Collection<? extends MediaVersionBI> mediaV = mediaC.getVersions(vc);
        for (MediaVersionBI dv : mediaV) {
            if (!blueprint.validate(dv)) {
                return construct(blueprint);
            }
        }
        return mediaC;
    }

    @Override
    public MediaChronicleBI construct(MediaCAB blueprint) throws IOException, InvalidCAB, ContradictionException {
        MediaChronicleBI imgC = getMedia(blueprint);

        if (imgC == null) {
            ConceptChronicle c = (ConceptChronicle) P.s.getConcept(blueprint.getConceptNid());
            Media img = new Media();
            Bdb.gVersion.incrementAndGet();
            img.enclosingConceptNid = c.getNid();
            img.nid = Bdb.uuidToNid(blueprint.getComponentUuid());
            Bdb.getMemoryCache().setCNidForNid(c.getNid(), img.nid);
            img.setPrimordialUuid(blueprint.getComponentUuid());
            img.setTypeNid(blueprint.getTypeNid());
            img.setFormat(blueprint.getFormat());
            img.setImage(blueprint.getDataBytes());
            img.setTextDescription(blueprint.getTextDescription());
            img.primordialStamp = Integer.MIN_VALUE;
            for (int p : ec.getEditPaths().getSetValues()) {
                if (img.primordialStamp == Integer.MIN_VALUE) {
                    img.primordialStamp
                            = Bdb.getStampDb().getStamp(blueprint.getStatus(), Long.MAX_VALUE, ec.getAuthorNid(),
                                    ec.getModuleNid(), p);
                } else {
                    if (img.revisions == null) {
                        img.revisions = new RevisionSet(img.primordialStamp);
                    }
                    img.revisions.add((MediaRevision) img.makeAnalog(blueprint.getStatus(),
                            Long.MAX_VALUE,
                            ec.getAuthorNid(),
                            ec.getModuleNid(),
                            p));
                }
            }
            c.getMedia().add(img);
            for (int p : ec.getEditPaths().getSetValues()) {
                for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                    construct(annotBp);
                }
                for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                    construct(annotBp);
                }
            }
            return img;
        } else {
            Media img = (Media) imgC;
            for (int p : ec.getEditPaths().getSetValues()) {
                MediaRevision imgR = img.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p);
                imgR.setTypeNid(blueprint.getTypeNid());
                imgR.setTextDescription(blueprint.getTextDescription());
                for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                    construct(annotBp);
                }
                for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                    construct(annotBp);
                }
            }
        }

        return imgC;
    }

    private ConceptChronicleBI getConcept(ConceptCB blueprint)
            throws InvalidCAB, IOException {
        if (P.s.hasUuid(blueprint.getComponentUuid())) {
            ComponentChronicleBI<?> component
                    = P.s.getComponent(blueprint.getComponentUuid());
            if (component == null) {
                return null;
            }
            if (component instanceof ConceptChronicleBI) {
                return (ConceptChronicleBI) component;
            } else {
                throw new InvalidCAB(
                        "Component exists of different type: "
                        + component + "\n\nConceptCAB: " + blueprint);
            }
        }
        return null;
    }

    @Override
    public ConceptChronicleBI constructIfNotCurrent(ConceptCB blueprint)
            throws IOException, InvalidCAB, ContradictionException {
        ConceptChronicleBI cc = getConcept(blueprint);
        if (cc == null) {
            return construct(blueprint);
        } else {
            ConceptChronicle concept = Bdb.getConceptForComponent(cc.getNid());
            if (concept.isCanceled() || concept.getPrimordialUuid().toString().length() == 0
                    || concept.getConceptAttributes().getVersions().isEmpty()) {
                return construct(blueprint);
            } else {
                throw new InvalidCAB(
                        "Concept already exists: "
                        + cc + "\n\nConceptCAB cannot be used for update: " + blueprint);
            }
        }
    }

    @Override
    public ConceptChronicleBI construct(ConceptCB blueprint) throws IOException, InvalidCAB, ContradictionException {

        int cNid = Bdb.uuidToNid(blueprint.getComponentUuid());
        Bdb.getMemoryCache().setCNidForNid(cNid, cNid);
        ConceptChronicle newC = ConceptChronicle.get(cNid);
        newC.setAnnotationStyleRefex(blueprint.isAnnotationRefexExtensionIdentity());

        ConceptAttributes a = null;
        if (newC.getConceptAttributes() == null) {
            a = new ConceptAttributes();
            a.nid = cNid;
            a.enclosingConceptNid = cNid;
            newC.setConceptAttributes(a);
        } else if (newC.isCanceled()) {
            a = newC.getConceptAttributes();
            for (int pathNid : ec.getEditPaths().getSetValues()) {
                a.resetUncommitted(blueprint.getStatus(), ec.getAuthorNid(), pathNid, ec.getModuleNid());
            }
            a.nid = cNid;
            a.enclosingConceptNid = cNid;
        } else {
            throw new InvalidCAB("Concept already exists:\n" + blueprint + "\n\n" + newC);
        }

        a.setDefined(blueprint.isDefined());
        a.setPrimordialUuid(blueprint.getComponentUuid());

        boolean primoridal = true;
        for (int p : ec.getEditPaths().getSetValues()) {
            if (primoridal) {
                primoridal = false;
                a.primordialStamp
                        = Bdb.getStampDb().getStamp(blueprint.getStatus(), Long.MAX_VALUE, ec.getAuthorNid(),
                                ec.getModuleNid(), p);
            } else {
                if (a.revisions == null) {
                    a.revisions = new RevisionSet(a.primordialStamp);
                }
                a.revisions.add((ConceptAttributesRevision) a.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p));
            }
        }

        List<DescriptionCAB> fsnBps = blueprint.getFullySpecifiedNameCABs();
        List<DescriptionCAB> prefBps = blueprint.getPreferredNameCABs();
        List<DescriptionCAB> descBps = blueprint.getDescriptionCABs();
        List<RelationshipCAB> relBps = blueprint.getRelationshipCABs();
        List<MediaCAB> mediaBps = blueprint.getMediaCABs();

        if (blueprint.getConceptAttributeAB() != null) {
            for (RefexCAB annot : blueprint.getConceptAttributeAB().getAnnotationBlueprints()) {
                this.construct(annot);
            }
            for (RefexDynamicCAB annot : blueprint.getConceptAttributeAB().getAnnotationDynamicBlueprints()) {
                this.construct(annot);
            }
        }

        for (DescriptionCAB fsnBp : fsnBps) {
            this.construct(fsnBp);
        }
        for (DescriptionCAB prefBp : prefBps) {
            this.construct(prefBp);
        }
        for (DescriptionCAB descBp : descBps) {
            if (fsnBps.contains(descBp) || prefBps.contains(descBp)) {
                continue;
            } else {
                this.construct(descBp);
            }
        }
        for (RelationshipCAB relBp : relBps) {
            this.construct(relBp);
        }
        for (MediaCAB mediaBp : mediaBps) {
            this.construct(mediaBp);
        }
        return newC;
    }

    @Override
    public ConceptAttributeChronicleBI construct(ConceptAttributeAB blueprint) throws IOException, InvalidCAB, ContradictionException {
        ConceptAttributes cac = getConAttr(blueprint);
        for (ConceptAttributeVersionBI cav : cac.getVersions(vc)) {
            for (int p : ec.getEditPaths().getSetValues()) {

                if (cac.revisions == null) {
                    cac.revisions
                            = new RevisionSet(cac.primordialStamp);
                }
                ConceptAttributesRevision r = (ConceptAttributesRevision) cac.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p);
                r.setDefined(blueprint.defined);
                cac.revisions.add(r);
            }
        }
        for (int p : ec.getEditPaths().getSetValues()) {
            for (RefexCAB annotBp : blueprint.getAnnotationBlueprints()) {
                construct(annotBp);
            }
            for (RefexDynamicCAB annotBp : blueprint.getAnnotationDynamicBlueprints()) {
                construct(annotBp);
            }
        }

        return cac;
    }

    @Override
    public ConceptAttributeChronicleBI constructIfNotCurrent(ConceptAttributeAB blueprint) throws IOException, InvalidCAB {
        ConceptAttributes cac = getConAttr(blueprint);
        for (ConceptAttributeVersionBI cav : cac.getVersions(vc)) {
            if (blueprint.validate(cav)) {
                return cac;
            }
            for (int p : ec.getEditPaths().getSetValues()) {

                if (cac.revisions == null) {
                    cac.revisions
                            = new RevisionSet(cac.primordialStamp);
                }
                ConceptAttributesRevision r = (ConceptAttributesRevision) cac.makeAnalog(blueprint.getStatus(),
                        Long.MAX_VALUE,
                        ec.getAuthorNid(),
                        ec.getModuleNid(),
                        p);
                r.setDefined(blueprint.defined);
                cac.revisions.add(r);

            }
        }

        return cac;
    }

    @Override
    public EditCoordinate getEditCoordinate() {
        return ec;
    }
}
