package info.colinhan.sisyphus.tartarus.model;

public class Flow extends AbstractNode implements Node {
    private Block block;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitFlow(this);
    }
}
