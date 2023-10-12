package info.colinhan.sisyphus.tartarus.action;


import info.colinhan.sisyphus.model.VariableType;

public final class ParameterDefinition {
    private final String name;
    private final VariableType type;
    private final Object defaultValue;
    private final boolean required;

    public ParameterDefinition(String name, VariableType type) {
        this(name, type, null);
    }

    public ParameterDefinition(String name, VariableType type, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        String result = type.validate(null, defaultValue);
        if (result != null) {
            this.required = true;
            if (defaultValue != null) {
                throw new IllegalArgumentException("Invalid default value for parameter %s: %s".formatted(name, result));
            }
        } else {
            this.required = false;
        }
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }
}
