package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;

public interface ModelParseContext extends VariableValidationContext {
    void pushFrame();

    void popFrame();

    void setVariableType(String variableName, VariableType variableType);
}
