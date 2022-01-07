package de.b3ttertogeth3r.walhalla.exceptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class InvalidFirestorePathException extends Exception {
    private static final String TAG = "InvalidFirestorePathException";
    private Throwable throwable = null;
    private String message = "";

    public InvalidFirestorePathException(){
        super();
        this.message = "exception thrown";
        Crashlytics.log(TAG, message);
    }

    protected InvalidFirestorePathException(String string){
        Crashlytics.log(TAG, string);
    }

    public InvalidFirestorePathException(DocumentReference ref){
        if(ref == null){
            message = "DocumentReference is null";
        }
        else if(ref.getPath().isEmpty()){
            message = "Path is empty";
        }
        Crashlytics.log(TAG, message);
    }

    public InvalidFirestorePathException(CollectionReference ref){
        if(ref == null){
            message = "CollectionReference is null";
        } else if (ref.getPath().isEmpty()) {
            message = "CollectionReference path is empty";
        }
        Crashlytics.log(TAG, message);
    }

    @Nullable
    @Override
    public String getMessage () {
        return message;
    }

    @Nullable
    @Override
    public synchronized Throwable getCause () {
        return throwable;
    }

    @NonNull
    @Override
    public String toString () {
        return "InvalidFirestorePathException";
    }
}
