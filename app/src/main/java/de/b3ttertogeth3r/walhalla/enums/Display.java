package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum Java docs</a>
 * @since 1.0
 */
public enum Display {
    EDIT("edit"),
    ADD("add"),
    SAVE("save changes"),
    DELETE("delete document"),
    DETAILS("details of event"),
    SHOW("show details whole document");

    private final String description;

    Display (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
