package de.b3ttertogeth3r.walhalla.exceptions;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class PersonException extends Exception {
    private static final String TAG = "PersonException";

    public PersonException (String message) {
        Crashlytics.log(TAG, message);
    }

    public PersonException (String message, Throwable cause) {
        Crashlytics.log(TAG, message, getCause());
    }

    public PersonException (Throwable cause) {
        Crashlytics.log(TAG, getCause());
    }
}
