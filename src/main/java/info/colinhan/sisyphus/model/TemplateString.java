package info.colinhan.sisyphus.model;

import info.colinhan.sisyphus.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

public class TemplateString implements ValueSource {
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
        // TODO:
        return null;
    }
}
