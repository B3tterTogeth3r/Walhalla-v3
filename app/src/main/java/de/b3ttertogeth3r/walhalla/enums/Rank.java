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
    /** german string description of the string*/
    private final String description;

    Rank (String description) {
        this.description = description;
    }

    /**
     * this function is to find the enum based on the {@link #description} of it
     *
     * @param rankName
     *         String value of a name
     * @return the Rank
     */
    public static Rank find (String rankName) {
        if (rankName == null || rankName.isEmpty()){
            return ERROR;
        } else if (ACTIVE_FUX.getDescription().equals(rankName)) {
            return ACTIVE_FUX;
        } else if (ACTIVE.getDescription().equals(rankName)) {
            return ACTIVE;
        } else if (PHILISTINES.getDescription().equals(rankName)) {
            return PHILISTINES;
        } else if (PHILISTINES_B.getDescription().equals(rankName)) {
            return PHILISTINES_B;
        } else if (ACTIVE_FRIEND.getDescription().equals(rankName)) {
            return ACTIVE_FRIEND;
        } else if (PHILISTINES_FRIEND.getDescription().equals(rankName)) {
            return PHILISTINES_FRIEND;
        } else if (PHILISTINES_HONOR.getDescription().equals(rankName)) {
            return PHILISTINES_HONOR;
        } else if (HONOR_MEMBER.getDescription().equals(rankName)) {
            return HONOR_MEMBER;
        } else if (GUEST.getDescription().equals(rankName)) {
            return GUEST;
        } else if (GONE.getDescription().equals(rankName)) {
            return GONE;
        } else if (LEFT.getDescription().equals(rankName)) {
            return LEFT;
        } else if (NONE.getDescription().equals(rankName)) {
            return NONE;
        } else if (ERROR.getDescription().equals(rankName)) {
            return ERROR;
        } else {
            return NONE;
        }
    }

    public String getDescription () {
        return description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
