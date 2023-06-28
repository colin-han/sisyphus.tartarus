package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

public interface ValueSource extends Node {
    Object getValue(ExecutionContext context);
}
