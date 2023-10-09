package info.colinhan.sisyphus.tartarus.action;

import info.colinhan.sisyphus.model.VariableTypes;

import java.util.HashMap;
import java.util.Map;

public final class BuiltInActions {
    private BuiltInActions() {}

    private static final Map<String, ActionDefinition> actions = new HashMap<>();

    public static void register(ActionDefinition definition) {
        actions.put(definition.getName(), definition);
    }

    public static ActionDefinition get(String name) {
        return actions.get(name);
    }

    static {
        register(new ActionDefinition("showForm")
                .defaultParameter("formName")
                .namedParameter("by", VariableTypes.USER));
        register(new ActionDefinition("confirm")
                .defaultParameter("message")
                .namedParameter("by", VariableTypes.USER));
        register(new ActionDefinition("callApi")
                .defaultParameter("url")
                .namedParameter("method", VariableTypes.ENUM("get", "post", "put", "delete"), "post")
                .namedParameter("body", VariableTypes.OPTIONAL(VariableTypes.STRING)));
    }
}
