package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.List;

public interface Statement extends Node {
    String getAssignee(ExecutionContext context);
}
