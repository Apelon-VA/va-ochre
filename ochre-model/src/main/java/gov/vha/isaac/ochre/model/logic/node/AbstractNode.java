package gov.vha.isaac.ochre.model.logic.node;

import gov.vha.isaac.ochre.api.DataTarget;
import gov.vha.isaac.ochre.api.logic.Node;
import gov.vha.isaac.ochre.api.tree.TreeNodeVisitData;
import gov.vha.isaac.ochre.collections.ConceptSequenceSet;
import gov.vha.isaac.ochre.model.logic.LogicalExpressionOchreImpl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by kec on 12/10/14.
 */
public abstract class AbstractNode implements Node {

    protected static final UUID namespaceUuid = UUID.fromString("d64c6d91-a37d-11e4-bcd8-0800200c9a66");

    LogicalExpressionOchreImpl logicGraphVersion;
    private short nodeIndex = Short.MIN_VALUE;
    protected UUID nodeUuid = null;
    

    public AbstractNode(LogicalExpressionOchreImpl logicGraphVersion) {
        this.logicGraphVersion = logicGraphVersion;
        logicGraphVersion.addNode(this);
    }

    public AbstractNode(LogicalExpressionOchreImpl logicGraphVersion, DataInputStream dataInputStream) throws IOException {
        nodeIndex = dataInputStream.readShort();
        this.logicGraphVersion = logicGraphVersion;
        logicGraphVersion.addNode(this);
    }
    
    protected AbstractNode(AbstractNode anotherNode) {
        this.nodeIndex = anotherNode.nodeIndex;
        this.nodeUuid = anotherNode.nodeUuid;
    }

    /**
     * Should be overridden by subclasses that need to add concepts. 
     * Concepts from connector nodes should not be added. 
     * @param conceptSequenceSet 
     */
    @Override
    public void addConceptsReferencedByNode(ConceptSequenceSet conceptSequenceSet) {
        conceptSequenceSet.add(getNodeSemantic().getConceptSequence());
    }
    
    
    @Override
    public void sort() {
        // override on nodes with multiple children. 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return (int) nodeIndex;
    }

    @Override
    public byte[] getBytes(DataTarget dataTarget) {
        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                DataOutputStream output = new DataOutputStream(outputStream);
                output.writeByte(getNodeSemantic().ordinal());
                output.writeShort(nodeIndex);
                writeNodeData(output, dataTarget);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public short getNodeIndex() {
        return nodeIndex;
    }

    @Override
    public void setNodeIndex(short nodeIndex) {
        if (this.nodeIndex == Short.MIN_VALUE) {
            this.nodeIndex = nodeIndex;
        } else if (this.nodeIndex == nodeIndex) {
            // nothing to do...
        } else {
            throw new IllegalStateException("Node index cannot be changed once set. NodeId: "
                    + this.nodeIndex + " attempted: " + nodeIndex);
        }
    }

    protected void writeData(DataOutput dataOutput, DataTarget dataTarget) throws IOException {
    }

    protected abstract void writeNodeData(DataOutput dataOutput, DataTarget dataTarget) throws IOException;

    @Override
    public int compareTo(Node o) {
        if (this.getNodeSemantic() != o.getNodeSemantic()) {
            return this.getNodeSemantic().compareTo(o.getNodeSemantic());
        }
        return compareFields(o);
     }

    protected abstract int compareFields(Node o);

    @Override
    public abstract AbstractNode[] getChildren();
    
    protected UUID getNodeUuid() {
        if (nodeUuid == null) {
            nodeUuid = initNodeUuid();
        }
        return nodeUuid;
    };
    
    public SortedSet<UUID> getNodeUuidSetForDepth(int depth) {
        SortedSet<UUID> uuidSet = new TreeSet<>();
        uuidSet.add(getNodeUuid());
        if (depth > 1) {
            for (AbstractNode child: getChildren()) {
                uuidSet.addAll(child.getNodeUuidSetForDepth(depth - 1));
            }
        }
        return uuidSet;
    }
    
    protected abstract UUID initNodeUuid();
    
    /**
     * 
     * @return A string representing the fragment of the expression 
     * rooted in this node. 
     */
    @Override
    public String fragmentToString() {
        
        return fragmentToString("");
    }
   @Override
    public String fragmentToString(String nodeIdSuffix) {
        StringBuilder builder = new StringBuilder();
        logicGraphVersion.processDepthFirst(this, (Node node, TreeNodeVisitData graphVisitData) -> {
            for (int i = 0; i < graphVisitData.getDistance(node.getNodeIndex()); i++) {
                builder.append("    ");
            }
            builder.append(node.toString(nodeIdSuffix));
            builder.append("\n");
        });
        return builder.toString();
    }

    @Override
    public String toString() {
        return toString("");
    }
    @Override
    public String toString(String nodeIdSuffix) {
         return "";
    }

    
    
}
