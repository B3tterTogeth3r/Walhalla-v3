/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old.firebase;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.jetbrains.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.MainActivity;
import de.b3ttertogeth3r.walhalla.old.models.Person;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final long SENDER_ID = 456221124895L;
    public static final String SERVER_KEY = "AAAAajjl6R8" +
            ":APA91bHGvk4AgM4UU_HnnkePOBg6tMsW2UzeJpQKAjwdGj6py8X3mVvAxT982dCylnT916B67LlFk" +
            "-A1qrKto4GugaiXAZaqmkhEOqxshh3ioDT8ZZ3p_KpR5hqL-hTYNjG786E1OMcQ";
    private static final String TAG = "MyFirebaseMessagingService";


    public MyFirebaseMessagingService () {
        super();
    }

    @Override
    public void onNewToken (@NonNull @NotNull String token) {
        if(Authentication.isSignIn()) {
            FirebaseUser user = Authentication.getUser();

            //send token to Firestore
            Person p = CacheData.getUser();
            if (user != null && p.getId().equals(user.getUid())) {
                p.setFcm_token(token);
                Firestore.uploadPerson(p);
            }
            CacheData.saveUser(p);
        }
    }

    @Override
    public void handleIntent (@NonNull Intent intent) {
        super.handleIntent(intent);
        Bundle extras = intent.getExtras();
        ActivityManager.RunningAppProcessInfo myProcess =
                new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        boolean isInBackground =
                myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        String page = extras.getString("StartPage", CacheData.getStartPage() + "");

        /* App shall start on another start page, because of the message, if it is in the
         * background.If the app is in the foreground, a dialog with the message and the title
         * will be displayed.
         */
        if (!page.isEmpty()) {
            if (isInBackground) {
                switch (page) {
                    case "balance":
                        CacheData.setIntentStartPage(R.string.menu_balance);
                        break;
                    case "program":
                        CacheData.setIntentStartPage(R.string.menu_program);
                        break;
                    case "news":
                        CacheData.setIntentStartPage(R.string.menu_messages);
                        break;
                }
            } else {
                String title = extras.getString("gcm.notification.title");
                String message = extras.getString("gcm.notification.body");
                try {
                    MainActivity.inAppMessage.displayMessage(title, message, page);
                } catch (Exception ignored){
                }
            }

        }
    }

}
