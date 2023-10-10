package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.action.ActionDefinition;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.*;

public class Action extends AbstractStatement implements Statement {
    private String id;
    private final String name;
    private final ActionDefinition definition;
    private final Map<String, ValueSource> parameters = new HashMap<>();

    public Action(ActionDefinition definition, String name) {
        this.definition = definition;
        this.name = name;
    }

    public boolean hasParameter(String parameterName) {
        return this.getParameters().containsKey(parameterName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ActionDefinition getDefinition() {
        return definition;
    }

    public Map<String, ValueSource> getParameters() {
        return parameters;
    }

    public void setParameter(String parameterName, ValueSource value) {
        this.parameters.put(parameterName, value);
    }

    public ValueSource getParameter(String parameterName) {
        return this.parameters.get(parameterName);
    }

    public Map<String, ValueSource> getNamedParameters() {
        Map<String, ValueSource> namedParameters = new HashMap<>();
        this.parameters.forEach((name, value) -> {
            if (!name.equals(this.definition.getDefaultParameter().getName())) {
                namedParameters.put(name, value);
            }
        });
        return namedParameters;
    }

    public ValueSource getPositionedParameter() {
        return this.getParameter(this.definition.getDefaultParameter().getName());
    }

    @Override
    public String getAssignee(ExecutionContext context) {
        if (this.parameters.containsKey("by")) {
            return context.getUser(this.parameters.get("by"));
        } else {
            return Constants.ROBOT;
        }
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitAction(this);
    }
}
