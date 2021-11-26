package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

public enum Pictures {
    CAMERA(0),
    GALLERY(1),
    WEB(2);
    final int number;
    Pictures(int number){
        this.number = number;
    }

    public int value () {
        return number;
    }

    @NonNull
    @Override
    public String toString () {
        return "Pictures";
    }
}
