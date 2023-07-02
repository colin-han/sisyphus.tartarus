package info.colinhan.sisyphus.tartarus.runtime;

import info.colinhan.sisyphus.tartarus.model.*;

import java.util.Objects;

public class NormalizeVisitor extends AbstractModelVisitor<Object> {
    @Override
    public Object visitTemplateString(TemplateString templateString) {
        TemplateNode firstNode = templateString.getNodes().get(0);
        TemplateNode lastNode = templateString.getNodes().get(templateString.getNodes().size() - 1);
        if (firstNode instanceof Literal) {
            Literal firstLiteral = (Literal) firstNode;
            firstLiteral.setValue(firstLiteral.getValue().replaceAll("^\\s+", ""));
        }
        if (lastNode instanceof Literal) {
            Literal lastLiteral = (Literal) lastNode;
            lastLiteral.setValue(lastLiteral.getValue().replaceAll("\\s+$", ""));
        }
        if (firstNode != lastNode) {
            if (firstNode instanceof Literal) {
                Literal firstLiteral = (Literal) firstNode;
                if (firstLiteral.getValue().isEmpty()) {
                    templateString.getNodes().remove(0);
                }
            }
            if (lastNode instanceof Literal) {
                Literal lastLiteral = (Literal) lastNode;
                if (lastLiteral.getValue().isEmpty()) {
                    templateString.getNodes().remove(templateString.getNodes().size() - 1);
                }
            }
        }
        return null;
    }
}
