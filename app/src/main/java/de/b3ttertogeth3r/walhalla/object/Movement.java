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
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.MyObject;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.interfaces.Validate;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Movement extends MyObject implements Validate {
    private static final String TAG = "Movement";
    private Double amount;
    private String add;
    private String purpose;
    private DocumentReference recipe;
    private String id;

    public Movement() {
    }

    public Movement(Timestamp time, Double amount, String add, String purpose, DocumentReference recipe) {
        this.time = time;
        this.amount = amount;
        this.add = add;
        this.purpose = purpose;
        this.recipe = recipe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TableRow getView(Context context) {
        TableRow row = new TableRow(context);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
        row.addView(new Text(context, format.format(time.toDate())));
        if (amount == 0) {
            row.addView(new Text(context, "€ 0,00"));
            row.addView(new Text(context, "€ 0,00"));
        } else if (amount > 0) {
            row.addView(new Text(context, getAmount().toString()));
            row.addView(new Text(context, "€ 0,00"));
        } else if (amount < 0) {
            row.addView(new Text(context, "€ 0,00"));
            row.addView(new Text(context, amount.toString()));
        }
        Text desc = new Text(context);
        if (!purpose.isEmpty()) {
            desc.setText(purpose);
        }
        row.addView(desc);
        if (recipe != null) {
            Image arrow = new Image(context);
            arrow.setImage(R.drawable.ic_arrow_right);
            row.addView(arrow);
            IFirestoreDownload download = new FirestoreMock.Download();
            arrow.setOnClickListener(v -> download.file(recipe)
                    .setOnSuccessListener(result -> MainActivity.openExternal.file(result))
                    .setOnFailListener(e -> Log.e(TAG, "File download didn't work", e)));
        } else {
            row.addView(new Text(context));
        }
        return row;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public DocumentReference getRecipe() {
        return recipe;
    }

    public void setRecipe(DocumentReference recipe) {
        this.recipe = recipe;
    }

    @Override
    public Timestamp getTime() {
        return time;
    }

    @Override
    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
