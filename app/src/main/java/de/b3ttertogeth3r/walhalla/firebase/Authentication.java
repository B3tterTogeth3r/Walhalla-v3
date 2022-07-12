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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Log;

public class Authentication implements IInit {
    private static final String TAG = "Authentication";
    private static FirebaseAuth Auth = null;

    public static void signIn (String email, String password, Loader<AuthResult> loader) {
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        loader.done(task.getException());
                        return;
                    }
                    loader.done(task.getResult());
                });
    }

    @Override
    public boolean init (Context context) {
        try {
            Auth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Log.e(TAG, "Init didn't work", e);
            Auth = null;
        }
        return Auth != null;
    }
}
