package info.colinhan.sisyphus.model;

import java.util.List;

public class Flow implements Node {
    private Block block;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
