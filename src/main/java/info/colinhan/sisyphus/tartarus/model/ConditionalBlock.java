package info.colinhan.sisyphus.tartarus.model;

public class ConditionalBlock extends AbstractNode implements Node {
    private Condition condition;
    private Block block;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitConditionalBlock(this);
    }
}
