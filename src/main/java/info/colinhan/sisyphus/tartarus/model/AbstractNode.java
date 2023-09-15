package info.colinhan.sisyphus.tartarus.model;

import java.util.UUID;

public abstract class AbstractNode implements Node {
    private final UUID uuid;

    public AbstractNode() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
