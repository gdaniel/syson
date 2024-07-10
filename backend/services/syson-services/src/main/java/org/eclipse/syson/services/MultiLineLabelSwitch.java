/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.services;

import java.util.Objects;

import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch returning the label of some container nodes in the diagrams.
 *
 * @author arichard
 */
public class MultiLineLabelSwitch extends SysmlSwitch<String> {

    private final LabelService labelService;

    public MultiLineLabelSwitch(LabelService labelService) {
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public String caseElement(Element object) {
        String declaredName = object.getDeclaredName();
        if (declaredName == null) {
            return "";
        }
        return declaredName;
    }

    @Override
    public String caseAcceptActionUsage(AcceptActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("accept action")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(LabelConstants.CR)
                .append(this.acceptActionUsagePayloadDetails(object))
                .append(this.acceptActionUsageReceiverDetails(object));
        return label.toString();
    }

    @Override
    public String caseActionDefinition(ActionDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("action def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseActionUsage(ActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("action")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseAllocationDefinition(AllocationDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("allocation def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseAllocationUsage(AllocationUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("allocation")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("attribute def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("attribute")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseAssignmentActionUsage(AssignmentActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("assign")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.assignmentActionUsageDetails(object));
        return label.toString();
    }

    @Override
    public String caseConstraintDefinition(ConstraintDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("constraint def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("constraint")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("enumeration def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("interface def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("interface")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("item def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("item")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseMetadataDefinition(MetadataDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("metadata def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseOccurrenceDefinition(OccurrenceDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(this.individual(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("occurrence def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(this.individual(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("occurrence")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String casePackage(Package object) {
        StringBuilder label = new StringBuilder();
        label
                .append(LabelConstants.OPEN_QUOTE)
                .append("package")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object));
        return label.toString();
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("part def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String casePartUsage(PartUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("part")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.getPerformActionUsageTag(object))
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(object.getPerformedAction().getName());
        return label.toString();
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("port def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String casePortUsage(PortUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("port")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("requirement def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseRequirementUsage(RequirementUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("requirement")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseUseCaseDefinition(UseCaseDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("use case def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseUseCaseUsage(UseCaseUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("use case")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    @Override
    public String caseStateDefinition(StateDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(this.getIsParallel(object.isIsParallel()))
                .append(LabelConstants.OPEN_QUOTE)
                .append("state def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseStateUsage(StateUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.abstractType(object))
                .append(this.getIsParallel(object.isIsParallel()))
                .append(LabelConstants.OPEN_QUOTE)
                .append("state")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getSubsettingLabel(object));
        return label.toString();
    }

    /**
     * Builds the string value for Parallel StateUsage or StateDefinition.
     *
     * @param isParallel
     *            Whether the State is parallel or not.
     */
    private StringBuilder getIsParallel(boolean isParallel) {
        StringBuilder parallel = new StringBuilder();
        if (isParallel) {
            parallel
                    .append(LabelConstants.OPEN_QUOTE)
                    .append("parallel")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return parallel;
    }

    private String abstractType(Type type) {
        StringBuilder label = new StringBuilder();
        if (type.isIsAbstract()) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append("abstract")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String multiplicityRange(Usage usage) {
        StringBuilder label = new StringBuilder();
        var optMultiplicityRange = usage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        if (optMultiplicityRange.isPresent()) {
            var range = optMultiplicityRange.get();
            String firstBound = null;
            String secondBound = null;
            var bounds = range.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
                    .flatMap(m -> m.getOwnedRelatedElement().stream())
                    .filter(LiteralExpression.class::isInstance)
                    .map(LiteralExpression.class::cast)
                    .toList();
            if (bounds.size() == 1) {
                firstBound = this.labelService.getValue(bounds.get(0));
            } else if (bounds.size() == 2) {
                firstBound = this.labelService.getValue(bounds.get(0));
                secondBound = this.labelService.getValue(bounds.get(1));
            }
            label.append(LabelConstants.OPEN_BRACKET);
            if (firstBound != null) {
                label.append(firstBound);
            }
            if (secondBound != null) {
                label.append("..");
                label.append(secondBound);
            }
            label.append(LabelConstants.CLOSE_BRACKET);
            label.append(LabelConstants.SPACE);
        }
        return label.toString();
    }

    private String individual(OccurrenceDefinition occurrenceDefinition) {
        return this.individual(occurrenceDefinition.isIsIndividual());
    }

    private String individual(OccurrenceUsage occurrenceUsage) {
        return this.individual(occurrenceUsage.isIsIndividual());
    }

    private String individual(boolean isIndividual) {
        StringBuilder label = new StringBuilder();
        if (isIndividual) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append("individual")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String assignmentActionUsageDetails(AssignmentActionUsage aau) {
        StringBuilder label = new StringBuilder();
        if (aau.getReferent() != null) {
            label
                    .append(aau.getReferent().getName())
                    .append(LabelConstants.SPACE)
                    .append(":=")
                    .append(LabelConstants.SPACE)
                    .append(this.getAssignmentValue(aau.getValueExpression()));
        }
        return label.toString();
    }

    private String getAssignmentValue(Expression expression) {
        StringBuilder label = new StringBuilder();
        if (expression != null) {
            // At the moment, we do not have the service to retrieve the textual representation of an
            // expression. This code should be enhanced to cover all kind of expression.
            var value = this.labelService.getValue(expression);
            if (value != null) {
                label.append(value);
            }
        }
        return label.toString();
    }

    private String getPerformActionUsageTag(PerformActionUsage pau) {
        if (Objects.equals(pau.getPerformedAction(), pau)) {
            return "perform action";
        }
        return "perform";
    }

    private String acceptActionUsagePayloadDetails(AcceptActionUsage aau) {
        StringBuilder label = new StringBuilder();
        this.addAcceptActionUsagePayloadParameter(label, aau);
        this.addAcceptActionUsagePayloadArgument(label, aau);
        return label.toString();
    }

    private void addAcceptActionUsagePayloadParameter(StringBuilder label, AcceptActionUsage aau) {
        var payloadParameter = aau.getPayloadParameter();
        if (payloadParameter != null) {
            var payloadName = payloadParameter.getDeclaredName();
            var payloadTypeName = payloadParameter.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .map(ft -> ft.getType())
                    .filter(type -> type != null)
                    .map(t -> t.getDeclaredName())
                    .findFirst()
                    .orElse(null);

            if (payloadTypeName != null) {
                label
                        .append(LabelConstants.OPEN_QUOTE)
                        .append("accept")
                        .append(LabelConstants.CLOSE_QUOTE);
                if (payloadName != null) {
                    label
                            .append(LabelConstants.SPACE)
                            .append(payloadName);
                }
                if (payloadName != null) {
                    label.append(LabelConstants.COLON);
                }
                label
                        .append(LabelConstants.SPACE)
                        .append(payloadTypeName);
                label.append(LabelConstants.CR);
            }
        }
    }

    private void addAcceptActionUsagePayloadArgument(StringBuilder label, AcceptActionUsage aau) {
        var payloadArgument = aau.getPayloadArgument();
        if (payloadArgument instanceof TriggerInvocationExpression tie) {
            Expression expression = tie.getOwnedRelationship().stream()
                    .filter(FeatureMembership.class::isInstance)
                    .map(FeatureMembership.class::cast)
                    .flatMap(pm -> pm.getOwnedRelatedElement().stream())
                    .filter(Expression.class::isInstance)
                    .map(Expression.class::cast)
                    .findFirst()
                    .orElse(null);
            if (expression != null && expression.getDeclaredName() != null) {
                label
                        .append(tie.getKind().toString())
                        .append(LabelConstants.SPACE)
                        // At the moment, we do not have the service to retrieve the textual representation of an
                        // expression.
                        .append(expression.getDeclaredName());
            }
        }
    }

    private String acceptActionUsageReceiverDetails(AcceptActionUsage aau) {
        StringBuilder label = new StringBuilder();
        Expression receiverArgument = aau.getReceiverArgument();
        if (receiverArgument instanceof FeatureReferenceExpression fre) {
            var referent = fre.getReferent();
            if (referent != null) {
                var referentName = referent.getName();
                if (referentName != null) {
                    label
                            .append(LabelConstants.OPEN_QUOTE)
                            .append("via")
                            .append(LabelConstants.CLOSE_QUOTE)
                            .append(LabelConstants.SPACE)
                            .append(referentName)
                            .append(LabelConstants.CR);
                }
            }
        }
        return label.toString();
    }
}
