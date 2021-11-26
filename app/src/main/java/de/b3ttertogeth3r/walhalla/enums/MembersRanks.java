package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum Java docs</a>
 * @since 1.0
 */
public enum MembersRanks {
    ACTIVE("active_member"),
    AH("silent_member"),
    GONE("resigned"),
    GUEST("guest");

    private final String description;

    MembersRanks (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
