/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.vha.isaac.ochre.model.logic.node.external;


import gov.vha.isaac.ochre.api.DataTarget;
import gov.vha.isaac.ochre.model.logic.LogicExpressionOchreImpl;
import gov.vha.isaac.ochre.model.logic.Node;
import gov.vha.isaac.ochre.model.logic.NodeSemantic;
import gov.vha.isaac.ochre.model.logic.node.AbstractNode;
import gov.vha.isaac.ochre.model.logic.node.internal.TemplateNodeWithNids;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import gov.vha.isaac.ochre.util.UuidT5Generator;

/**
 *
 * @author kec
 */
public class TemplateNodeWithUuids extends AbstractNode {

    /**
     * Sequence of the concept that defines the template
     */
    UUID templateConceptUuid;

    /**
     * Sequence of the assemblage concept that provides the substitution values
     * for the template.
     */
    UUID assemblageConceptUuid;

    public TemplateNodeWithUuids(LogicExpressionOchreImpl logicGraphVersion, DataInputStream dataInputStream) throws IOException {
        super(logicGraphVersion, dataInputStream);
        templateConceptUuid = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
        assemblageConceptUuid = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
    }

    public TemplateNodeWithUuids(LogicExpressionOchreImpl logicGraphVersion, UUID templateConceptUuid, UUID assemblageConceptUuid) {
        super(logicGraphVersion);
        this.templateConceptUuid = templateConceptUuid;
        this.assemblageConceptUuid = assemblageConceptUuid;
    }

    public TemplateNodeWithUuids(TemplateNodeWithNids internalForm) {
        super(internalForm);
        this.templateConceptUuid = getIdentifierService().getUuidPrimordialForNid(internalForm.getTemplateConceptNid()).get();
        this.assemblageConceptUuid =getIdentifierService().getUuidPrimordialForNid(internalForm.getAssemblageConceptNid()).get();
    }


    @Override
    public void writeNodeData(DataOutput dataOutput, DataTarget dataTarget) throws IOException {
        super.writeData(dataOutput, dataTarget);
        switch (dataTarget) {
            case EXTERNAL:
                dataOutput.writeLong(templateConceptUuid.getMostSignificantBits());
                dataOutput.writeLong(templateConceptUuid.getLeastSignificantBits());
                dataOutput.writeLong(assemblageConceptUuid.getMostSignificantBits());
                dataOutput.writeLong(assemblageConceptUuid.getLeastSignificantBits());
                break;
            case INTERNAL:
                TemplateNodeWithNids internalForm =  new TemplateNodeWithNids(this);
                internalForm.writeNodeData(dataOutput, dataTarget);
                break;
            default: throw new UnsupportedOperationException("Can't handle dataTarget: " + dataTarget);
        }
    }


    @Override
    public NodeSemantic getNodeSemantic() {
        return NodeSemantic.TEMPLATE;
    }

    @Override
    public final AbstractNode[] getChildren() {
        return new AbstractNode[0];
    }

    @Override
    public final void addChildren(Node... children) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "TemplateNode[" + getNodeIndex() + "]: "
                + "assemblage: " + getConceptService().getConcept(assemblageConceptUuid).toUserString()
                + ", template: " + getConceptService().getConcept(templateConceptUuid).toUserString()
                + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        TemplateNodeWithUuids that = (TemplateNodeWithUuids) o;

        if (!assemblageConceptUuid.equals(that.assemblageConceptUuid)) {
            return false;
        }
        return templateConceptUuid.equals(that.templateConceptUuid);
    }

    @Override
    protected int compareFields(Node o) {
        TemplateNodeWithUuids that = (TemplateNodeWithUuids) o;
        if (!assemblageConceptUuid.equals(that.assemblageConceptUuid)) {
            return assemblageConceptUuid.compareTo(that.assemblageConceptUuid);
        }

        return templateConceptUuid.compareTo(that.templateConceptUuid);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + templateConceptUuid.hashCode();
        result = 31 * result + assemblageConceptUuid.hashCode();
        return result;
    }

    @Override
    protected UUID initNodeUuid() {
        try {
            return UuidT5Generator.get(getNodeSemantic().getSemanticUuid(),
                    templateConceptUuid.toString()
                    + assemblageConceptUuid.toString());
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public UUID getTemplateConceptUuid() {
        return templateConceptUuid;
    }

    public UUID getAssemblageConceptUuid() {
        return assemblageConceptUuid;
    }

}