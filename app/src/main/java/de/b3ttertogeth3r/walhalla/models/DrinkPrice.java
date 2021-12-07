package de.b3ttertogeth3r.walhalla.models;

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
