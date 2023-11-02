package info.colinhan.sisyphus.tartarus.model;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateString extends AbstractNode implements ValueSource {
    private List<TemplateNode> nodes = new ArrayList<>();

    public List<TemplateNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TemplateNode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(TemplateNode node) {
        this.nodes.add(node);
    }

    @Override
    public Object getValue(ExecutionContext context) {
        return this.nodes.stream()
                .map(node -> node.getValue(context).toString())
                .collect(Collectors.joining());
    }

    @Override
    public VariableType getValueType(VariableValidationContext context) {
        if (this.nodes.size() == 1) {
            return this.nodes.get(0).getValueType(context);
        } else if (this.nodes.size() > 1) {
            return VariableTypes.STRING;
        } else {
            return VariableTypes.UNKNOWN;
        }
    }

    @Override
    public <T> T accept(ModelVisitor<? extends T> visitor) {
        return visitor.visitTemplateString(this);
    }

    public String toCode() {
        return this.nodes.stream().map(TemplateNode::toCode).collect(Collectors.joining());
    }
}
