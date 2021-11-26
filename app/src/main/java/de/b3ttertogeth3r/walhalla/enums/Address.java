package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;

public enum Address {
    CITY("City"),
    ZIP("ZIP"),
    STREET("Street"),
    NUMBER("Number");
    private final String description;

    Address (String description) {
        this.description = description;
    }


    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
