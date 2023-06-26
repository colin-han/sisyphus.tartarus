package info.colinhan.sisyphus.model;

public class NotCondition implements Condition {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
