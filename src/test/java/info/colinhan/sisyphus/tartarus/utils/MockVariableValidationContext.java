package info.colinhan.sisyphus.tartarus.utils;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.ReferenceType;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;

public class MockVariableValidationContext implements VariableValidationContext {
    @Override
    public boolean hasUser(String username) {
        return false;
    }

    @Override
    public VariableType getVariableType(ReferenceType type, String name) {
        return switch (type) {
            case VARIABLE, ENV -> VariableTypes.STRING;
            case RULE -> VariableTypes.USER;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
