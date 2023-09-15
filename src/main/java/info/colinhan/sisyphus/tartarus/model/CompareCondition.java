package info.colinhan.sisyphus.tartarus.model;

public class CompareCondition extends AbstractNode implements Condition {
    private ValueSource left;
    private Comparison comparison;
    private ValueSource right;

    public ValueSource getLeft() {
        return left;
    }

    public void setLeft(ValueSource left) {
        this.left = left;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    public ValueSource getRight() {
        return right;
    }

    public void setRight(ValueSource right) {
        this.right = right;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitCompareCondition(this);
    }
}
