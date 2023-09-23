package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.*;

import java.util.stream.Collectors;

public class TFormalExpressionTransformer extends AbstractModelVisitor<String> {
    @Override
    public String visitReference(Reference reference) {
        switch (reference.getType()) {
            case VARIABLE:
                return reference.getVariableName();
            case RULE:
                return "$.rules." + reference.getVariableName();
            case ENV:
                return "$.env." + reference.getVariableName();
            default:
                throw new RuntimeException("Unknown reference type!");
        }
    }

    @Override
    public String visitLiteral(Literal literal) {
        return "\"" + literal.getValue() + "\"";
    }

    @Override
    public String visitTemplateString(TemplateString templateString) {
        return templateString.getNodes().stream().map(this::visit).collect(Collectors.joining(" + "));
    }

    @Override
    public String visitAndOrCondition(AndOrCondition andOrCondition) {
        return "(" +
                andOrCondition.getConditions().stream()
                        .map(this::visit)
                        .collect(Collectors.joining(") " + andOrCondition.getOperator().toString() + " (")) +
                ")";
    }

    @Override
    public String visitNotCondition(NotCondition notCondition) {
        return "!(" + this.visit(notCondition.getCondition()) + ")";
    }

    @Override
    public String visitCompareCondition(CompareCondition compareCondition) {
        return "(" + this.visit(compareCondition.getLeft()) + ") " +
                compareCondition.getComparison().toBPMNExprString() +
                " (" + this.visit(compareCondition.getRight()) + ")";
    }
}
