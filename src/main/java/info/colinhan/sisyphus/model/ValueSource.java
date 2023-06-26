package info.colinhan.sisyphus.model;

import info.colinhan.sisyphus.runtime.ExecutionContext;

public interface ValueSource extends Node {
    Object getValue(ExecutionContext context);
}
