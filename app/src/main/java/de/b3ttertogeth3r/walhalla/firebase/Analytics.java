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
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.interfaces.CacheData;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Cache;

public class Analytics implements IInit, IAnalytics {
    public static final String TAG = "Analytics";
    protected static IAnalytics iAnalytics;
    private FirebaseAnalytics analytics;
    private CacheData cacheData;

    @Override
    public void screenChange(@NonNull String fragment_name) {
        if (cacheData.getAnalyticsCollection()) {
            Bundle bundle = new Bundle();
            bundle.putString("Fragment", fragment_name);
            analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
            Log.i(TAG, "screen change send");
        }
    }

    @Override
    public void changeStartPage(int res_string_value) {
        analytics.setUserProperty("start_page",
                String.valueOf(cacheData.getStartPage()));
        analytics.setUserProperty("start_page_str",
                String.valueOf(cacheData.getStartPageStr()));
    }

    @Override
    public void setRank(@NonNull Rank rank) {

    }

    @Override
    public void changeDataCollection(boolean value) {
        analytics.setAnalyticsCollectionEnabled(value);
    }

    @Override
    public boolean init(Context context) {
        try {
            analytics = FirebaseAnalytics.getInstance(context);
            iAnalytics = this;
            cacheData = Cache.CACHE_DATA;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
