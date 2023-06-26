package info.colinhan.sisyphus.model;

import info.colinhan.sisyphus.runtime.ExecutionContext;

public class Literal implements TemplateNode, ValueSource {
    private String value;

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
}
