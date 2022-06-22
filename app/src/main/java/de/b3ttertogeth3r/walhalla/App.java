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

package de.b3ttertogeth3r.walhalla;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;
    private static boolean internet = false;

    public static boolean isInternet () {
        return internet;
    }

    public static void setInternet (boolean internet) {
        App.internet = internet;
    }

    public static Context getContext() {
        return ctx;
    }

    public static void setContext (Context context) {
        App.ctx = context;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        try{
            ctx = this.getApplicationContext();
        } catch (Exception ignored) {}
    }
}
