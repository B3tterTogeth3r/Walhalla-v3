package de.b3ttertogeth3r.walhalla.models;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Account {
    private float income = 0f;
    private float expense = 0f;
    private String eventID, purpose, add, recipe_path;
    private Timestamp date = new Timestamp(new Date());

    public Account () {
    }

    public Account (float income, float expense, String eventID, String purpose, String add,
                    String recipe_path) {
        this.income = income;
        this.expense = expense;
        this.eventID = eventID;
        this.purpose = purpose;
        this.add = add;
        this.recipe_path = recipe_path;
    }

    public Account (@NonNull Drink drink) {
        this.expense = drink.getAmount() * drink.getPrice();
        this.eventID = "beer";
        this.add = drink.getKind();
        this.date = drink.getDate();
        this.purpose = drink.getUid();
    }

    public float getIncome () {
        return income;
    }

    public void setIncome (float income) {
        this.income = income;
    }

    public float getExpense () {
        return expense;
    }

    public void setExpense (float expense) {
        this.expense = expense;
    }

    public String getEventID () {
        return eventID;
    }

    public void setEventID (String eventID) {
        this.eventID = eventID;
    }

    public String getPurpose () {
        return purpose;
    }

    public void setPurpose (String purpose) {
        this.purpose = purpose;
    }

    public String getAdd () {
        return add;
    }

    public void setAdd (String add) {
        this.add = add;
    }

    public String getRecipe_path () {
        return recipe_path;
    }

    public void setRecipe_path (String recipe_path) {
        this.recipe_path = recipe_path;
    }

    public Timestamp getDate () {
        return date;
    }

    public void setDate (Timestamp date) {
        this.date = date;
    }
}
