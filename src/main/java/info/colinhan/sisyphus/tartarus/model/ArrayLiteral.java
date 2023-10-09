package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral extends AbstractNode implements ValueSource {
    private List<ValueSource> values = new ArrayList<>();

    public List<ValueSource> getValues() {
        return values;
    }

    public void setValues(List<ValueSource> values) {
        this.values = values;
    }

    @Override
    public Object getValue(ExecutionContext context) {
        // TODO:
        return null;
    }

    @Override
    public VariableType getValueType(VariableValidationContext context) {
        VariableType type = VariableTypes.UNKNOWN;
        for (ValueSource item : this.values) {
            VariableType itemType = item.getValueType(context);
            if (type == VariableTypes.UNKNOWN) {
                type = itemType;
            } else if (type != itemType) {
                type = VariableTypes.ANY;
            }
        }
        return VariableTypes.ARRAY(type);
    }

    public void addValue(ValueSource value) {
        this.values.add(value);
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitArrayLiteral(this);
    }
}
