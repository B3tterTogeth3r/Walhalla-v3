package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;


public enum Rank {
    ACTIVE_FUX("Fux"),
    ACTIVE("Bursch"),
    PHILISTINES("A-Philister"),
    PHILISTINES_B("B-Philister"),
    ACTIVE_FRIEND("Korporationsfreund Aktiver"),
    PHILISTINES_FRIEND("Korporationsfreund Alter Herr"),
    PHILISTINES_HONOR("Ehrenphilister"),
    HONOR_MEMBER("Ehrenmitglied"),
    GUEST("Gast"),
    GONE("Verstorben"),
    LEFT("Ausgetreten"),
    NONE("not_signed_in"),
    ERROR("Error_while_loading");
    private final String description;

    Rank (String description) {
        this.description = description;
    }

    /**
     * TODO this function is to find the enum based on the {@link #description} of it
     *
     * @param rankName
     *         String value of a name
     * @return the Rank
     */
    public Rank find (@NonNull String rankName) {
        if (rankName.equals(description)) {
            return this;
        } else {
            return ERROR;
        }
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
