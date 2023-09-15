package info.colinhan.sisyphus.tartarus.model;

import java.util.List;

public class AndOrCondition extends AbstractNode implements Condition {
    private final AndOr operator;
    private List<Condition> conditions;

    public AndOrCondition(AndOr operator) {
        this.operator = operator;
    }

    public AndOr getOperator() {
        return operator;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitAndOrCondition(this);
    }
}
