package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

public enum Design {
    TEXT("text"),
    TITLE("title"),
    SUBTITLE1("subtitle1"),
    SUBTITLE2("subtitle2"),
    BUTTON("button"),
    IMAGE("image"),
    LINK("link"),
    TABLE("table"),
    LIST_CHECKED("list_checked"),
    LIST_BULLET("list_bullet"),
    LIST_CHECKABLE("list_checkable");
    private final String description;

    Design (String description) {
        this.description = description;
    }


    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
