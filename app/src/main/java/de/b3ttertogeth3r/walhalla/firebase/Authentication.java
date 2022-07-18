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

package de.b3ttertogeth3r.walhalla.firebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;

public class Authentication implements IInit, IAuth {
    private static final String TAG = "Authentication";
    protected static IAuth iAuth;
    private FirebaseAuth Auth = null;

    @Override
    public boolean init(Context context) {
        try {
            Auth = FirebaseAuth.getInstance();
            iAuth = this;
        } catch (Exception e) {
            Log.e(TAG, "Init didn't work", e);
            Auth = null;
        }
        return Auth != null;
    }

    @Override
    public Loader<AuthResult> signIn(String email, String password) {
        Loader<AuthResult> loader = new Loader<>();
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        loader.done(task.getException());
                        return;
                    }
                    loader.done(task.getResult());
                });
        return loader;
    }

    @Override
    public boolean isSignIn() {
        return (Auth != null && Auth.getCurrentUser() != null);
    }

    @Override
    public Loader<Boolean> changePassword(Person p, String newPW) {
        return changePassword(p, "Walhalla1864", newPW);
    }

    @Override
    public Loader<Boolean> changePassword(Person p, String oldPW, String newPW) {
        Loader<Boolean> loader = new Loader<>();
        return loader.done();
    }

    @Override
    public void signOut() {
        Auth.signOut();
    }

    @Override
    public void changeLoggingData() {

    }

    @Override
    @Nullable
    public FirebaseUser getUser() {
        if (isSignIn()) {
            return Auth.getCurrentUser();
        }
        return null;
    }

    @Override
    public void sendVerificationMail() {

    }

    @Override
    public Loader<Boolean> exitsEmail(String email) {
        Loader<Boolean> loader = new Loader<>();
        Auth.signInWithEmailAndPassword(email, "Walhalla1864")
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        if (Objects.requireNonNull(task.getException().getMessage()).contains("deleted")) {
                            loader.done(false);
                        } else if (Objects.requireNonNull(task.getException().getMessage()).contains("password")) {
                            loader.done(true);
                        } else {
                            loader.done(task.getException());
                        }
                        return;
                    }
                    loader.done(true);
                });
        return loader;
    }

    @Override
    public void linkGoogle(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkTwitter(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkFacebook(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkGithub(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkApple(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkMicrosoft(@NonNull Loader<Void> loader) {
        loader.done();
    }

    @Override
    public void linkYahoo(@NonNull Loader<Void> loader) {
        loader.done();
    }
}
