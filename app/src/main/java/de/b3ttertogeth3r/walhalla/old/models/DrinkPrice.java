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

public class DrinkPrice {
    private boolean available = true;
    private int priceBuy = 0;
    private float priceSell = 0f;
    private String name;

    /**
     * empty constructor
     *
     * @see #DrinkPrice(boolean, int, int, String) full constructor
     * @since 1.0
     */
    public DrinkPrice () {
    }

    /**
     * @param priceBuy
     *         price to buy a crate of beer
     * @param priceSell
     *         the price per bottle selling
     * @param name
     *         the name of the drink
     * @since 1.0
     */
    public DrinkPrice (boolean available, int priceBuy, int priceSell, String name) {
        this.available = available;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.name = name;
    }

    public int getPriceBuy () {
        return priceBuy;
    }

    public void setPriceBuy (int priceBuy) {
        this.priceBuy = priceBuy;
    }

    public float getPriceSell () {
        return priceSell;
    }

    public void setPriceSell (float priceSell) {
        this.priceSell = priceSell;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public boolean isAvailable () {
        return available;
    }

    public void setAvailable (boolean available) {
        this.available = available;
    }
}
