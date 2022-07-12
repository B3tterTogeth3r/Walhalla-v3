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

package de.b3ttertogeth3r.walhalla.mock;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.interfaces.IAuth;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public class AuthMock implements IAuth {
    private static final String TAG = "AuthMock";

    public AuthMock() {
        ToastList.addToast(Toast.makeToast(App.getContext(), TAG + "-DATA"));
    }

    @Override
    public Loader<AuthResult> signIn(String email, String password) {
        Loader<AuthResult> loader = new Loader<>();
        return loader.done();
    }

    @Override
    public boolean isSignIn() {
        return true;
    }

    @Override
    public Loader<Boolean> changePassword(Person p, String oldPW, String newPW) {
        Loader<Boolean> loader = new Loader<>();
        return loader.done();
    }

    @Override
    public void signOut() {

    }

    @Override
    public void changeLoggingData() {

    }

    @Override
    public FirebaseUser getUser() {
        return null;
    }

    @Override
    public void sendVerificationMail() {

    }

    @Override
    public Loader<Boolean> exitsEmail(String email) {
        Loader<Boolean> loader = new Loader<>();
        return loader.done(false);
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
