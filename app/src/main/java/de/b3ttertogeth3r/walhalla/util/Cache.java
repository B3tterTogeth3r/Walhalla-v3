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

package de.b3ttertogeth3r.walhalla.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.mock.AnalyticsMock;
import de.b3ttertogeth3r.walhalla.mock.AuthMock;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;

/**
 * @author B3tterTogeth3r
 */
public class Cache implements IInit {
    public static final String START_PAGE = "start_page";
    public static final String USER_DATA = "user_data";
    private static final String TAG = "Cache";
    private static final String ADDRESS = USER_DATA + "_address";
    private static SharedPreferences SP;
    private static IAnalytics analytics;
    private static IAuth auth;
    private static IFirestoreDownload firestoreDownload;

    private Cache() {
    }

    public static boolean changeAnalyticsCollections(boolean value) {
        boolean result = SP.edit().putBoolean(Analytics.TAG, value).commit();
        if (result) {
            analytics.changeDataCollection(value);
            return true;
        } else {
            return false;
        }
    }

    public static boolean getAnalyticsCollection() {
        return SP.getBoolean(Analytics.TAG, true);
    }

    public static int getStartPage() {
        return SP.getInt(START_PAGE, R.string.menu_home);
    }

    public static boolean setStartPage(int pageId) {
        boolean result = SP.edit().putInt(START_PAGE, pageId).commit();
        if (result) {
            analytics.changeStartPage(pageId);
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    public static Loader<Boolean> saveUserData() {
        Loader<Boolean> resultLoader = new Loader<>();
        if (auth.isSignIn()) {
            String uid = auth.getUser().getUid();
            Loader<Boolean[]> waitLoader = new Loader<>();
            Boolean[] wait = new Boolean[]{false, false, false, false, false};
            firestoreDownload.person(uid).setOnSuccessListener(result -> {
                if (result == null || !result.validate()) {
                    throw new NoDataException("Person download failed");
                }
                if (result.getId() != null && !result.getId().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.ID, result.getId()).apply();
                }

                if (result.getFirst_Name() != null && !result.getFirst_Name().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.FIRST_NAME, result.getFirst_Name()).apply();
                }

                if (result.getLast_Name() != null && !result.getLast_Name().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.LAST_NAME, result.getLast_Name()).apply();
                }

                if (result.getOrigin() != null && !result.getOrigin().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.ORIGIN, result.getOrigin()).apply();
                }

                if (result.getMajor() != null && !result.getMajor().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.MAJOR, result.getMajor()).apply();
                }

                if (result.getNickname() != null && !result.getNickname().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.NICKNAME, result.getNickname()).apply();
                }

                if (result.getJoined() != -1) {
                    SP.edit().putInt(USER_DATA + Person.JOINED, result.getJoined()).apply();
                }

                if (result.getMobile() != null && !result.getMobile().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.MOBILE, result.getMobile()).apply();
                }

                if (result.getMail() != null && !result.getMail().isEmpty()) {
                    SP.edit().putString(USER_DATA + Person.MAIL, result.getMail()).apply();
                }

                if (result.getBirthday() != null) {
                    SP.edit().putLong(USER_DATA + Person.BIRTHDAY, result.getBirthday().toDate().getTime()).apply();
                }

                if (result.getRank() != null) {
                    SP.edit().putInt(USER_DATA + Person.RANK, result.getRank().ordinal()).apply();
                }
                SP.edit().putBoolean(USER_DATA + Person.ENABLED, result.isEnabled())
                        .putBoolean(USER_DATA + Person.PASSWORD, result.isPassword()).apply();
                wait[0] = true;
                waitLoader.done(wait);
            });
            firestoreDownload.personAddress(uid).setOnSuccessListener(result -> {
                if (result == null || result.isEmpty()) {
                    throw new NoDataException("Download of address did not work or user has none");
                }
                for (int i = 0; i < result.size(); i++) {
                    for (int j = -1; j < 7; j++) {
                        SP.edit().putString(i + ADDRESS + j, result.get(i).getValue(j)).apply();
                    }
                }
            });
            firestoreDownload.personUndoneChores(uid).setOnSuccessListener(result -> {
            });
            firestoreDownload.personImage(uid).setOnSuccessListener(result -> {
                // TODO: 13.07.22 save image list in cache
            });
            firestoreDownload.pastChargen(uid).setOnSuccessListener(result -> {
                // TODO: 13.07.22 save board member list in cache
            });

            return resultLoader;
        } else {
            return resultLoader.done();
        }
    }

    @Override
    public boolean init(Context context) {
        try {
            SP = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            analytics = new AnalyticsMock();
            auth = new AuthMock();
            firestoreDownload = new FirestoreMock.Download();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "init: SharedPreferences error", e);
            return false;
        }
    }
}
