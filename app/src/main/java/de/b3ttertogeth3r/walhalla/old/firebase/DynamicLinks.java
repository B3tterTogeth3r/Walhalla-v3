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

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;

public class DynamicLinks {
    private static final String TAG = "DynamicLinks";
    public static FirebaseDynamicLinks DYNAMIC_LINKS;

    public static void getLink (Intent intent, MyCompleteListener<Void> listener) {
        DYNAMIC_LINKS.getDynamicLink(intent).addOnSuccessListener(pendingDynamicLinkData -> {
            try {
                if (pendingDynamicLinkData.getLink() != null) {
                    Uri deepLink = pendingDynamicLinkData.getLink();
                    if (deepLink != null) {
                        Log.e(TAG, deepLink.getPath());
                    }
                }
            } catch (Exception ignored) {
            } finally {
                listener.onSuccess(null);
            }
        }).addOnFailureListener(exception -> {
            Crashlytics.error(TAG, ":getDynamicLink" +
                    ":onFailure", exception);
            listener.onSuccess(null);
        });
    }
}
