package az.projectdailyreport.projectdailyreport.model;
public enum UserRole {
    ROLE_SUPERADMIN("SuperAdmin"),
    ROLE_ADMIN("Admin"),
    ROLE_HEAD("Head"),
    ROLE_USER("User");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

