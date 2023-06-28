package info.colinhan.sisyphus.tartarus.model;

public enum Comparison {
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">=");

    private final String value;
    Comparison(String value) {
        this.value = value;
    }

    public static Comparison fromValue(String value) {
        for (Comparison comparison : Comparison.values()) {
            if (comparison.value.equals(value)) {
                return comparison;
            }
        }
        throw new IllegalArgumentException("No comparison with value " + value);
    }


    @Override
    public String toString() {
        return this.value;
    }
}
