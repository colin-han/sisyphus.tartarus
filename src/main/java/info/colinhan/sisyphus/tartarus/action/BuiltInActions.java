package info.colinhan.sisyphus.tartarus.action;

import info.colinhan.sisyphus.model.VariableTypes;

import java.util.HashMap;
import java.util.Map;

import static info.colinhan.sisyphus.model.VariableTypes.*;

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
        ParameterDefinition byParameter = new ParameterDefinition("by", USER);
        ParameterDefinition atLatestParameter = new ParameterDefinition("atLatest", OPTIONAL(STRING));
        register(new ActionDefinition("nop")
                .allowUnknownNameParameters());
        register(new ActionDefinition("fillForm")
                .defaultParameter("formName")
                .namedParameter(byParameter)
                .namedParameter(atLatestParameter));
        register(new ActionDefinition("confirm")
                .defaultParameter("message")
                .namedParameter(byParameter));
        register(new ActionDefinition("callApi")
                .defaultParameter("url")
                .namedParameter("method", ENUM("get", "post", "put", "delete"), "post")
                .namedParameter("headers", OPTIONAL(STRING))
                .namedParameter("body", OPTIONAL(STRING))
                .namedParameter("auth", OPTIONAL(STRING))
                .namedParameter("onError", ENUM("failed", "paused", "ignored"), "failed"));
    }
}
