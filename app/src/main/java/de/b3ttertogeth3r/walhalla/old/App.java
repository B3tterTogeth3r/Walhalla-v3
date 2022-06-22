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

package de.b3ttertogeth3r.walhalla.old;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Stack
 * @see android.app.Application
 * @see android.content.Context
 * @since 1.0
 */
@SuppressLint("StaticFieldLeak")
public class App extends Application {
    private static final String TAG = "App";
    private static Context ctx;
    private static boolean internetConnection = true;
    private static FragmentManager fm;

    public App () {
        App.ctx = getContext();
    }

    public static void init () {
        try {
            StartActivity.newDone.appDone();
        } catch (Exception e) {
            Log.e(TAG, "init: error", e);
        }
    }

    public static Context getContext () {
        return ctx;
    }

    public static void setContext (Context ctx) {
        App.ctx = ctx;
    }


    /**
     * @return true, if the app has an active internet connection
     */
    public static boolean isInternetConnection () {
        return internetConnection;
    }

    /**
     * @param internetConnection
     *         true, if the app has an active internet connection
     */
    public static void setInternetConnection (boolean internetConnection) {
        App.internetConnection = internetConnection;
    }

    public static FragmentManager getFragmentManager () {
        return fm;
    }

    public static void setFragmentManager (FragmentManager manager) {
        fm = manager;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        App.ctx = this;
    }
}
