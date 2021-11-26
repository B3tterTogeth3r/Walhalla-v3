package de.b3ttertogeth3r.walhalla.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A custom listener to make a wait on firebase download results more easy.
 *
 * @since 1.1
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data">Wait on Firebase Result</a> Answer on GitHub I kindly used.
 */
public interface OnGetDataListener {
    String TAG = "OnGetDataListener";

    void onSuccess (DocumentSnapshot documentSnapshot);

    default void onFailure () {
    }

    default void onStart () {
    }

    default void onFailure (Exception exception) {
    }
}
