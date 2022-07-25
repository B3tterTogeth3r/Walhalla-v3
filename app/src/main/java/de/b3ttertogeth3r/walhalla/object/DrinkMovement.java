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

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;
import de.b3ttertogeth3r.walhalla.util.ToastList;

/**
 * Like {@link Movement} but just for {@link Drink}s and not for income and other movements
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public class DrinkMovement extends MyObject implements Validate {
    /**
     * id of the drink<br>usually set after the download to find the object in the database.
     */
    private String id;
    /**
     * name of the drink
     */
    private String name;
    /**
     * amount of bottles the user consumed
     */
    private int amount;
    /**
     * price of one bottle
     */
    private float price;

    /**
     * default constructor for {@link de.b3ttertogeth3r.walhalla.firebase.Firestore Firestore} downloads
     */
    public DrinkMovement() {
    }

    /**
     * constructor for new created recipes
     *
     * @param name   Name of the drink
     * @param amount Amount of bottles the user consumed
     * @param price  Price of one bottle
     * @param time   Time the recipe was created
     */
    public DrinkMovement(String name, int amount, float price, Timestamp time) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.time = time;
    }

    public DrinkMovement(String id, String name, int amount, float price, Timestamp time) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.time = time;
    }

    public TableRow getView(@NonNull Context context) {
        TableRow view = new TableRow(context);
        ToastList.addToast(Toast.makeToast(context, R.string.error_dev));
        return view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
