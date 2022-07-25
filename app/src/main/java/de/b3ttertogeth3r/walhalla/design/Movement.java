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

package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Movement {
    private static final String TAG = "Movement";
    private final Context context;
    private final TableRow view;
    private final Text time;
    /**
     * the actual amount of money, not the amount of bottles
     */
    private final Text expense;
    private final Text income;
    /**
     * purpose of a movement or the amount and the name of the drink
     */
    private final Text purpose;
    /**
     * additional string - if drink-> the price per bottle
     */
    private final Text add;
    private final ImageView recipe;
    private Timestamp timestamp;

    public Movement(@NonNull Context context, @Nullable ViewGroup root) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = (TableRow) inflater.inflate(R.layout.movement_layout, root);
        time = view.findViewById(R.id.date);
        expense = view.findViewById(R.id.expense);
        income = view.findViewById(R.id.income);
        purpose = view.findViewById(R.id.purpose);
        add = view.findViewById(R.id.add);
        recipe = view.findViewById(R.id.recipe);

        final int DIVIDER = 2;

        time.setPadding((time.getPaddingLeft() / DIVIDER),
                (time.getTop() / DIVIDER),
                (time.getPaddingRight() / DIVIDER),
                (time.getPaddingBottom()) / DIVIDER);
        expense.setPadding((expense.getPaddingLeft() / DIVIDER),
                (expense.getTop() / DIVIDER),
                (expense.getPaddingRight() / DIVIDER),
                (expense.getPaddingBottom()) / DIVIDER);
        income.setPadding((income.getPaddingLeft() / DIVIDER),
                (income.getTop() / DIVIDER),
                (income.getPaddingRight() / DIVIDER),
                (income.getPaddingBottom()) / DIVIDER);
        purpose.setPadding((purpose.getPaddingLeft() / DIVIDER),
                (purpose.getTop() / DIVIDER),
                (purpose.getPaddingRight() / DIVIDER),
                (purpose.getPaddingBottom()) / DIVIDER);
        add.setPadding((add.getPaddingLeft() / DIVIDER),
                (add.getTop() / DIVIDER),
                (add.getPaddingRight() / DIVIDER),
                (add.getPaddingBottom()) / DIVIDER);
        recipe.setPadding((recipe.getPaddingLeft() / DIVIDER),
                (recipe.getTop() / DIVIDER),
                (recipe.getPaddingRight() / DIVIDER),
                (recipe.getPaddingBottom()) / DIVIDER);
    }

    @NonNull
    public static Movement create(FragmentActivity context, ViewGroup root, @NonNull de.b3ttertogeth3r.walhalla.object.Movement m) {
        Movement movement = new Movement(context, root);
        movement.setTime(m.getTime());
        double dAmount = m.getAmount();
        if (dAmount == 0) {
            movement.setIncome(0);
            movement.setExpense(0);
        } else if (dAmount < 0) {
            movement.setIncome(0);
            movement.setExpense(Math.abs(dAmount));
        } else if (dAmount > 0) {
            movement.setIncome(Math.abs(dAmount));
            movement.setExpense(0);
        }
        if (!m.getPurpose().isEmpty()) {
            movement.setPurpose(m.getPurpose());
        }
        if (!m.getAdd().isEmpty()) {
            movement.setAdd(m.getAdd());
        } else {
            movement.setAdd(String.valueOf(m.getAmount()));
        }
        if (m.getRecipe() != null) {
            movement.setRecipe(m.getRecipe());
        }
        return movement;
    }

    public void setIncome(double amount) {
        if (amount == 0) {
            return;
        }
        String s = "€ " + String.format(Values.LOCALE, "%.2f", amount).replace(".", ",");
        this.income.setText(s);
    }

    public void setExpense(double amount) {
        if (amount == 0) {
            return;
        }
        String s = "€ " + String.format(Values.LOCALE, "%.2f", amount).replace(".", ",");
        this.expense.setText(s);
    }

    public void setPurpose(String purpose) {
        this.purpose.setText(purpose);
    }

    public void setAdd(String add) {
        this.add.setText(add);
    }

    public void setRecipe(DocumentReference ref) {
        this.recipe.setVisibility(ViewGroup.VISIBLE);
        Firebase.firestoreDownload()
                .file(ref)
                .setOnSuccessListener(result -> {
                    if (result != null && result.validate()) {
                        MainActivity.openExternal.file(result);
                    }
                    throw new NoDataException("No data downloaded");
                })
                .setOnFailListener(e -> {
                    Log.e(TAG, "onFailureListener: ", e);
                    Toast.makeToast(context, R.string.fui_error_unknown).show();
                });
    }

    @NonNull
    public static Movement create(FragmentActivity context, ViewGroup root, @NonNull DrinkMovement dm) {
        Movement movement = new Movement(context, root);
        movement.setExpense(dm.getAmount() * dm.getPrice());
        movement.setIncome(0);
        movement.setPurpose(context.getString(R.string.menu_drink_invoice));
        movement.setAdd(dm.getAmount() + " " + dm.getViewString());
        return movement;
    }

    public TableRow show() {
        return view;
    }

    public Timestamp getTime() {
        return timestamp;
    }

    public void setTime(@NonNull Timestamp timestamp) {
        this.timestamp = timestamp;
        SimpleDateFormat day = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
        time.setText(day.format(timestamp.toDate()));
        time.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public String toString() {
        return "Design/" + TAG;
    }
}
