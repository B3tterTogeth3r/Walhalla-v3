/*
 * Copyright (c) 2022-2023.
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
import java.util.concurrent.atomic.AtomicInteger;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.exception.SignInException;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Log;

public class Authentication implements IInit, IAuth {
    private static final String TAG = "Authentication";
    protected static IAuth iAuth;
    private static FirebaseAuth AUTH;

    @Override
    public boolean init(Context context, boolean isEmulator) {
        try {
            AUTH = FirebaseAuth.getInstance();
            if (isEmulator) {
                AUTH.useEmulator("10.0.2.2", 9099);
            }
            iAuth = this;
        } catch (Exception e) {
            Log.e(TAG, "Init didn't work", e);
            AUTH = null;
        }
        return AUTH != null;
    }

    @Override
    public Loader<AuthResult> signIn(String email, String password) {
        Loader<AuthResult> loader = new Loader<>();
        AtomicInteger run = new AtomicInteger();
        if (AUTH != null) {
            try {
                AUTH.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (run.get() == 0) {
                                run.set(1);
                                if (task.getException() != null) {
                                    Exception e = task.getException();
                                    loader.done(e);
                                    return;
                                }
                                loader.done(task.getResult());
                            }
                        });
            } catch (Exception e) {
                loader.done(e);
            }
        } else {
            return loader.done(new NullPointerException("Auth == null"));
        }
        return loader;
    }

    @Override
    public boolean isSignIn() {
        return (AUTH != null && AUTH.getCurrentUser() != null);
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
        Cache.CACHE_DATA.reset();
        AUTH.signOut();
    }

    @Override
    public void changeLoggingData() {

    }

    @Override
    @Nullable
    public FirebaseUser getUser() {
        if (isSignIn()) {
            return AUTH.getCurrentUser();
        }
        return null;
    }

    @Override
    public void sendVerificationMail() {

    }

    @Override
    public Loader<Boolean> exitsEmail(String email) {
        Loader<Boolean> loader = new Loader<>();
        AtomicInteger run = new AtomicInteger();
        if (AUTH != null) {
            AUTH.signInWithEmailAndPassword(email, "Walhalla1864")
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null && run.get() != 0) {
                            Exception e = task.getException();
                            if (Objects.requireNonNull(e.getMessage()).contains("deleted")) {
                                loader.done(false);
                            } else if (Objects.requireNonNull(e.getMessage()).contains("password")) {
                                loader.done(true);
                            } else if (Objects.requireNonNull(e.getMessage()).contains("to connect to")) {
                                loader.done(new SignInException("app couldn't connect to the emulator"));//, e));
                            } else {
                                loader.done(e);
                            }
                            return;
                        }
                        run.set(1);
                        loader.done(true);
                    });
        } else {
            return loader.done(new SignInException("Auth == null"));
        }
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

    @Override
    public void addAuthStateListener(FirebaseAuth.AuthStateListener authStateListener) {
        AUTH.addAuthStateListener(authStateListener);
    }

    @Override
    public void removeAuthListener(FirebaseAuth.AuthStateListener authStateListener) {
        AUTH.removeAuthStateListener(authStateListener);
    }

    @Override
    public boolean checkUser() {
        return AUTH.getCurrentUser() != null;
    }
}
