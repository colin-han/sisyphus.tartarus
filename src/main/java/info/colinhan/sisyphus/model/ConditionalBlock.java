package info.colinhan.sisyphus.model;

public class ConditionalBlock implements Node {
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
}
