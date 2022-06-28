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

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.b3ttertogeth3r.walhalla.interfaces.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;

public class Firestore implements IInit {
    private static final String TAG = "Firestore";
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore FBFS;

    public Firestore () {
    }

    @Nullable
    public static DocumentReference getReference(String refString) {
        try {
            return FBFS.collection("").document(refString);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean init (Context context) {
        try {
            FBFS = FirebaseFirestore.getInstance();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static class Download {//implements IFirestoreDownload {
    }

    public static class Upload { //implements IFirestoreUpload {

    }
}
