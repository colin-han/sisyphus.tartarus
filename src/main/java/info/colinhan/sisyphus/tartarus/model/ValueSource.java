package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

public interface ValueSource extends Node {
    Object getValue(ExecutionContext context);
    VariableType getValueType(VariableValidationContext context);
}
