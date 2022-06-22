/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old.interfaces;

import android.content.Intent;

import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.old.enums.Walhalla;

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

    void intentOpener (Intent intent, int resultCode);

    void switchFragment(int resId);
}
