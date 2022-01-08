package de.b3ttertogeth3r.walhalla.firebase;

import android.util.Log;

import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;

/**
 * @see <a href="https://firebase.google.com/docs/cloud-messaging">Firebase Cloud Messaging</a>
 */
public class Messaging {
    private static final String TAG = "Messaging";

    public static void getFCMToken (MyCompleteListener<String> listener) {
        Firebase.MESSAGING.getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onFailure(task.getException());
                return;
            }

            // Get new FCM registration token
            String token = task.getResult();

            // Log and toast
            Log.d(TAG, token);

            listener.onSuccess(token);
        });
    }
}
