/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Drink {
    private int amount = 0;
    private Timestamp date = new Timestamp(new Date());
    private float price = 0f;
    private String uid;
    private String kind;

    /**
     * empty constructor
     *
     * @see #Drink(int, float, String, String) full constructor
     * @since 1.0
     */
    public Drink() {
    }

    /**
     * @param totalAmount the amount of bottles
     * @param priceEach   the price per bottle
     * @param uid         the uid of the user
     * @param name        the name of the drink
     * @since 1.0
     */
    public Drink(int totalAmount, float priceEach, String uid, String name) {
        this.amount = totalAmount;
        this.price = priceEach;
        this.uid = uid;
        this.kind = name;
    }

    public int getAmount () {
        return amount;
    }

    public void setAmount (int amount) {
        this.amount = amount;
    }

    public Timestamp getDate () {
        return date;
    }

    public void setDate (Timestamp date) {
        this.date = date;
    }

    public float getPrice () {
        return price;
    }

    public void setPrice (float price) {
        this.price = price;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getKind () {
        return kind;
    }

    public void setKind (String kind) {
        this.kind = kind;
    }
}
