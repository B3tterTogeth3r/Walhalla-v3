/*
 * Copyright (c) 2022-2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;

public class CloudMessaging extends FirebaseMessagingService implements IInit {
    private static final String TAG = "CloudMessaging";
    private FirebaseMessaging messaging;

    public static void sendNotification(Person person, String message) {
        // TODO: 25.05.22 send it via a path in the realtime database together with cloud functions
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // TODO: 25.05.22 save token online
    }

    @Override
    public boolean init(Context context) {
        try {
            messaging = FirebaseMessaging.getInstance();
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Crashlytics init error", e);
            return false;
        }
    }

    private void getToken() {
        Context context = App.getContext();
        messaging.getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = context.getString(R.string.msg_token_fmt) + token;
                    Log.d(TAG, msg);
                    Toast.makeToast(context, msg).show();
                });
    }
}
