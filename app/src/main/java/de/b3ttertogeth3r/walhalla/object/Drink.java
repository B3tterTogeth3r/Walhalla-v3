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

import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

/**
 * An object to display a drink
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public class Drink implements Validate {
    private static final String TAG = "Drink";
    /**
     * name of the drink.
     */
    private String name;
    /**
     * id of the drink<br>usually set after the download to find the object in the database.
     */
    private String id;
    /**
     * The price to buy a crate.
     */
    private float priceBuy;
    /**
     * The price a whole crate is to be sold.
     */
    private float priceSell;
    /**
     * The price just one bottle is to be sold.
     */
    private float priceBottle;

    /**
     *
     */
    public Drink() {
    }

    /**
     * @param name        The name of the drink
     * @param priceBuy    The price to buy a crate
     * @param priceSell   The price a whole crate is to be sold
     * @param priceBottle The price just one bottle is to be sold
     */
    public Drink(String name, float priceBuy, float priceSell, float priceBottle) {
        this.name = name;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.priceBottle = priceBottle;
    }

    public String getViewString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(float priceBuy) {
        this.priceBuy = priceBuy;
    }

    public float getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(float priceSell) {
        this.priceSell = priceSell;
    }

    public float getPriceBottle() {
        return priceBottle;
    }

    public void setPriceBottle(float priceBottle) {
        this.priceBottle = priceBottle;
    }

    @Override
    public boolean validate() {
        return (!name.isEmpty());
    }
}
