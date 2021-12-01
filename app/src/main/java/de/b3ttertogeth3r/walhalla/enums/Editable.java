package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

public enum Editable {
    NAME("Full name"),
    FIRST_NAME("First name"),
    LAST_NAME("Last name"),
    ADDRESS("Post address"),
    DOB("Date of birth"),
    POB("Place of birth"),
    MOBILE("Mobile number"),
    MAIL("Email address"),
    MAJOR("Major or occupation"),
    JOINED("Joined semester"),
    PICTURE("Picture or just its path"),
    CONNECTED_SERVICES("Don't know yet"),
    EMPTY("No field to edit"),
    STREET("Street"),
    NUMBER("Number"),
    ZIP("Zip"),
    CITY("City"),
    DAY("Day"),
    MONTH("Month"),
    YEAR("Year");
    private final String description;

    Editable (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
