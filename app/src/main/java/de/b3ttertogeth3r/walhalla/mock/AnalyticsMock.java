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

package de.b3ttertogeth3r.walhalla.mock;

import android.content.Context;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public class AnalyticsMock implements IAnalytics, IInit {
    private static final String TAG = "AnalyticsMock";
    /**
     * true, if the user allowed data collection by Google's Firebase Analytics
     */
    private boolean dataCollection;

    public AnalyticsMock() {
        try {
            ToastList.addToast(Toast.makeToast(App.getContext(), TAG + "-MOCK-DATA"));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void screenChange(@NonNull String name) {
        if (dataCollection)
            Log.i(TAG, "screenChange: changed screen to" + name);
    }

    @Override
    public void changeStartPage(int string_value) {
        if (dataCollection)
            Log.i(TAG, "Start page set to " + string_value);
    }

    @Override
    public void setRank(@NonNull Rank rank) {
        if (dataCollection)
            Log.i(TAG, "Rank set to " + rank);
    }

    @Override
    public void changeDataCollection(boolean value) {
        dataCollection = value;
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        return true;
    }
}
