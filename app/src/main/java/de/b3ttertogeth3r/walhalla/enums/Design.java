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
    TABLE_TITLE("table_title"),
    TABLE_ROW("table_row"),
    LIST_CHECKED("list_checked"),
    LIST_BULLET("list_bullet"),
    BLOCK("block");
    private final String description;

    Design (String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
