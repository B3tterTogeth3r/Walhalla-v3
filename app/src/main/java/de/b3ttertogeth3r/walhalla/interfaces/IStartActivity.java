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

package de.b3ttertogeth3r.walhalla.interfaces;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.StartActivity;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.FirebaseInit;

public interface IStartActivity extends FirebaseInit {
    /**
     * FIXME: 02.08.22 before publishing: set false
     */
    boolean IS_EMULATOR = true;

    default void start() {
        initApp();
        initCache();
        checkFirstStart();
        Object o = new Object();
        synchronized (o) {
            initFirebase();
            try {
                o.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                checkRank();
            }
        }
    }

    /**
     * @see de.b3ttertogeth3r.walhalla.App App
     */
    void initApp();

    /**
     * @see de.b3ttertogeth3r.walhalla.util.Cache Cache
     */
    void initCache();

    /**
     * If the app is started the first time, it needs internet. Otherwise close the app.
     */
    void checkFirstStart();

    /**
     * @see de.b3ttertogeth3r.walhalla.interfaces.firebase.FirebaseInit FirebaseInit
     */
    void initFirebase();

    /**
     * Check if the signed in user is a board member.
     */
    void checkRank();

    boolean isOnline();

    /**
     * Update the progressbar. If the {@link StartActivity#COUNTER} equals
     * {@link StartActivity#TOTAL}, the {@link MainActivity} is started.
     *
     * @since {@link StartActivity} version 1.0
     */
    void updateProgressbar();
}
