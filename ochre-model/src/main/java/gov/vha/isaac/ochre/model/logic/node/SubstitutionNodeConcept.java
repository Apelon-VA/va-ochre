package gov.vha.isaac.ochre.model.logic.node;

import gov.vha.isaac.ochre.api.logic.assertions.substitution.SubstitutionFieldSpecification;
import gov.vha.isaac.ochre.model.logic.LogicalExpressionOchreImpl;
import gov.vha.isaac.ochre.api.logic.NodeSemantic;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;
import gov.vha.isaac.ochre.util.UuidT5Generator;

/**
 * Created by kec on 12/10/14.
 */
public class SubstitutionNodeConcept extends SubstitutionNode {

    public SubstitutionNodeConcept(LogicalExpressionOchreImpl logicGraphVersion, DataInputStream dataInputStream) throws IOException {
        super(logicGraphVersion, dataInputStream);
    }

    public SubstitutionNodeConcept(LogicalExpressionOchreImpl logicGraphVersion,
            SubstitutionFieldSpecification substitutionFieldSpecification) {
        super(logicGraphVersion, substitutionFieldSpecification);
    }

    @Override
    public NodeSemantic getNodeSemantic() {
        return NodeSemantic.SUBSTITUTION_CONCEPT;
    }

    @Override
    public String toString() {
        return toString("");
    }

    @Override
    public String toString(String nodeIdSuffix) {
        return "Concept substitution[" + getNodeIndex() + nodeIdSuffix + "]" + super.toString(nodeIdSuffix);
    }

    @Override
    protected UUID initNodeUuid() {
        return UuidT5Generator.get(getNodeSemantic().getSemanticUuid(),
                substitutionFieldSpecification.toString());
    }
}
