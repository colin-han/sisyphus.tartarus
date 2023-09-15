package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.List;

public class Reference extends AbstractNode implements TemplateNode, ValueSource, ArraySource, Condition {
    private ReferenceType type = ReferenceType.VARIABLE;
    private String variableName;
    private String defaultValue;

    public ReferenceType getType() {
        return type;
    }

    public void setType(ReferenceType type) {
        this.type = type;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getValue(ExecutionContext context) {
        // TODO:
        return null;
    }

    @Override
    public List<ValueSource> getArray(ExecutionContext context) {
        return null;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitReference(this);
    }

    @Override
    public String toCode() {
        if (this.defaultValue != null) {
            return String.format("%s{%s:%s}", this.type.getValue(), this.variableName, this.defaultValue);
        } else {
            return this.type.getValue() + this.variableName;
        }
    }
}
