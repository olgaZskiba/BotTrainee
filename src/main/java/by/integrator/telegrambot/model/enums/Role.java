package by.integrator.telegrambot.model.enums;

public enum Role {
    CLIENT("user"),
    ADMIN("admin");

    private String deepLink;

    private String getDeepLink() {
        return deepLink;
    }

    Role(String deepLink) {
        this.deepLink = deepLink;
    }

    public static Role parseRole(String input) {
        for (Role role : Role.values()) {
            if (input.endsWith(role.getDeepLink())) {
                System.out.println(role.getDeepLink());
                return role;
            }
        }
        return CLIENT;
    }
}
