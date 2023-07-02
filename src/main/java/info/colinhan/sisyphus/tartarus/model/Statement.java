package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

public interface Statement extends Node {
    String getAssignee(ExecutionContext context);
}
