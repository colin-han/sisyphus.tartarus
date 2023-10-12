package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.List;

public interface ArraySource extends Node {
    List<ValueSource> getArray(ExecutionContext context);
    VariableType getElementType(VariableValidationContext context);
}
