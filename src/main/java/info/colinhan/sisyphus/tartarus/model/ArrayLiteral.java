package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral implements ValueSource {
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

    public void addValue(ValueSource value) {
        this.values.add(value);
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitArrayLiteral(this);
    }
}
