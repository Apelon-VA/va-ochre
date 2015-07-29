/*
 * Copyright 2015 kec.
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
package gov.vha.isaac.ochre.model.logic.definition;

import gov.vha.isaac.ochre.api.ConceptProxy;
import gov.vha.isaac.ochre.api.Get;
import gov.vha.isaac.ochre.api.component.concept.ConceptChronology;
import gov.vha.isaac.ochre.api.logic.LogicalExpression;
import gov.vha.isaac.ochre.api.logic.LogicalExpressionBuilder;
import gov.vha.isaac.ochre.api.logic.Node;
import gov.vha.isaac.ochre.api.logic.assertions.AllRole;
import gov.vha.isaac.ochre.api.logic.assertions.Assertion;
import gov.vha.isaac.ochre.api.logic.assertions.ConceptAssertion;
import gov.vha.isaac.ochre.api.logic.assertions.Feature;
import gov.vha.isaac.ochre.api.logic.assertions.NecessarySet;
import gov.vha.isaac.ochre.api.logic.assertions.SomeRole;
import gov.vha.isaac.ochre.api.logic.assertions.SufficientSet;
import gov.vha.isaac.ochre.api.logic.assertions.Template;
import gov.vha.isaac.ochre.api.logic.assertions.connectors.And;
import gov.vha.isaac.ochre.api.logic.assertions.connectors.Connector;
import gov.vha.isaac.ochre.api.logic.assertions.connectors.DisjointWith;
import gov.vha.isaac.ochre.api.logic.assertions.connectors.Or;
import gov.vha.isaac.ochre.api.logic.assertions.literal.BooleanLiteral;
import gov.vha.isaac.ochre.api.logic.assertions.literal.FloatLiteral;
import gov.vha.isaac.ochre.api.logic.assertions.literal.InstantLiteral;
import gov.vha.isaac.ochre.api.logic.assertions.literal.IntegerLiteral;
import gov.vha.isaac.ochre.api.logic.assertions.literal.LiteralAssertion;
import gov.vha.isaac.ochre.api.logic.assertions.literal.StringLiteral;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.BooleanSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.ConceptSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.FloatSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.InstantSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.IntegerSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.StringSubstitution;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.SubstitutionFieldSpecification;
import gov.vha.isaac.ochre.model.logic.LogicalExpressionOchreImpl;
import gov.vha.isaac.ochre.api.logic.NodeSemantic;
import gov.vha.isaac.ochre.api.logic.assertions.LogicalSet;
import gov.vha.isaac.ochre.api.logic.assertions.substitution.SubstitutionAssertion;
import gov.vha.isaac.ochre.model.logic.node.AbstractNode;
import gov.vha.isaac.ochre.model.logic.node.LiteralNodeBoolean;
import gov.vha.isaac.ochre.model.logic.node.LiteralNodeFloat;
import gov.vha.isaac.ochre.model.logic.node.LiteralNodeInstant;
import gov.vha.isaac.ochre.model.logic.node.LiteralNodeInteger;
import gov.vha.isaac.ochre.model.logic.node.LiteralNodeString;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeBoolean;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeConcept;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeFloat;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeInstant;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeInteger;
import gov.vha.isaac.ochre.model.logic.node.SubstitutionNodeString;
import gov.vha.isaac.ochre.model.logic.node.internal.ConceptNodeWithNids;
import gov.vha.isaac.ochre.model.logic.node.internal.FeatureNodeWithNids;
import gov.vha.isaac.ochre.model.logic.node.internal.RoleNodeAllWithNids;
import gov.vha.isaac.ochre.model.logic.node.internal.RoleNodeSomeWithNids;
import gov.vha.isaac.ochre.model.logic.node.internal.TemplateNodeWithNids;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.mahout.math.map.OpenShortObjectHashMap;

/**
 *
 * @author kec
 */
public class LogicalExpressionBuilderOchreImpl implements LogicalExpressionBuilder {

    private boolean built = false;
    private short nextAxiomId = 0;
    private final Set<GenericAxiom> rootSets = new HashSet<>();
    private final HashMap<GenericAxiom, List<GenericAxiom>> definitionTree = new HashMap<>(20);
    private final OpenShortObjectHashMap<Object> axiomParameters = new OpenShortObjectHashMap<>(20);

    public LogicalExpressionBuilderOchreImpl() {
    }

    public short getNextAxiomIndex() {
        return nextAxiomId++;
    }

    private List<GenericAxiom> asList(Assertion... assertions) {
        ArrayList<GenericAxiom> list = new ArrayList<>(assertions.length);
        Arrays.stream(assertions).forEach((assertion) -> list.add((GenericAxiom) assertion));
        return list;
    }
    private List<? extends Assertion> makeAssertionsFromNodeDescendants(Node node) {
        return node.getChildStream().map((childNode) -> 
                makeAssertionFromNode(childNode)).collect(Collectors.toList());
    }

    private Assertion makeAssertionFromNode(Node node) {
        switch (node.getNodeSemantic()) {
            case DEFINITION_ROOT:
                break;
            case NECESSARY_SET:
                return necessarySet(makeAssertionsFromNodeDescendants(node).toArray(new Connector[0]));
            case SUFFICIENT_SET:
                return sufficientSet(makeAssertionsFromNodeDescendants(node).toArray(new Connector[0]));
            case AND:
                return and(makeAssertionsFromNodeDescendants(node).toArray(new Assertion[0]));
            case OR:
                return or(makeAssertionsFromNodeDescendants(node).toArray(new Assertion[0]));
            case DISJOINT_WITH:
                break;
            case ROLE_ALL:
                RoleNodeAllWithNids allRoleNode = (RoleNodeAllWithNids) node;
                return allRole(allRoleNode.getTypeConceptNid(), makeAssertionFromNode(allRoleNode.getOnlyChild()));
            case ROLE_SOME:
                RoleNodeSomeWithNids someRoleNode = (RoleNodeSomeWithNids) node;
                return someRole(someRoleNode.getTypeConceptNid(), makeAssertionFromNode(someRoleNode.getOnlyChild()));
            case CONCEPT:
                ConceptNodeWithNids conceptNode = (ConceptNodeWithNids) node;
                return conceptAssertion(conceptNode.getConceptNid());
            case FEATURE:
                FeatureNodeWithNids featureNode = (FeatureNodeWithNids) node;
                return feature(featureNode.getTypeConceptNid(), 
                        (LiteralAssertion) makeAssertionFromNode(featureNode.getOnlyChild()));
            case LITERAL_BOOLEAN:
                LiteralNodeBoolean literalNodeBoolean = (LiteralNodeBoolean) node;
                return booleanLiteral(literalNodeBoolean.getLiteralValue());
            case LITERAL_FLOAT:
                LiteralNodeFloat literalNodeFloat = (LiteralNodeFloat) node;
                return floatLiteral(literalNodeFloat.getLiteralValue());
            case LITERAL_INSTANT:
                LiteralNodeInstant literalNodeInstant = (LiteralNodeInstant) node;
                return instantLiteral(literalNodeInstant.getLiteralValue());
            case LITERAL_INTEGER:
                LiteralNodeInteger literalNodeInteger = (LiteralNodeInteger) node;
                return integerLiteral(literalNodeInteger.getLiteralValue());
            case LITERAL_STRING:
                LiteralNodeString literalNodeString = (LiteralNodeString) node;
                return stringLiteral(literalNodeString.getLiteralValue());

            case TEMPLATE:
                TemplateNodeWithNids templateNode = (TemplateNodeWithNids) node;
                return template(templateNode.getTemplateConceptNid(), 
                        templateNode.getAssemblageConceptNid());
            case SUBSTITUTION_CONCEPT:
                SubstitutionNodeConcept substitutionNodeConcept = (SubstitutionNodeConcept) node;
                return conceptSubstitution(substitutionNodeConcept.getSubstitutionFieldSpecification());
            case SUBSTITUTION_BOOLEAN:
                SubstitutionNodeBoolean substitutionNodeBoolean = (SubstitutionNodeBoolean) node;
                return booleanSubstitution(substitutionNodeBoolean.getSubstitutionFieldSpecification());
            case SUBSTITUTION_FLOAT:
                SubstitutionNodeFloat substitutionNodeFloat = (SubstitutionNodeFloat) node;
                return floatSubstitution(substitutionNodeFloat.getSubstitutionFieldSpecification());
            case SUBSTITUTION_INSTANT:
                SubstitutionNodeInstant substitutionNodeInstant = (SubstitutionNodeInstant) node;
                return instantSubstitution(substitutionNodeInstant.getSubstitutionFieldSpecification());
            case SUBSTITUTION_INTEGER:
                SubstitutionNodeInteger substitutionNodeInteger = (SubstitutionNodeInteger) node;
                return integerSubstitution(substitutionNodeInteger.getSubstitutionFieldSpecification());
            case SUBSTITUTION_STRING:
                SubstitutionNodeString substitutionNodeString = (SubstitutionNodeString) node;
                return stringSubstitution(substitutionNodeString.getSubstitutionFieldSpecification());
        }
        throw new UnsupportedOperationException("Can't handle: " + node.getNodeSemantic());
    }

    @Override
    public void addToRoot(LogicalSet logicalSet) {
        checkNotBuilt();
        
        GenericAxiom axiom;
        if (logicalSet instanceof NecessarySet) {
           axiom = new GenericAxiom(NodeSemantic.NECESSARY_SET, this);
        } else {
           axiom = new GenericAxiom(NodeSemantic.SUFFICIENT_SET, this);
        }
        rootSets.add(axiom);
        addToDefinitionTree(axiom, logicalSet);
    }

    @Override
    public Assertion cloneSubTree(Node subTreeRoot) {
        return makeAssertionFromNode(subTreeRoot);
    }

    @Override
    public NecessarySet necessarySet(Connector... connector) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.NECESSARY_SET, this);
        rootSets.add(axiom);
        addToDefinitionTree(axiom, connector);
        return axiom;
    }

    protected void addToDefinitionTree(GenericAxiom axiom, Assertion... connectors) {
        definitionTree.put(axiom, asList(connectors));
    }

    @Override
    public SufficientSet sufficientSet(Connector... connector) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUFFICIENT_SET, this);
        rootSets.add(axiom);
        addToDefinitionTree(axiom, connector);
        return axiom;
    }

    @Override
    public DisjointWith disjointWith(ConceptChronology conceptChronology) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.DISJOINT_WITH, this);
        axiomParameters.put(axiom.getIndex(), conceptChronology);
        return axiom;
    }


    @Override
    public And and(Assertion... assertions) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.AND, this);
        addToDefinitionTree(axiom, assertions);
        return axiom;
    }

    @Override
    public ConceptAssertion conceptAssertion(ConceptChronology conceptChronology) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.CONCEPT, this);
        axiomParameters.put(axiom.getIndex(), conceptChronology);
        return axiom;
    }

    @Override
    public AllRole allRole(ConceptChronology roleTypeChronology, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_ALL, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), roleTypeChronology);
        return axiom;
    }

    @Override
    public Feature feature(ConceptChronology featureTypeChronology, LiteralAssertion literal) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.FEATURE, this);
        addToDefinitionTree(axiom, literal);
        axiomParameters.put(axiom.getIndex(), featureTypeChronology);
        return axiom;
    }

    @Override
    public SomeRole someRole(ConceptChronology roleTypeChronology, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_SOME, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), roleTypeChronology);
        return axiom;
    }

    @Override
    public Template template(ConceptChronology templateChronology, ConceptChronology assemblageToPopulateTemplateConcept) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.TEMPLATE, this);
        axiomParameters.put(axiom.getIndex(), new Object[]{templateChronology, assemblageToPopulateTemplateConcept});
        return axiom;
    }
    private Template template(Integer templateChronologyNid, Integer assemblageToPopulateTemplateConceptNid) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.TEMPLATE, this);
        axiomParameters.put(axiom.getIndex(), new Object[]{templateChronologyNid, assemblageToPopulateTemplateConceptNid});
        return axiom;
    }

    @Override
    public Or or(Assertion... assertions) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.OR, this);
        addToDefinitionTree(axiom, assertions);
        return axiom;
    }

    @Override
    public BooleanLiteral booleanLiteral(boolean booleanLiteral) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.LITERAL_BOOLEAN, this);
        axiomParameters.put(axiom.getIndex(), booleanLiteral);
        return axiom;
    }

    @Override
    public FloatLiteral floatLiteral(float floatLiteral) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.LITERAL_FLOAT, this);
        axiomParameters.put(axiom.getIndex(), floatLiteral);
        return axiom;
    }

    @Override
    public InstantLiteral instantLiteral(Instant literalValue) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.LITERAL_INSTANT, this);
        axiomParameters.put(axiom.getIndex(), literalValue);
        return axiom;
    }

    @Override
    public IntegerLiteral integerLiteral(int literalValue) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.LITERAL_INTEGER, this);
        axiomParameters.put(axiom.getIndex(), literalValue);
        return axiom;
    }

    @Override
    public StringLiteral stringLiteral(String literalValue) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.LITERAL_STRING, this);
        axiomParameters.put(axiom.getIndex(), literalValue);
        return axiom;
    }

    @Override
    public BooleanSubstitution booleanSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_BOOLEAN, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public ConceptSubstitution conceptSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_CONCEPT, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public FloatSubstitution floatSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_FLOAT, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public InstantSubstitution instantSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_INSTANT, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public IntegerSubstitution integerSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_INTEGER, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public StringSubstitution stringSubstitution(SubstitutionFieldSpecification fieldSpecification) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.SUBSTITUTION_STRING, this);
        axiomParameters.put(axiom.getIndex(), fieldSpecification);
        return axiom;
    }

    @Override
    public LogicalExpression build() throws IllegalStateException {
        checkNotBuilt();
        LogicalExpressionOchreImpl definition = new LogicalExpressionOchreImpl();
        definition.Root();

        rootSets.forEach((axiom) -> addToDefinition(axiom, definition));

        definition.sort();
        built = true;
        return definition;
    }

    private void checkNotBuilt() throws IllegalStateException {
        if (built) {
            throw new IllegalStateException("Builder has already built. Builders cannot be reused.");
        }
    }

    private AbstractNode addToDefinition(GenericAxiom axiom, LogicalExpressionOchreImpl definition)
            throws IllegalStateException {

        AbstractNode newNode;
        switch (axiom.getSemantic()) {
            case NECESSARY_SET:
                newNode = definition.NecessarySet(getChildren(axiom, definition));
                definition.getRoot().addChildren(newNode);
                return newNode;
            case SUFFICIENT_SET:
                newNode = definition.SufficientSet(getChildren(axiom, definition));
                definition.getRoot().addChildren(newNode);
                return newNode;
            case AND:
                return definition.And(getChildren(axiom, definition));
            case OR:
                return definition.Or(getChildren(axiom, definition));
            case FEATURE:
                if (axiomParameters.get(axiom.getIndex()) instanceof Integer) {
                    return definition.Feature((Integer) axiomParameters.get(axiom.getIndex()),
                        addToDefinition(definitionTree.get(axiom).get(0), definition));
                }
                ConceptChronology featureTypeProxy = (ConceptChronology) axiomParameters.get(axiom.getIndex());
                return definition.Feature(featureTypeProxy.getNid(),
                        addToDefinition(definitionTree.get(axiom).get(0), definition));
            case CONCEPT:
                if (axiomParameters.get(axiom.getIndex()) instanceof Integer) {
                    return definition.Concept(((Integer) axiomParameters.get(axiom.getIndex())));
                }
                if (axiomParameters.get(axiom.getIndex()) instanceof ConceptProxy) {
                    return definition.Concept(((ConceptProxy) axiomParameters.get(axiom.getIndex())).getConceptSequence());
                }
                ConceptChronology conceptProxy = (ConceptChronology) axiomParameters.get(axiom.getIndex());
                return definition.Concept(conceptProxy.getConceptSequence());
            case ROLE_ALL:
                if (axiomParameters.get(axiom.getIndex()) instanceof Integer) {
                    return definition.AllRole(((Integer) axiomParameters.get(axiom.getIndex())),
                            addToDefinition(definitionTree.get(axiom).get(0), definition));
                }
                if (axiomParameters.get(axiom.getIndex()) instanceof ConceptProxy) {
                    return definition.AllRole(((ConceptProxy) axiomParameters.get(axiom.getIndex())).getNid(),
                            addToDefinition(definitionTree.get(axiom).get(0), definition));
                }
                ConceptChronology roleTypeProxy = (ConceptChronology) axiomParameters.get(axiom.getIndex());
                return definition.AllRole(roleTypeProxy.getNid(),
                        addToDefinition(definitionTree.get(axiom).get(0), definition));
            case ROLE_SOME:
                if (axiomParameters.get(axiom.getIndex()) instanceof Integer) {
                    return definition.SomeRole(((Integer) axiomParameters.get(axiom.getIndex())),
                            addToDefinition(definitionTree.get(axiom).get(0), definition));
                }
                if (axiomParameters.get(axiom.getIndex()) instanceof ConceptProxy) {
                    return definition.SomeRole(((ConceptProxy) axiomParameters.get(axiom.getIndex())).getNid(),
                            addToDefinition(definitionTree.get(axiom).get(0), definition));
                }
                roleTypeProxy = (ConceptChronology) axiomParameters.get(axiom.getIndex());
                return definition.SomeRole(roleTypeProxy.getNid(),
                        addToDefinition(definitionTree.get(axiom).get(0), definition));
            case TEMPLATE:
                Object[] params = (Object[]) axiomParameters.get(axiom.getIndex());
                if (params[0] instanceof Integer) {
                   return definition.Template((Integer) params[0],
                            (Integer) params[1]);
                }
                if (params[0] instanceof ConceptProxy) {
                    ConceptProxy templateConceptProxy = (ConceptProxy) params[0];
                    ConceptProxy assemblageToPopulateTemplateConceptProxy = (ConceptProxy) params[1];
                    return definition.Template(templateConceptProxy.getConceptSequence(),
                            assemblageToPopulateTemplateConceptProxy.getConceptSequence());
                }
                ConceptChronology templateConceptProxy = (ConceptChronology) params[0];
                ConceptChronology assemblageToPopulateTemplateConceptProxy = (ConceptChronology) params[1];
                return definition.Template(templateConceptProxy.getConceptSequence(),
                        assemblageToPopulateTemplateConceptProxy.getConceptSequence());
            case DISJOINT_WITH:
                if (axiomParameters.get(axiom.getIndex()) instanceof Integer) {
                    return definition.DisjointWith(definition.Concept(((Integer) axiomParameters.get(axiom.getIndex()))));
                }
                if (axiomParameters.get(axiom.getIndex()) instanceof ConceptProxy) {
                    return definition.DisjointWith(definition.Concept(((ConceptProxy) axiomParameters.get(axiom.getIndex())).getConceptSequence()));
                }
                ConceptChronology disjointConceptProxy = (ConceptChronology) axiomParameters.get(axiom.getIndex());
                return definition.DisjointWith(definition.Concept(disjointConceptProxy.getConceptSequence()));
            case LITERAL_BOOLEAN:
                boolean booleanLiteral = (Boolean) axiomParameters.get(axiom.getIndex());
                return definition.BooleanLiteral(booleanLiteral);
            case LITERAL_FLOAT:
                float floatLiteral = (Float) axiomParameters.get(axiom.getIndex());
                return definition.FloatLiteral(floatLiteral);
            case LITERAL_INSTANT:
                Instant instantLiteral = (Instant) axiomParameters.get(axiom.getIndex());
                return definition.InstantLiteral(instantLiteral);
            case LITERAL_INTEGER:
                int integerLiteral = (Integer) axiomParameters.get(axiom.getIndex());
                return definition.IntegerLiteral(integerLiteral);
            case LITERAL_STRING:
                String stringLiteral = (String) axiomParameters.get(axiom.getIndex());
                return definition.StringLiteral(stringLiteral);
            case SUBSTITUTION_BOOLEAN:
                SubstitutionFieldSpecification fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.BooleanSubstitution(fieldSpecification);
            case SUBSTITUTION_CONCEPT:
                fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.ConceptSubstitution(fieldSpecification);
            case SUBSTITUTION_FLOAT:
                fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.FloatSubstitution(fieldSpecification);
            case SUBSTITUTION_INSTANT:
                fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.InstantSubstitution(fieldSpecification);
            case SUBSTITUTION_INTEGER:
                fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.IntegerSubstitution(fieldSpecification);
            case SUBSTITUTION_STRING:
                fieldSpecification
                        = (SubstitutionFieldSpecification) axiomParameters.get(axiom.getIndex());
                return definition.StringSubstitution(fieldSpecification);
            default:
                throw new UnsupportedOperationException("Can't handle: " + axiom.getSemantic());

        }
    }

    protected AbstractNode[] getChildren(GenericAxiom axiom, LogicalExpressionOchreImpl definition) {
        List<GenericAxiom> childrenAxioms = definitionTree.get(axiom);
        List<AbstractNode> children = new ArrayList<>(childrenAxioms.size());
        childrenAxioms.forEach((childAxiom) -> children.add(addToDefinition(childAxiom, definition)));
        return children.toArray(new AbstractNode[children.size()]);
    }

    @Override
    public DisjointWith disjointWith(ConceptProxy conceptProxy) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.DISJOINT_WITH, this);
        axiomParameters.put(axiom.getIndex(), conceptProxy);
        return axiom;
    }

    @Override
    public ConceptAssertion conceptAssertion(ConceptProxy conceptProxy) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.CONCEPT, this);
        axiomParameters.put(axiom.getIndex(), conceptProxy);
        return axiom;
    }

    @Override
     public ConceptAssertion conceptAssertion(Integer conceptNid) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.CONCEPT, this);
        axiomParameters.put(axiom.getIndex(), conceptNid);
        return axiom;
    }

    @Override
    public AllRole allRole(ConceptProxy roleTypeProxy, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_ALL, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), roleTypeProxy);
        return axiom;
    }

    private AllRole allRole(Integer roleTypeNid, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_ALL, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), roleTypeNid);
        return axiom;
    }

    @Override
    public Feature feature(ConceptProxy featureTypeProxy, LiteralAssertion literal) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.FEATURE, this);
        addToDefinitionTree(axiom, literal);
        axiomParameters.put(axiom.getIndex(), featureTypeProxy);
        return axiom;
    }
    private Feature feature(Integer featureTypeNid, LiteralAssertion literal) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.FEATURE, this);
        addToDefinitionTree(axiom, literal);
        axiomParameters.put(axiom.getIndex(), featureTypeNid);
        return axiom;
    }

    @Override
    public SomeRole someRole(ConceptProxy roleTypeProxy, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_SOME, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), roleTypeProxy);
        return axiom;
    }

    private SomeRole someRole(Integer conceptNid, Assertion roleRestriction) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.ROLE_SOME, this);
        addToDefinitionTree(axiom, roleRestriction);
        axiomParameters.put(axiom.getIndex(), conceptNid);
        return axiom;
    }

    @Override
    public Template template(ConceptProxy templateProxy, ConceptProxy assemblageToPopulateTemplateProxy) {
        checkNotBuilt();
        GenericAxiom axiom = new GenericAxiom(NodeSemantic.TEMPLATE, this);
        axiomParameters.put(axiom.getIndex(), new Object[]{templateProxy, assemblageToPopulateTemplateProxy});
        return axiom;
    }

}
