package info.colinhan.sisyphus.model;

import java.util.HashMap;
import java.util.Map;

public class Action implements Statement {
    private String id;
    private String name;
    private TemplateString positionedParameter;
    private Map<String, TemplateString> namedParameters = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TemplateString getPositionedParameter() {
        return positionedParameter;
    }

    public void setPositionedParameter(TemplateString positionedParameter) {
        this.positionedParameter = positionedParameter;
    }

    public Map<String, TemplateString> getNamedParameters() {
        return namedParameters;
    }

    public void setNamedParameter(String parameterName, TemplateString value) {
        this.namedParameters.put(parameterName, value);
    }
}
