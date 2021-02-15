package by.minilooth.telegrambot.model.enums;

public enum Role {
    CLIENT("user");

    private String deepLink;

    private String getDeepLink() {
        return deepLink;
    }

    Role(String deepLink) {
        this.deepLink = deepLink;
    }

    public static Role parseRole(String input) {
        for (Role role : Role.values()) {
            if (role.getDeepLink().equals(input)) {
                return role;
            }
        }
        return CLIENT;
    }
}
