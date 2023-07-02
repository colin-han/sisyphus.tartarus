package info.colinhan.sisyphus.tartarus.runtime;

import info.colinhan.sisyphus.tartarus.model.ValueSource;

public interface ExecutionContext {
    String getUser(ValueSource source);
}
