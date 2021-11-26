package de.b3ttertogeth3r.walhalla.exceptions;

import de.b3ttertogeth3r.walhalla.firebase.Firebase;

public class PersonException extends Exception {
    private static final String TAG = "PersonException";

    public PersonException (String message) {
        Firebase.Crashlytics.log(TAG, message);
    }

    public PersonException (String message, Throwable cause) {
        Firebase.Crashlytics.log(TAG, message, getCause());
    }

    public PersonException (Throwable cause) {
        Firebase.Crashlytics.log(TAG, getCause());
    }
}
