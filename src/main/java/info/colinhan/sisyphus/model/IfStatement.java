package info.colinhan.sisyphus.model;

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
}
