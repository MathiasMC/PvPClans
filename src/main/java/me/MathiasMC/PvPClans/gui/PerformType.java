package me.MathiasMC.PvPClans.gui;

public enum PerformType {
    GUI("[gui]"),
    CONFIRM("[confirm]"),
    CLOSE("[close]"),
    COINS("[coins]"),
    CANCEL("[cancel]"),
    SHARE_COINS("[share_coins]"),
    PAGE("[page]"),
    COMMAND("[command]");

    private final String identifier;

    PerformType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static PerformType get(String s) {
        for (PerformType perform : values()) {
            if (s.startsWith(perform.identifier)) {
                return perform;
            }
        }
        return null;
    }
}
