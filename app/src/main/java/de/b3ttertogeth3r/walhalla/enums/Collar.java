package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

/**
 * @since 1.0
 * @version 1.0
 * @author B3tterTogeth3r
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum java docs</a>
 */
public enum Collar {
    HO ("suit"),
    O ("shirt"),
    IO ("collar");

    private final String description;
    Collar (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
