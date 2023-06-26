package info.colinhan.sisyphus.model;

public class AndCondition implements Condition {
    private Condition left;
    private Condition right;

    public Condition getLeft() {
        return left;
    }

    public void setLeft(Condition left) {
        this.left = left;
    }

    public Condition getRight() {
        return right;
    }

    public void setRight(Condition right) {
        this.right = right;
    }
}
