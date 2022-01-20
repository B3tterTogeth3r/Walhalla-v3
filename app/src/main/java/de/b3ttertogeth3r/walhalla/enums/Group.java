package de.b3ttertogeth3r.walhalla.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum Group {
    PUBLIC("Öffentlich"),
    ACTIVE("Aktive"),
    PHILISTINES("Philister"),
    NONE("Niemand"),
    DRAFT("Entwurf");

    final String description;

    Group (String description) {
        this.description = description;
    }

    public static CharSequence[] getList () {
        return new CharSequence[]{PUBLIC.description, ACTIVE.description,
                PHILISTINES.description, NONE.description, DRAFT.description};
    }
}
