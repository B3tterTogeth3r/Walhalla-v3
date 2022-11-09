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

package de.b3ttertogeth3r.walhalla.dialog;

import static de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore.download;

import android.content.Context;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.object.PersonLight;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.SearchModel;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class PersonSearchDialog extends SimpleSearchDialogCompat<SearchModel> {
    private static final String TAG = "PersonSearchDialog";

    public PersonSearchDialog(Context context, String title, String searchHint, @Nullable Filter filter, ArrayList<SearchModel> items, SearchResultListener<SearchModel> searchResultListener) {
        super(context, title, searchHint, filter, items, searchResultListener);
    }

    @NonNull
    public static Loader<PersonLight> create(FragmentActivity activity) {
        Loader<PersonLight> loader = new Loader<>();
        download()
                .getPersonList(activity)
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        throw new NoDataException("No person was downloaded.");
                    }
                    ArrayList<SearchModel> personList = new ArrayList<>();
                    for (PersonLight p : result) {
                        personList.add(new SearchModel(p.getFull_Name()));
                    }
                    SimpleSearchDialogCompat<SearchModel> ssdc = new SimpleSearchDialogCompat<>(activity, "Search...",
                            "Who are you looking for?", null, personList,
                            (dialog, item, position) -> {
                                dialog.dismiss();
                                loader.done(result.get(position));
                            });
                    ssdc.setCancelOnTouchOutside(false);
                    ssdc.show();
                })
                .setOnFailListener(e -> {
                    Toast.makeToast(activity,
                            "An error occurred while downloading the list. Try again another time").show();
                    Log.e(TAG, "showPersonDialog: loading the list didn't work", e);
                    loader.done(e);
                });
        return loader;
    }
}
