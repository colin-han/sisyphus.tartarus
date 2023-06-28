package info.colinhan.sisyphus.tartarus.model;

public interface Node {
    <T> T accept(ModelVisitor<? extends T> visitor);
}
