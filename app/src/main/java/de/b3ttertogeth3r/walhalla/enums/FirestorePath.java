package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore;

/**
 * This class is to make all paths to the data inside the Firestore Database formatted the same and
 * to remove typing errors.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore CustomFirestore
 * @since 1.0
 */
public enum FirestorePath {
    ROOMS("rooms", Firestore.getSites("rooms")),
    ABOUT_US("about us", Firestore.getSites("about_us")),
    HISTORY_OWN("own history", Firestore.getSites("history_own")),
    FRATERNITY_WUERZBURG("frat history in würzburg", Firestore.getSites("fraternity_wuerzburg"
    )),
    FRATERNITY_GERMANY("frat history in germany", Firestore.getSites("fraternity_germany")),
    IMAGES("images", Firestore.getImages()),
    EVENT("event");
    private final String name;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;

    FirestorePath (String name) {
        this.name = name;
    }

    FirestorePath (String name, CollectionReference collectionReference) {
        this.name = name;
        this.collectionReference = collectionReference;
    }

    FirestorePath (String name, DocumentReference reference) {
        this.name = name;
        this.documentReference = reference;
    }

    @NonNull
    public DocumentReference getEvent (int semesterID, String eventID) throws IllegalArgumentException {
        if (eventID == null || eventID.isEmpty()) {
            throw new IllegalArgumentException("Event id cannot be empty");
        }
        return Firestore.getSemester(semesterID).collection("event").document(eventID);
    }

    public CollectionReference getCollection () throws NullPointerException {
        if (collectionReference == null) {
            throw new NullPointerException("Object is a DocumentReference");
        }
        return collectionReference;
    }

    public DocumentReference getDocument () throws NullPointerException {
        if (documentReference == null) {
            throw new NullPointerException("Object is a CollectionReference");
        }
        return documentReference;
    }

    @NonNull
    @Override
    public String toString () {
        return name;
    }
}
