package de.b3ttertogeth3r.walhalla.interfaces;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import de.b3ttertogeth3r.walhalla.models.Image;
import de.b3ttertogeth3r.walhalla.models.User;

/**
 * A custom listener to make a notification on firebase download results more easy.
 * Learned Generics with this class via <a href="https://www.youtube.com/watch?v=K1iu1kXkVoA">this video</a>.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see
 * <a href="https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data">Wait
 * on Firebase Result</a> Answer on GitHub I kindly used.
 * @since 1.1
 */
public interface MyCompleteListener<T> {
    String TAG = "OnGetDataListener";

    /**
     * Data returned by the Listener
     * @param result value of the result. Can be null
     */
    void onSuccess(@Nullable T result);

    /**
     * The loading failed (No error send)
     */
    default void onFailure () {
        onFailure(null);
    }

    /**
     * notify of started loading
     */
    default void onBegin () {
    }

    /**
     * A loading exception occurred. The listener gets notified with the exception.
     *
     * @param exception
     *         Error exception
     */
    void onFailure (@Nullable Exception exception);
}
