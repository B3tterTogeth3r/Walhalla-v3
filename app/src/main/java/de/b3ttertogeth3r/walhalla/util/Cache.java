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
import androidx.annotation.Nullable;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.CacheData;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see SharedPreferences
 * @since 3.1
 */
public class Cache implements CacheData, IInit {
    public static CacheData CACHE_DATA;
    private static SharedPreferences SP;
    private static IAnalytics analytics;
    private Context context;

    @Override
    public void changeAnalyticsCollections(boolean value) {
        SP.edit().putBoolean(Analytics.TAG, value).apply();
        analytics.changeDataCollection(value);
    }

    @Override
    public boolean getAnalyticsCollection() {
        return SP.getBoolean(Analytics.TAG, true);
    }

    @Override
    public int getStartPage() {
        return SP.getInt(START_PAGE, R.string.menu_home);
    }

    @Override
    public void setStartPage(int pageId) {
        SP.edit().putInt(START_PAGE, pageId)
                .putString(START_PAGE_STR, context.getString(pageId))
                .apply();
        analytics.changeStartPage(pageId);
    }

    @Override
    public String getStartPageStr() {
        return SP.getString(START_PAGE_STR, "Start");
    }

    @Override
    public boolean isFirstStart() {
        return SP.getBoolean(FIRST_START, false);
    }

    @Override
    public void setFirstStart(boolean value) {
        SP.edit().putBoolean(FIRST_START, value).apply();
    }

    @Override
    public int getChosenSemester() {
        return SP.getInt(CHOSEN_SEMESTER, Values.currentSemester.getId());
    }

    @Override
    public void setChosenSemester(int semId) {
        SP.edit().putInt(CHOSEN_SEMESTER, semId).apply();
    }

    @Override
    public boolean isBoardMember() {
        if (getRank() == Rank.ADMIN) {
            return true;
        }
        return SP.getBoolean(BOARD_MEMBER, false);
    }

    @Override
    public void setBoardMember(boolean value) {
        SP.edit().putBoolean(BOARD_MEMBER, value).apply();
    }

    public Visibility getVisibility() {
        Visibility v = Visibility.PUBLIC;
        for (Visibility visibilities : Visibility.values()) {
            if (visibilities.toString().toUpperCase()
                    .equals(SP.getString(VISIBILITY, "PUBLIC"))) {
                return visibilities;
            }
        }
        return v;
    }

    @Override
    public void setVisibility(@NonNull Visibility visibility) {
        SP.edit().putString(VISIBILITY, visibility.toString().toUpperCase()).apply();
    }

    @Override
    @Nullable
    public Charge getCharge() {
        return Charge.valueOf(SP.getString(CHARGE, "NONE").toUpperCase());
    }

    @Override
    public void setCharge(@NonNull Charge charge) {
        SP.edit().putString(CHARGE, charge.toString().toUpperCase()).apply();
    }

    @Override
    public Rank getRank() {
        Charge c = getCharge();
        if (c == Charge.ADMIN) {
            return Rank.ADMIN;
        }
        try {
            return Rank.valueOf(SP.getString(RANK, Rank.NONE.toString().toUpperCase()));
        } catch (Exception e) {
            e.printStackTrace();
            return Rank.NONE;
        }
    }

    @Override
    public void setRank(@NonNull Rank rank) {
        if (getCharge() == Charge.ADMIN) {
            SP.edit()
                    .putString(RANK, Rank.ADMIN.toString().toUpperCase())
                    .apply();
        } else {
            SP.edit()
                    .putString(RANK, rank.toString().toUpperCase())
                    .apply();
        }
    }

    @Override
    public void reset() {
        SP.edit().putString(RANK, Rank.NONE.toString().toUpperCase())
                .putString(CHARGE, Charge.NONE.toString())
                .putString(BOARD_MEMBER, Charge.NONE.toString())
                .putInt(CHOSEN_SEMESTER, Values.currentSemester.getId())
                .putString(START_PAGE_STR, "Start")
                .putString(VISIBILITY, "PUBLIC")
                .apply();
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        try {
            this.context = context;
            Cache.SP = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            analytics = Firebase.analytics();
            CACHE_DATA = this;
            setChosenSemester(Values.currentSemester.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
