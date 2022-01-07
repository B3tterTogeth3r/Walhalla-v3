package de.b3ttertogeth3r.walhalla.interfaces;

import android.content.DialogInterface;
import android.content.Intent;

public interface OnDoneListener {

    default void sortResult (int which, Intent intent) {
        if (which == DialogInterface.BUTTON_NEUTRAL){
            neutral(intent);
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            positive(intent);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            negative(intent);
        }
    }

    default void positive (Intent resultIntent) {
    }

    default void negative (Intent resultIntent) {
    }

    default void neutral (Intent resultIntent) {
    }
}
