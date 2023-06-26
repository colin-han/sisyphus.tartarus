package info.colinhan.sisyphus.model;

import info.colinhan.sisyphus.runtime.ExecutionContext;

import java.util.List;

public interface ArraySource extends Node {
    List<ValueSource> getArray(ExecutionContext context);
}
