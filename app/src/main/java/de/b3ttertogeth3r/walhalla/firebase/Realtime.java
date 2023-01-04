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

package de.b3ttertogeth3r.walhalla.firebase;

import static de.b3ttertogeth3r.walhalla.firebase.Realtime.Paths.PURPOSE;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IRealtimeDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IRealtimeUpload;
import de.b3ttertogeth3r.walhalla.util.Log;

public class Realtime implements IInit {
    private static final String TAG = "Realtime";
    protected static IRealtimeUpload upload;
    protected static IRealtimeDownload download;
    private FirebaseDatabase database;

    public Realtime() {
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        try {
            database = FirebaseDatabase.getInstance();
            download = new Realtime.Download();
            upload = new Realtime.Upload();
            if (isEmulator) {
                database.useEmulator("10.0.2.2", 9000);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "init: ", e);
            return false;
        }
    }

    protected enum Paths {
        PURPOSE("purpose");

        private final String description;

        Paths(String description) {
            this.description = description;
        }

        public String get() {
            return description;
        }
    }

    private abstract class Common {
        @NonNull
        final public Task<DataSnapshot> purposePath() {
            return database.getReference(PURPOSE.get()).get();
        }
    }

    public class Download extends Common implements IRealtimeDownload {
        private static final String TAG = "Realtime.Download";

        public Download() {
            download = this;
        }

        @Override
        public Loader<ArrayList<String>> getPurposeList() {
            Loader<ArrayList<String>> loader = new Loader<>();
            ArrayList<String> list = new ArrayList<>();
            purposePath().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    loader.done(task.getException());
                    return;
                }
                Log.d(TAG, String.valueOf(task.getResult()));
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    try {
                        list.add(ds.getValue(String.class));
                    } catch (Exception e) {
                        Log.e(TAG, "getPurposeList: error found!", e);
                    }
                }
                loader.done(list);

            });
            return loader;
        }

        @Override
        public Loader<Integer> getCurrentSemester() {
            Loader<Integer> loader = new Loader<>();
            return loader.done();
        }
    }

    public class Upload extends Common implements IRealtimeUpload {
        private static final String TAG = "Realtime.Upload";

        public Upload() {
            upload = this;
        }

        @Override
        public Loader<Boolean> newPurpose(String purpose) {
            Loader<Boolean> loader = new Loader<>();
            download.getPurposeList()
                    .setOnSuccessListener(result -> {
                        if (result == null) {
                            result = new ArrayList<>();
                        }
                        result.add(purpose);
                        database.getReference(PURPOSE.get())
                                .setValue(result)
                                .addOnCompleteListener(task1 -> loader.done(true));
                    })
                    .setOnFailListener(loader::done);
            return loader;
        }
    }
}
