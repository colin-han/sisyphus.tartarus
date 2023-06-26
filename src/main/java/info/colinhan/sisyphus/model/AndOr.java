package info.colinhan.sisyphus.model;

public enum AndOr {
    AND("and", "&&"),
    OR("or", "||");

    private String name;
    private String alias;

    AndOr(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public static AndOr fromString(String value) {
        for (AndOr andOr : AndOr.values()) {
            if (andOr.name.equals(value) || andOr.alias.equals(value)) {
                return andOr;
            }
        }
        return null;
    }
}
