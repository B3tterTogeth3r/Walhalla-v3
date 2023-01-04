/*
 * Copyright (c) 2022-2023.
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

import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.ICloudFunctions;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class CloudFunctions implements ICloudFunctions, IInit {
    private static final String TAG = "CloudFunctions";
    protected static ICloudFunctions iCloudFunctions;
    private FirebaseFunctions cloudFunctions;

    @Override
    public Loader<Boolean> checkBoardMember(String uid) {
        Loader<Boolean> loader = new Loader<>();
        Map<String, Object> data = new HashMap<>();
        data.put("semester", Values.currentSemester.getId());
        data.put("uid", uid);
        try {
            cloudFunctions
                    .getHttpsCallable("checkBoardMember")
                    .call(data)
                    .continueWith(task -> (Boolean) task.getResult().getData())
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            Exception e = task.getException();
                            loader.done(e);
                            return;
                        } else if (task.isSuccessful()) {
                            try {
                                SideNav.reload.reload();
                            } catch (Exception exception) {
                                Log.d(TAG, "checkBoardMember: Reloading the side nav did not " +
                                        "work, even if the result is " + task.getResult() + ". It will work after a relaunch of the app.");
                            }
                            loader.done(task.getResult());
                        }
                        loader.done();
                    });
        } catch (Exception e) {
            return loader.done(false);
        }
        return loader;
    }

    @Override
    public Loader<Charge> getCharge(String uid) {
        Loader<Charge> loader = new Loader<>();
        Map<String, Object> data = new HashMap<>();
        data.put("semester", Values.currentSemester.getId());
        data.put("uid", uid);

        try {
            cloudFunctions
                    .getHttpsCallable("getCharge")
                    .call(data)
                    .continueWith(task -> (String) task.getResult().getData())
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        }
                        try {
                            String result = task.getResult().toUpperCase();
                            Charge c = Charge.valueOf(result);
                            Log.i(TAG, "getCharge: found charge");
                            SideNav.reload.reload();
                            Cache.CACHE_DATA.setCharge(c);
                            loader.done(c);
                        } catch (Exception exception) {
                            loader.done(exception);
                        }
                    });
        } catch (Exception e) {
            return loader.done();
        }
        return loader;
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        try {
            cloudFunctions = FirebaseFunctions.getInstance();
            if (isEmulator) {
                cloudFunctions.useEmulator("10.0.2.2", 5001);
            }
            iCloudFunctions = this;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
