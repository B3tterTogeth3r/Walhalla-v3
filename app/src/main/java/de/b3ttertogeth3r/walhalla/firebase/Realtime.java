package de.b3ttertogeth3r.walhalla.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.b3ttertogeth3r.walhalla.App;

/**
 * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
 */
public class Realtime {
    private static final String TAG = "Realtime";

    /**
     * Sets a {@link App#isInternetConnection() boolean} for the internet connection of the
     * app.
     *
     * @see
     * <a href="https://firebase.google.com/docs/database/android/offline-capabilities#section-connection-state">Connection
     * State</a>
     * @since 1.0
     */
    public static void internet (String uid) {
        Firebase.REALTIME_DB.getReference("/online_users").onDisconnect().setValue(null,
                (error, ref) -> {
                    // set value of Person/{userID}/online to false
                    //Firebase.onlineStatus(uid, false);
                });

        DatabaseReference connectedRef = Firebase.REALTIME_DB.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                boolean connected = false;
                try {
                    //noinspection ConstantConditions
                    connected = snapshot.getValue(Boolean.class);
                } catch (Exception e) {
                    Log.d(TAG, "not connected");
                    App.setInternetConnection(false);
                }
                if (connected) {
                    Log.d(TAG, "connected");
                    App.setInternetConnection(true);
                    // set value of Person/{userID}/online to true
                    Firebase.onlineStatus(uid, true);
                } else {
                    Log.d(TAG, "not connected");
                    App.setInternetConnection(false);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
    }
}
