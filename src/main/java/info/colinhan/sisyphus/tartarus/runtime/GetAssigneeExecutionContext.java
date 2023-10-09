package info.colinhan.sisyphus.tartarus.runtime;

import info.colinhan.sisyphus.model.ReferenceType;
import info.colinhan.sisyphus.tartarus.model.*;

import java.util.HashMap;
import java.util.Map;

public class GetAssigneeExecutionContext implements ExecutionContext {
    private final Map<String, String> alias = new HashMap<>();

    public void registerAlias(String variableName, ArraySource source) {
        if (source instanceof ValueSource) {
            this.alias.put(variableName, getUser((ValueSource)source));
        }
    }

    @Override
    public String getUser(ValueSource source) {
        if (source instanceof TemplateString) {
            TemplateString templateString = (TemplateString) source;
            if (templateString.getNodes().size() == 1) {
                return getUser(templateString.getNodes().get(0));
            }
        } else if (source instanceof Reference) {
            Reference reference = (Reference) source;
            String name = reference.toCode();
            if (reference.getType() == ReferenceType.VARIABLE) {
                return this.alias.getOrDefault(reference.getVariableName(), name);
            }
            return name;
        }
        return Constants.ROBOT;
    }

    public void unregisterAlias(String variableName) {
        this.alias.remove(variableName);
    }
}
