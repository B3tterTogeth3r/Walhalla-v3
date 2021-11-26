package de.b3ttertogeth3r.walhalla.abstraction;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged (CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged (@NonNull Editable s) {
        textChanged(s.toString());
    }

    public abstract void textChanged (String s);

    @NonNull
    @Override
    public String toString () {
        return "MyTextWatcher";
    }

}
