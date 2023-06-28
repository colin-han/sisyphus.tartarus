package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.List;

public interface ArraySource extends Node {
    List<ValueSource> getArray(ExecutionContext context);
}
