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

package de.b3ttertogeth3r.walhalla.object;

import android.content.Context;

import com.google.firebase.Timestamp;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.object.INews;
import de.b3ttertogeth3r.walhalla.util.Log;

/**
 * A news entry. it has nested online the content of the news entry as a list of {@link Text}
 * objects. They are loaded on
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public class News extends MyObject implements INews {
    /**
     * title of the news entry
     */
    private String title;
    /**
     * to whom this entry is visible to
     */
    private Visibility visibility;
    /**
     * number of text objects nested inside the entry
     */
    private int amount;
    /**
     * uid of the user who uploaded the news entry
     */
    private String uploadedBy;

    private String id;

    public News() {
    }

    public News(String title, Visibility visibility, int amount, String uploadedBy, Timestamp timestamp) {
        this.title = title;
        this.visibility = visibility;
        this.amount = amount;
        this.uploadedBy = uploadedBy;
        this.time = timestamp;
    }

    @Override
    public LinearLayout getView(Context context) throws NoDataException {
        LinearLayout layout = new LinearLayout(context);
        de.b3ttertogeth3r.walhalla.design.Title title =
                new de.b3ttertogeth3r.walhalla.design.Title(context);
        de.b3ttertogeth3r.walhalla.design.Text time =
                new de.b3ttertogeth3r.walhalla.design.Text(context);
        title.setTitle(this.title);
        // SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy mm:hh", Variables.LOCALE);
        time.setText(getTime().toDate().toString());
        layout.addView(title);
        layout.addView(time);

        IFirestoreDownload contentDownload = Firebase.Firestore.download();
        contentDownload.getNewsText(getId())
                .setOnSuccessListener(result -> {
                    if (result != null)
                        for (Text t : result) {
                            for (String s : t.getValue()) {
                                layout.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, s));
                            }
                        }
                }).setOnFailListener(e -> {
                    Log.e("News", "getView: download of the content didn't work", e);
                });
        return layout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility rank) {
        this.visibility = rank;
    }

    public int getAmount() {
        return amount;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
