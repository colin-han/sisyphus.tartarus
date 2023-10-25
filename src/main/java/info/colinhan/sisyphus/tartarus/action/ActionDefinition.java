package info.colinhan.sisyphus.tartarus.action;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;
import info.colinhan.sisyphus.exception.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ActionDefinition {
    private final String name;
    private final List<ParameterDefinition> namedParameters = new ArrayList<>();
    private ParameterDefinition defaultParameter = new ParameterDefinition("default", VariableTypes.STRING, null);
    private boolean allowUknownNamedParameters = false;

    public ActionDefinition(String name) {
        this.name = name;
    }

    public ActionDefinition(String name, List<ParameterDefinition> namedParameters) {
        this.name = name;
        this.namedParameters.addAll(namedParameters);
    }

    public String getName() {
        return name;
    }

    public List<ParameterDefinition> getNamedParameters() {
        return namedParameters;
    }

    public ParameterDefinition getDefaultParameter() {
        return defaultParameter;
    }


    public ActionDefinition defaultParameter(String name, VariableType type) {
        return defaultParameter(name, type, null);
    }

    public ActionDefinition defaultParameter(String name) {
        return defaultParameter(name, VariableTypes.OPTIONAL(VariableTypes.STRING), null);
    }

    public ActionDefinition defaultParameter(String name, VariableType type, Object defaultValue) {
        this.defaultParameter = new ParameterDefinition(name, type, defaultValue);
        return this;
    }

    public ActionDefinition namedParameter(String name, VariableType type) {
        return namedParameter(name, type, null);
    }

    public ActionDefinition namedParameter(String name, VariableType type, Object defaultValue) {
        return this.namedParameter(new ParameterDefinition(name, type, defaultValue));
    }

    public ActionDefinition namedParameter(ParameterDefinition namedParameter) {
        this.namedParameters.add(namedParameter);
        return this;
    }

    public ActionDefinition allowUnknownNameParameters() {
        this.allowUknownNamedParameters = true;
        return this;
    }

    public void validate(VariableType positionedParameterValueType,
                         Map<String, VariableType> namedParameterTypes,
                         VariableValidationContext context) throws ParseException {
        if (positionedParameterValueType == null && this.defaultParameter.isRequired()) {
            throw new ParseException("Missing default parameter!");
        }
        String result = this.defaultParameter.getType().validate(context, positionedParameterValueType);
        if (result != null) {
            throw new ParseException("Invalid default parameter: " + result);
        }

        Set<String> namedParameterNames = namedParameterTypes.keySet();
        for (ParameterDefinition namedParameter : this.namedParameters) {
            String theName = namedParameter.getName();
            VariableType type = namedParameterTypes.get(theName);
            namedParameterNames.remove(theName);
            if (type == null) {
                if (namedParameter.isRequired()) {
                    throw new ParseException("Missing named parameter: " + theName);
                } else {
                    continue;
                }
            }

            result = namedParameter.getType().validate(context, type);
            if (result != null) {
                throw new ParseException("Invalid named parameter for \"" + theName + "\": " + result);
            }
        }

        if (!namedParameterNames.isEmpty()) {
            throw new ParseException("Unknown named parameter(s): " + namedParameterNames);
        }
    }
}
