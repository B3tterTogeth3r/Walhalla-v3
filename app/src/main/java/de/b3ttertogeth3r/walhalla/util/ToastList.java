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

import androidx.annotation.NonNull;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.design.Toast;

/**
 * A list to only show one Toast at a time.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public class ToastList {
    private static final String TAG = "ToastList";
    private static final ArrayList<Toast> toastList = new ArrayList<>();

    public static void addToast(@NonNull Toast toast) {
        toastList.add(toast);
    }

    public static void show() {
        if (!toastList.isEmpty()) {
            for (Toast t : toastList) {
                t.setDuration(android.widget.Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
}
