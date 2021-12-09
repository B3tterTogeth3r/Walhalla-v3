package de.b3ttertogeth3r.walhalla.interfaces;

import android.net.Uri;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.b3ttertogeth3r.walhalla.models.Image;

/**
 * A custom listener to make a notification on firebase download results more easy.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see
 * <a href="https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data">Wait
 * on Firebase Result</a> Answer on GitHub I kindly used.
 * @since 1.1
 */
public interface OnGetDataListener {
    String TAG = "OnGetDataListener";

    /**
     * notify listener, the download was successful and send the data
     *
     * @param documentSnapshot
     *         the downloaded data
     */
    default void onSuccess (DocumentSnapshot documentSnapshot) {
    }

    /**
     * notify listener, the download was successful and send the data
     *
     * @param queryDocumentSnapshot
     *         list of downloaded data
     */
    default void onSuccess (QueryDocumentSnapshot queryDocumentSnapshot) {
    }

    default void onSuccess(QuerySnapshot querySnapshot){
    }

    /**
     * returns the image uri of the uploaded image
     *
     * @param imageUri
     *         link to the image
     */
    default void onSuccess (Uri imageUri) {
    }

    /**
     * returns a string value through the listener.
     *
     * @param string
     *         text value
     */
    default void onSuccess (String string) {
    }

    /**
     * returns an image object through the listener.
     *
     * @param image
     *          image object
     */
    default void onSuccess (Image image) {
    }

    /**
     * No data to be send, just notify of done download. Mostly used to notify a completed upload.
     */
    default void onSuccess () {
    }

    /**
     * The loading failed (No error send)
     */
    default void onFailure () {
        onFailure(null);
    }

    /**
     * notify of started loading
     */
    default void onStart () {
    }

    /**
     * A loading exception occurred. The listener gets notified with the exception.
     *
     * @param exception
     *         Error exception
     */
    void onFailure (Exception exception);
}
