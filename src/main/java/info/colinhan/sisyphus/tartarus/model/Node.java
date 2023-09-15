package info.colinhan.sisyphus.tartarus.model;

import java.util.List;
import java.util.UUID;

public interface Node {
    UUID getUUID();

    <T> T accept(ModelVisitor<? extends T> visitor);
}
