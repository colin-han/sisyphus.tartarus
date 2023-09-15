package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

public class WhileStatement extends AbstractStatement implements Statement {
    private final boolean parallel;
    private String variableName;
    private ArraySource arraySource;
    private Block block;

    public WhileStatement(boolean parallel) {
        this.parallel = parallel;
    }

    public boolean isParallel() {
        return parallel;
    }

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

    @Override
    public String getAssignee(ExecutionContext context) {
        for (Statement statement : this.block.getStatements()) {
            return statement.getAssignee(context);
        }
        return Constants.ROBOT;
    }
}
