package info.colinhan.sisyphus.tartarus.model;

public enum ReferenceType {
    VARIABLE("$"),
    RULE("@"),
    ENV("#");

    private String value;
    ReferenceType(String value) {
        this.value = value;
    }

    public static ReferenceType fromValue(String value) {
        for (ReferenceType type : ReferenceType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
