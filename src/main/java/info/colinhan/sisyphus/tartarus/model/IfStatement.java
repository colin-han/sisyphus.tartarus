package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

public class IfStatement implements Statement {
    private List<ConditionalBlock> thenBlocks = new ArrayList<>();
    private Block elseBlock;

    public List<ConditionalBlock> getThenBlocks() {
        return thenBlocks;
    }

    public void setThenBlocks(List<ConditionalBlock> thenBlocks) {
        this.thenBlocks = thenBlocks;
    }

    public Block getElseBlock() {
        return elseBlock;
    }

    public void setElseBlock(Block elseBlock) {
        this.elseBlock = elseBlock;
    }

    public void addThenBlock(ConditionalBlock block) {
        this.thenBlocks.add(block);
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitIfStatement(this);
    }

    @Override
    public String getAssignee(ExecutionContext context) {
        for (ConditionalBlock thenBlock : this.thenBlocks) {
            for (Statement statement : thenBlock.getBlock().getStatements()) {
                return statement.getAssignee(context);
            }
        }
        for (Statement statement : this.elseBlock.getStatements()) {
            return statement.getAssignee(context);
        }
        return Constants.ROBOT;
    }
}
