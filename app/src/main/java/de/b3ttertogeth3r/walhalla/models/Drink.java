package de.b3ttertogeth3r.walhalla.models;

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
