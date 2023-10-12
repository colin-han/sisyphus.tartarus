package info.colinhan.sisyphus.tartarus.utils;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.ReferenceType;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;
import info.colinhan.sisyphus.tartarus.ModelParseContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MockModelParseContext implements ModelParseContext {
    private final Stack<Map<String, VariableType>> variableTypes = new Stack<>();

    public MockModelParseContext() {
        this.pushFrame();

        this.setVariableType("stakeholders", VariableTypes.ARRAY(VariableTypes.USER));
    }

    @Override
    public boolean hasUser(String username) {
        return false;
    }

    @Override
    public VariableType getVariableType(ReferenceType type, String name) {
        return switch (type) {
            case VARIABLE -> {
                for (int i = this.variableTypes.size() - 1; i >= 0; i--) {
                    Map<String, VariableType> frame = this.variableTypes.get(i);
                    if (frame.containsKey(name)) {
                        yield frame.get(name);
                    }
                }
                yield VariableTypes.STRING;
            }
            case ENV -> VariableTypes.STRING;
            case RULE -> VariableTypes.USER;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public void setVariableType(String variableName, VariableType variableType) {
        this.variableTypes.peek().put(variableName, variableType);
    }

    @Override
    public void pushFrame() {
        this.variableTypes.push(new HashMap<>());
    }

    @Override
    public void popFrame() {
        this.variableTypes.pop();
    }
}
