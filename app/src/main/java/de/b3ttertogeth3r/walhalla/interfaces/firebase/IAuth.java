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

package de.b3ttertogeth3r.walhalla.interfaces.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.object.Person;

/**
 * Interface collection used by {@link de.b3ttertogeth3r.walhalla.firebase.Authentication
 * Authentication}
 * <br>It contains the functions:
 * <ul>
 *     <li>{@link #signIn(String, String)}</li>
 *     <li>{@link #isSignIn()}</li>
 *     <li>{@link #changePassword(Person, String)}</li>
 *     <li>{@link #changePassword(Person, String, String)}</li>
 *     <li>{@link #signOut()}</li>
 *     <li>{@link #changeLoggingData()}</li>
 *     <li>{@link #getUser()}</li>
 *     <li>{@link #sendVerificationMail()}</li>
 *     <li>{@link #exitsEmail(String)}</li>
 *     <li>{@link #linkGoogle(Loader)}</li>
 *     <li>{@link #linkGithub(Loader)}</li>
 *     <li>{@link #linkTwitter(Loader)}</li>
 *     <li>{@link #linkFacebook(Loader)}</li>
 *     <li>{@link #linkApple(Loader)}</li>
 *     <li>{@link #linkMicrosoft(Loader)}</li>
 *     <li>{@link #linkYahoo(Loader)}</li>
 *     <li>{@link #addAuthStateListener(FirebaseAuth.AuthStateListener)}</li>
 *     <li>{@link #removeAuthListener(FirebaseAuth.AuthStateListener)}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public interface IAuth {

    /**
     * @param email    email address of the user
     * @param password password to use with the sign in
     * @since 1.0
     */
    Loader<AuthResult> signIn(String email, String password);

    /**
     * @return true, if a user is signed in
     * @since 1.0
     */
    boolean isSignIn();

    /**
     * @param p     Person to change the password of
     * @param newPW the new password
     * @return loader result listener
     * @since 1.0
     */
    default Loader<Boolean> changePassword(Person p, String newPW) {
        return changePassword(p, "Walhalla1684", newPW);
    }

    /**
     * @param p     Person to change the password of
     * @param oldPW the old password
     * @param newPW the new password
     * @return loader result listener
     * @since 1.0
     */
    Loader<Boolean> changePassword(Person p, String oldPW, String newPW);

    /**
     * sign the current user out
     *
     * @since 1.0
     */
    void signOut();

    /**
     * change the ability of the app to log custom events and use analytics data to display custom
     * ads.
     *
     * @since 1.0
     */
    void changeLoggingData();

    /**
     * @return the current signed in Firebase user or null
     * @since 1.0
     */
    FirebaseUser getUser();

    /**
     * send an email with a verification link to the email address of the signed in user
     *
     * @since 1.0
     */
    void sendVerificationMail();

    /**
     * @param email email to check for
     * @return true, if email is in auth
     * @since 1.0
     */
    Loader<Boolean> exitsEmail(String email);

    /**
     * link the account with Google
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/google-signin">Authenticate with
     * Google on Android</a>
     * @since 1.0
     */
    void linkGoogle(@NonNull Loader<Void> loader);

    /**
     * Link the account with Twitter
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/twitter-login">Authenticate Using
     * Twitter on Android</a>
     * @since 1.0
     */
    void linkTwitter(@NonNull Loader<Void> loader);

    /**
     * Link the account with FaceBook
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/facebook-login">Authenticate
     * Using Facebook Login on Android</a>
     * @since 1.0
     */
    void linkFacebook(@NonNull Loader<Void> loader);

    /**
     * Link the account with GitHub
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/github-auth">Authenticate Using
     * GitHub on Android</a>
     * @since 1.0
     */
    void linkGithub(@NonNull Loader<Void> loader);

    /**
     * Link the account with Apple
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/apple">Authenticate Using Apple
     * on Android</a>
     * @since 1.0
     */
    void linkApple(@NonNull Loader<Void> loader);

    /**
     * Link the account with Microsoft
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/microsoft-oauth">Authenticate
     * Using Microsoft on Android</a>
     * @since 1.0
     */
    void linkMicrosoft(@NonNull Loader<Void> loader);

    /**
     * Link the account with Yahoo
     *
     * @param loader result listener
     * @see <a href="https://firebase.google.com/docs/auth/android/yahoo-oauth">Authenticate Using
     * Yahoo on Android</a>
     * @since 1.0
     */
    void linkYahoo(@NonNull Loader<Void> loader);

    /**
     * Set a custom auth state listener
     *
     * @param authStateListener listener
     * @see <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.AuthStateListener">FirebaseAuth.AuthStateListener on Android</a>
     * @since 1.1
     */
    void addAuthStateListener(FirebaseAuth.AuthStateListener authStateListener);

    /**
     * Remove a custom auth state listener
     *
     * @param authStateListener listener
     * @see <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth.AuthStateListener">FirebaseAuth.AuthStateListener on Android</a>
     * @since 1.1
     */
    void removeAuthListener(FirebaseAuth.AuthStateListener authStateListener);
}
