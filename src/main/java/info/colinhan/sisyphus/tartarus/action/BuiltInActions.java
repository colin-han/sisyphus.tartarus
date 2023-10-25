package info.colinhan.sisyphus.tartarus.action;

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

    public static final String ACTION_NAME_NOP = "nop";

    public static final String ACTION_NAME_FILL_FORM = "fillForm";

    public static final String ACTION_NAME_CONFIRM = "confirm";

    public static final String ACTION_NAME_CALL_API = "callApi";

    public static final String PARAM_NAME_BY = "by";

    public static final String PARAM_NAME_AT_LATEST = "atLatest";

    public static final String PARAM_NAME_FORM_NAME = "formName";

    public static final String PARAM_NAME_MESSAGE = "message";

    public static final String PARAM_NAME_URL = "url";

    public static final String PARAM_NAME_METHOD = "method";

    public static final String PARAM_NAME_HEADERS = "headers";

    public static final String PARAM_NAME_BODY = "body";

    public static final String PARAM_NAME_AUTH = "auth";

    public static final String PARAM_NAME_ON_ERROR = "onError";

    public static final ActionDefinition ACTION_NOP = new ActionDefinition(ACTION_NAME_NOP)
            .allowUnknownNameParameters();

    public static final ParameterDefinition PARAM_BY = new ParameterDefinition(PARAM_NAME_BY, USER);

    public static final ParameterDefinition PARAM_AT_LATEST = new ParameterDefinition(PARAM_NAME_AT_LATEST, OPTIONAL(STRING));

    public static final ActionDefinition ACTION_FILL_FORM = new ActionDefinition(ACTION_NAME_FILL_FORM)
            .defaultParameter(PARAM_NAME_FORM_NAME)
            .namedParameter(PARAM_BY)
            .namedParameter(PARAM_AT_LATEST);

    public static final ActionDefinition ACTION_CONFIRM = new ActionDefinition(ACTION_NAME_CONFIRM)
            .defaultParameter(PARAM_NAME_MESSAGE)
            .namedParameter(PARAM_BY);

    public static final ActionDefinition PARAM_CALL_API = new ActionDefinition(ACTION_NAME_CALL_API)
            .defaultParameter(PARAM_NAME_URL)
            .namedParameter(PARAM_NAME_METHOD, ENUM("get", "post", "put", "delete"), "post")
            .namedParameter(PARAM_NAME_HEADERS, OPTIONAL(STRING))
            .namedParameter(PARAM_NAME_BODY, OPTIONAL(STRING))
            .namedParameter(PARAM_NAME_AUTH, OPTIONAL(STRING))
            .namedParameter(PARAM_NAME_ON_ERROR, ENUM("failed", "paused", "ignored"), "failed");

    static {
        register(ACTION_NOP);
        register(ACTION_FILL_FORM);
        register(ACTION_CONFIRM);
        register(PARAM_CALL_API);
    }
}
