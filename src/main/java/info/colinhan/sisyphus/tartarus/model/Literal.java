package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

public class Literal extends AbstractNode implements TemplateNode, ValueSource {
    private VariableType type;
    private String value;

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object getValue(ExecutionContext context) {
        return null;
    }

    @Override
    public VariableType getValueType(VariableValidationContext context) {
        return this.type;
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public String toCode() {
        return this.value;
    }
}
