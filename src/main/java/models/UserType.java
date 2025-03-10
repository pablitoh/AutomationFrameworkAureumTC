package models;

public enum UserType {
    VALID_USER("valid_user"),
    LOCKED_OUT_USER("locked_out_user"),
    PROBLEM_USER("problem_user"),
    PERFORMANCE_GLITCH_USER("performance_glitch_user"),
    ERROR_USER("error_user"),
    VISUAL_USER("visual_user"),
    INVALID_USER("invalid_user");

    private final String key;

    UserType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}