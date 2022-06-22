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

package de.b3ttertogeth3r.walhalla.old.models;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.b3ttertogeth3r.walhalla.R;

public class ProfileError {
    @Exclude
    private static final String TAG = "ProfileError";
    private int page = R.string.menu_home;
    private boolean isMessage = true;
    private String message;

    public ProfileError () {
    }

    public ProfileError (int page, boolean isMessage, String message) {
        this.page = page;
        this.isMessage = isMessage;
        this.message = message;
    }

    public ProfileError (Set<String> set) {
        List<String> stringSet = new ArrayList<>(set);
        System.out.println(set + "\n" + stringSet);
        if (stringSet.size() == 3) {
            while (!stringSet.isEmpty()) {
                try {
                    this.page = Integer.parseInt(stringSet.get(0));
                } catch (Exception ignored) {
                    if (stringSet.get(0).contains("true") || stringSet.get(0).equals("false")) {
                        this.isMessage = (stringSet.get(0).equals("true"));
                    } else {
                        this.message = stringSet.get(0);
                    }
                }
                stringSet.remove(0);
            }
        }
    }

    public int getPage () {
        return page;
    }

    public void setPage (int page) {
        this.page = page;
    }

    public boolean isMessage () {
        return isMessage;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (boolean message) {
        isMessage = message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    @Exclude
    public Set<String> getSet () {
        Set<String> set = new HashSet<>();
        set.add(String.valueOf(this.page));
        set.add(String.valueOf(this.isMessage));
        set.add(this.message);
        return set;
    }
}
