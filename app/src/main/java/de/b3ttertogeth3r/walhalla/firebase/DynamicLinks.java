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
import android.content.Intent;
import android.util.Log;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;

public class DynamicLinks implements IInit {
    private static final String TAG = "DynamicLinks";
    private static Context context;
    private static FirebaseDynamicLinks dynamicLinks;

    public static void receivedDynamicLink(Intent intent, Loader<PendingDynamicLinkData> result) {
        dynamicLinks.getDynamicLink(intent)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        result.done(task.getException());
                        return;
                    }
                    result.done(task.getResult());
                });
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        try {
            DynamicLinks.context = context;
            dynamicLinks = FirebaseDynamicLinks.getInstance();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "init: exception found", e);
            return false;
        }
    }
}
