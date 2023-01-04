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

package de.b3ttertogeth3r.walhalla.interfaces;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.util.Log;

public interface RealtimeListeners {
    ArrayList<ListenerRegistration> listener = new ArrayList<>();

    /**
     * Stop listening to realtime changes via the balance listener
     */
    static void stopRealtimeListener() {
        for (ListenerRegistration lr : listener) {
            lr.remove();
        }
        Log.i("RealtimeListeners", "Successfully removed all listeners");
    }
}
