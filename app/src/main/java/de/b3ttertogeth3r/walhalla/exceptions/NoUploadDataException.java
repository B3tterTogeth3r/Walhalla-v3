package de.b3ttertogeth3r.walhalla.exceptions;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class NoUploadDataException extends Exception{
    private static final String TAG = "NoUploadDataException";
    private final String message = "No upload data was found.";

    public NoUploadDataException () {
        super();
        Crashlytics.error(TAG, message);
    }
}
