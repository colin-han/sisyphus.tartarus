package info.colinhan.sisyphus.model;

import java.util.List;

public class AndOrCondition implements Condition {
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
}
