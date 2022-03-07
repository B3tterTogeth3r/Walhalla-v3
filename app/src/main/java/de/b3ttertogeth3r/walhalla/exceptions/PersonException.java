package de.b3ttertogeth3r.walhalla.exceptions;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.utils.MyLog;

public class PersonException extends Exception {
    private static final String TAG = "PersonException";

    public PersonException (String message) {
        MyLog.i(TAG, message);
    }

    public PersonException (String message, Throwable cause) {
        MyLog.e(TAG, message, cause);
    }

    public PersonException (Throwable cause) {
        MyLog.e(TAG, "exception found", cause);
    }
}
