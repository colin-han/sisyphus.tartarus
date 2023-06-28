package info.colinhan.sisyphus.tartarus.model;

public class WhileStatement implements Statement {
    private String variableName;
    private ArraySource arraySource;
    private Block block;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public ArraySource getArraySource() {
        return arraySource;
    }

    public void setArraySource(ArraySource arraySource) {
        this.arraySource = arraySource;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitWhileStatement(this);
    }
}
