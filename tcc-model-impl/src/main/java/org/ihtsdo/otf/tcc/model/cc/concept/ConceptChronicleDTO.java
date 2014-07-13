/*
 * Copyright 2013 International Health Terminology Standards Development Organisation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ihtsdo.otf.tcc.model.cc.concept;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.ihtsdo.otf.tcc.api.chronicle.ComponentChronicleBI;
import org.ihtsdo.otf.tcc.api.contradiction.ContradictionException;
import org.ihtsdo.otf.tcc.api.coordinate.Position;
import org.ihtsdo.otf.tcc.api.chronicle.ProcessComponentChronicleBI;
import org.ihtsdo.otf.tcc.api.store.Ts;
import org.ihtsdo.otf.tcc.api.changeset.ChangeSetGenerationPolicy;
import org.ihtsdo.otf.tcc.api.changeset.ChangeSetGenerationThreadingPolicy;
import org.ihtsdo.otf.tcc.api.conattr.ConceptAttributeChronicleBI;
import org.ihtsdo.otf.tcc.api.concept.ConceptChronicleBI;
import org.ihtsdo.otf.tcc.api.concept.ConceptVersionBI;
import org.ihtsdo.otf.tcc.api.coordinate.EditCoordinate;
import org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate;
import org.ihtsdo.otf.tcc.api.description.DescriptionChronicleBI;
import org.ihtsdo.otf.tcc.api.id.IdBI;
import org.ihtsdo.otf.tcc.api.media.MediaChronicleBI;
import org.ihtsdo.otf.tcc.api.refex.RefexChronicleBI;
import org.ihtsdo.otf.tcc.api.refex.RefexVersionBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.RefexDynamicChronicleBI;
import org.ihtsdo.otf.tcc.api.refexDynamic.RefexDynamicVersionBI;
import org.ihtsdo.otf.tcc.api.relationship.RelationshipChronicleBI;
import org.ihtsdo.otf.tcc.api.relationship.group.RelGroupVersionBI;
import org.ihtsdo.otf.tcc.model.cc.attributes.ConceptAttributes;
import org.ihtsdo.otf.tcc.model.cc.description.Description;
import org.ihtsdo.otf.tcc.model.cc.media.Media;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMember;
import org.ihtsdo.otf.tcc.model.cc.refex.RefexMemberFactory;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.RefexDynamicMember;
import org.ihtsdo.otf.tcc.model.cc.refexDynamic.RefexDynamicMemberFactory;
import org.ihtsdo.otf.tcc.model.cc.relationship.Relationship;
import org.ihtsdo.otf.tcc.dto.TtkConceptChronicle;
import org.ihtsdo.otf.tcc.dto.component.attribute.TtkConceptAttributesChronicle;
import org.ihtsdo.otf.tcc.dto.component.description.TtkDescriptionChronicle;
import org.ihtsdo.otf.tcc.dto.component.media.TtkMediaChronicle;
import org.ihtsdo.otf.tcc.dto.component.refex.TtkRefexAbstractMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.refexDynamic.TtkRefexDynamicMemberChronicle;
import org.ihtsdo.otf.tcc.dto.component.relationship.TtkRelationshipChronicle;
 
/**
 *
 * @author kec
 */
public class ConceptChronicleDTO implements ConceptChronicleBI {

    int nid;
    boolean annotationStyleRefex;
    ConceptAttributes conceptAttributes;
    Collection<Description> descriptions;
    Collection<Relationship> relationshipsOutgoing;
    Collection<Media> media;
    Collection<RefexMember<?, ?>> refsetMembers;
    Collection<RefexDynamicMember> refsetMembersDynamic;

    public ConceptChronicleDTO(TtkConceptChronicle ttkConceptChronicle) throws IOException {
        this.nid = Ts.get().getNidForUuids(ttkConceptChronicle.getPrimordialUuid());
        this.annotationStyleRefex = ttkConceptChronicle.isAnnotationStyleRefex();

        TtkConceptAttributesChronicle eAttr = ttkConceptChronicle.getConceptAttributes();

        if (eAttr != null) {
            this.conceptAttributes = new ConceptAttributes(eAttr, this);
        }

        if ((ttkConceptChronicle.getDescriptions() != null) && !ttkConceptChronicle.getDescriptions().isEmpty()) {
            descriptions = new ArrayList<>(ttkConceptChronicle.getDescriptions().size());
            for (TtkDescriptionChronicle eDesc : ttkConceptChronicle.getDescriptions()) {
                Description desc = new Description(eDesc, this);

                descriptions.add(desc);
            }
        }

        if ((ttkConceptChronicle.getRelationships() != null) && !ttkConceptChronicle.getRelationships().isEmpty()) {
            relationshipsOutgoing = new ArrayList<>(ttkConceptChronicle.getRelationships().size());
            for (TtkRelationshipChronicle eRel : ttkConceptChronicle.getRelationships()) {
                Relationship rel = new Relationship(eRel, this);

                relationshipsOutgoing.add(rel);
            }
        }

        if ((ttkConceptChronicle.getMedia() != null) && !ttkConceptChronicle.getMedia().isEmpty()) {
            media = new ArrayList<>(ttkConceptChronicle.getMedia().size());
            for (TtkMediaChronicle eMedia : ttkConceptChronicle.getMedia()) {
                Media img = new Media(eMedia, this);

                media.add(img);
            }

        }

        if ((ttkConceptChronicle.getRefsetMembers() != null) && !ttkConceptChronicle.getRefsetMembers().isEmpty()) {
            refsetMembers = new ArrayList<>(ttkConceptChronicle.getRefsetMembers().size());
            for (TtkRefexAbstractMemberChronicle<?> eRefsetMember : ttkConceptChronicle.getRefsetMembers()) {
                RefexMember<?, ?> refsetMember = RefexMemberFactory.create(eRefsetMember, this.nid);

                refsetMembers.add(refsetMember);
            }

        }
        
        if ((ttkConceptChronicle.getRefsetMembersDynamic() != null) && !ttkConceptChronicle.getRefsetMembersDynamic().isEmpty()) {
            refsetMembersDynamic = new ArrayList<>(ttkConceptChronicle.getRefsetMembersDynamic().size());
            for (TtkRefexDynamicMemberChronicle eRefsetMember : ttkConceptChronicle.getRefsetMembersDynamic()) {
                RefexDynamicMember refsetMember = RefexDynamicMemberFactory.create(eRefsetMember, this.nid);

                refsetMembersDynamic.add(refsetMember);
            }

        }
    }

    public ConceptChronicleDTO(DataInput in) throws IOException {
        nid = in.readInt();
        annotationStyleRefex = in.readBoolean();
        int attributeBytes = in.readInt();
        if (attributeBytes > 0) {
            byte[] bytes = new byte[attributeBytes];
            in.readFully(bytes);
            this.conceptAttributes = new ConceptAttributes(this, new DataInputStream(new ByteArrayInputStream(bytes)));
        }
        int descriptionCount = in.readInt();
        if (descriptionCount > 0) {
            descriptions = new ArrayList<>(descriptionCount);
            for (int i = 0; i < descriptionCount; i++) {
                int byteCount = in.readInt();
                if (byteCount > 0) {
                    byte[] bytes = new byte[byteCount];
                    in.readFully(bytes);
                    descriptions.add(new Description(this, new DataInputStream(new ByteArrayInputStream(bytes))));
                }
            }
        }
        int relationshipCount = in.readInt();
        if (relationshipCount > 0) {
            relationshipsOutgoing = new ArrayList<>(relationshipCount);
            for (int i = 0; i < relationshipCount; i++) {
                int byteCount = in.readInt();
                if (byteCount > 0) {
                    byte[] bytes = new byte[byteCount];
                    in.readFully(bytes);
                    relationshipsOutgoing.add(new Relationship(this, new DataInputStream(new ByteArrayInputStream(bytes))));
                }
            }
        }
        int mediaCount = in.readInt();
        if (mediaCount > 0) {
            media = new ArrayList<>(mediaCount);
            for (int i = 0; i < mediaCount; i++) {
                int byteCount = in.readInt();
                if (byteCount > 0) {
                    byte[] bytes = new byte[byteCount];
                    in.readFully(bytes);
                    media.add(new Media(this, new DataInputStream(new ByteArrayInputStream(bytes))));
                }
            }
        }
        int refsetMemberCount = in.readInt();
        if (refsetMemberCount > 0) {
            refsetMembers = new ArrayList<>(refsetMemberCount);
            for (int i = 0; i < refsetMemberCount; i++) {
                int refexNid = in.readInt();
                int type = in.readInt();
                int byteCount = in.readInt();
                if (byteCount > 0) {
                    byte[] bytes = new byte[byteCount];
                    in.readFully(bytes);
                    refsetMembers.add(RefexMemberFactory.create(refexNid, type, nid,
                            new DataInputStream(new ByteArrayInputStream(bytes))));
                }
            }
        }
        
        int refsetMemberDynamicCount = in.readInt();
        if (refsetMemberDynamicCount > 0) {
            refsetMembersDynamic = new ArrayList<>(refsetMemberDynamicCount);
            for (int i = 0; i < refsetMemberDynamicCount; i++) {
                int refexNid = in.readInt();
                int byteCount = in.readInt();
                if (byteCount > 0) {
                    byte[] bytes = new byte[byteCount];
                    in.readFully(bytes);
                    refsetMembersDynamic.add(RefexDynamicMemberFactory.create(refexNid, nid, new DataInputStream(new ByteArrayInputStream(bytes))));
                }
            }
        }
    }

    @Override
    public void writeExternal(DataOutputStream out) throws IOException {
        out.writeInt(nid);
        out.writeBoolean(annotationStyleRefex);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream to = new DataOutputStream(new ByteArrayOutputStream());
        if (conceptAttributes != null) {
            conceptAttributes.writeToBdb(to, Integer.MIN_VALUE);
            byte[] bytes = baos.toByteArray();
            out.writeInt(bytes.length);
            out.write(bytes);
        } else {
            out.writeInt(0);
        }
        baos = new ByteArrayOutputStream();
        to = new DataOutputStream(new ByteArrayOutputStream());
        if ((descriptions != null) && !descriptions.isEmpty()) {
            out.writeInt(descriptions.size());
            for (Description desc : descriptions) {
                desc.writeToBdb(to, Integer.MIN_VALUE);
                byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                out.write(bytes);
            }
        } else {
            out.writeInt(0);
        }

        if ((relationshipsOutgoing != null) && !relationshipsOutgoing.isEmpty()) {
            out.writeInt(relationshipsOutgoing.size());
            for (Relationship rel : relationshipsOutgoing) {
                baos = new ByteArrayOutputStream();
                to = new DataOutputStream(new ByteArrayOutputStream());
                rel.writeToBdb(to, Integer.MIN_VALUE);
                byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                out.write(bytes);
            }
        } else {
            out.writeInt(0);
        }

        if ((media != null) && !media.isEmpty()) {
            out.writeInt(media.size());
            for (Media medium : media) {
                baos = new ByteArrayOutputStream();
                to = new DataOutputStream(new ByteArrayOutputStream());
                medium.writeToBdb(to, Integer.MIN_VALUE);
                byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                out.write(bytes);
            }

        } else {
            out.writeInt(0);
        }

        if ((refsetMembers != null) && !refsetMembers.isEmpty()) {
            out.writeInt(refsetMembers.size());
            for (RefexMember<?, ?> refsetMember : refsetMembers) {
                out.writeInt(refsetMember.nid);
                out.writeInt(refsetMember.getRefexType().getTypeToken());
                baos = new ByteArrayOutputStream();
                to = new DataOutputStream(new ByteArrayOutputStream());
                refsetMember.writeToBdb(to, Integer.MIN_VALUE);
                byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                try {
                    out.write(bytes);
                } catch (OutOfMemoryError ex) {
                    throw ex;
                }
            }

        } else {
            out.writeInt(0);
        }
        
        if ((refsetMembersDynamic != null) && !refsetMembersDynamic.isEmpty()) {
            out.writeInt(refsetMembersDynamic.size());
            for (RefexDynamicMember refsetMember : refsetMembersDynamic) {
                out.writeInt(refsetMember.nid);
                baos = new ByteArrayOutputStream();
                to = new DataOutputStream(new ByteArrayOutputStream());
                refsetMember.writeToBdb(to, Integer.MIN_VALUE);
                byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                out.write(bytes);
            }

        } else {
            out.writeInt(0);
        }
    }

    @Override
    public boolean isAnnotationStyleRefex() throws IOException {
        return annotationStyleRefex;
    }

    @Override
    public void setAnnotationStyleRefex(boolean annotationSyleRefex) {
        this.annotationStyleRefex = annotationSyleRefex;
    }

    @Override
    public ConceptAttributeChronicleBI getConceptAttributes() throws IOException {
        return conceptAttributes;
    }

    @Override
    public Collection<? extends MediaChronicleBI> getMedia() throws IOException {
        return media;
    }

    @Override
    public Collection<? extends DescriptionChronicleBI> getDescriptions() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getRefsetMembers() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @see org.ihtsdo.otf.tcc.api.concept.ConceptChronicleBI#getRefsetDynamicMembers()
     */
    @Override
    public Collection<? extends RefexDynamicChronicleBI<?>> getRefsetDynamicMembers() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Collection<? extends RelationshipChronicleBI> getRelationshipsOutgoing() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptChronicleBI getEnclosingConcept() {
        return this;
    }

    @Override
    public int getNid() {
        return nid;
    }

    @Override
    public int getConceptNid() {
        return nid;
    }

    // ----------------------- UNSUPPORTED BELOW ---------------------------
    @Override
    public Collection<? extends IdBI> getAdditionalIds() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends IdBI> getAllIds() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getAnnotations() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAnnotation(RefexChronicleBI<?> annotation) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @see org.ihtsdo.otf.tcc.api.chronicle.ComponentBI#addDynamicAnnotation(org.ihtsdo.otf.tcc.api.refexDynamic.RefexDynamicChronicleBI)
     */
    @Override
    public boolean addDynamicAnnotation(RefexDynamicChronicleBI<?> annotation) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public UUID getPrimordialUuid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RefexChronicleBI<?> getRefsetMemberForComponent(int componentNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancel() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean commit(ChangeSetGenerationPolicy changeSetPolicy, ChangeSetGenerationThreadingPolicy changeSetWriterThreading) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toLongString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RefexVersionBI<?> getCurrentRefsetMemberForComponent(ViewCoordinate vc, int componentNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComponentChronicleBI<?> getComponent(int nid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getCurrentRefsetMembers(ViewCoordinate vc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getCurrentRefsetMembers(ViewCoordinate vc, Long cutoffTime) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RelGroupVersionBI> getRelationshipGroupsActive(ViewCoordinate vc) throws IOException, ContradictionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RelationshipChronicleBI> getRelationshipsIncoming() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasCurrentRefsetMemberForComponent(ViewCoordinate vc, int componentNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processComponentChronicles(ProcessComponentChronicleBI processor) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptVersionBI getVersion(ViewCoordinate c) throws ContradictionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends ConceptVersionBI> getVersions(ViewCoordinate c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends ConceptVersionBI> getVersions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isUncommitted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Integer> getAllStamps() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Position> getPositions() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptVersionBI getPrimordialVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean makeAdjudicationAnalogs(EditCoordinate ec, ViewCoordinate vc) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toUserString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getAnnotationsActive(ViewCoordinate xyz) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends RefexVersionBI<?>> Collection<T> getAnnotationsActive(ViewCoordinate xyz, Class<T> cls) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getAnnotationsActive(ViewCoordinate xyz, int refexNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends RefexVersionBI<?>> Collection<T> getAnnotationsActive(ViewCoordinate xyz, int refexNid, Class<T> cls) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersActive(ViewCoordinate xyz, int refsetNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersActive(ViewCoordinate xyz) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexVersionBI<?>> getRefexMembersInactive(ViewCoordinate xyz) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getRefexMembers(int refsetNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends RefexChronicleBI<?>> getRefexes() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @see org.ihtsdo.otf.tcc.api.chronicle.ComponentBI#getRefexesDynamic()
     */
    @Override
    public Collection<? extends RefexDynamicChronicleBI<?>> getRefexesDynamic() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @see org.ihtsdo.otf.tcc.api.chronicle.ComponentBI#getRefexDynamicAnnotations()
     */
    @Override
    public Collection<? extends RefexDynamicChronicleBI<?>> getRefexDynamicAnnotations() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @see org.ihtsdo.otf.tcc.api.chronicle.ComponentBI#getRefexDynamicMembers()
     */
    @Override
    public Collection<? extends RefexDynamicChronicleBI<?>> getRefexDynamicMembers() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @see org.ihtsdo.otf.tcc.api.chronicle.ComponentBI#getRefexesDynamicActive(org.ihtsdo.otf.tcc.api.coordinate.ViewCoordinate)
     */
    @Override
    public Collection<? extends RefexDynamicVersionBI<?>> getRefexesDynamicActive(ViewCoordinate viewCoordinate) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UUID> getUUIDs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasCurrentAnnotationMember(ViewCoordinate xyz, int refsetNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasCurrentRefexMember(ViewCoordinate xyz, int refsetNid) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
