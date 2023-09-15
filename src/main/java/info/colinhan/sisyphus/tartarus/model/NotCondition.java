package info.colinhan.sisyphus.tartarus.model;

public class NotCondition extends AbstractNode implements Condition {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitNotCondition(this);
    }
}
