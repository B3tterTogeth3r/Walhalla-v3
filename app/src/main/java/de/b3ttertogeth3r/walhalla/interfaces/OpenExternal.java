package de.b3ttertogeth3r.walhalla.interfaces;

import android.content.Intent;


import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.enums.Walhalla;

public interface OpenExternal {
    default void browser () {
        browser(Walhalla.WEBSITE.toString());
    }

    void browser (String link);

    default void email () {
        email(Walhalla.EMAIL_INFO.toString());
    }

    default void email (String emailOrSubject) {
        if (emailOrSubject == null) {
            email(Walhalla.EMAIL_SENIOR.toString());
        } else if (emailOrSubject.contains("@")) {
            email(emailOrSubject, "");
        } else {
            email(Walhalla.EMAIL_SENIOR.toString(), emailOrSubject);
        }
    }

    void email (@NotNull String email, String subject);
    void intentOpener(Intent intent, int resultCode);
}
