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

package de.b3ttertogeth3r.walhalla.object;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;

public class PersonLight {
    private String id;
    private String full_Name;
    private String nickname;
    private boolean enabled;
    private boolean password;
    private TrafficLightColor color;

    public PersonLight() {
    }

    public PersonLight(String id, String full_Name, String nickname, boolean enabled, boolean password, TrafficLightColor color) {
        this.id = id;
        this.full_Name = full_Name;
        this.nickname = nickname;
        this.enabled = enabled;
        this.password = password;
        this.color = color;
    }

    @Exclude
    public View getView(@NonNull Context context) {
        if (this.color == null) {
            return new ProfileRow(context, this.full_Name, this.nickname, TrafficLightColor.BLACK);
        } else {
            return new ProfileRow(context, this.full_Name, this.nickname, this.color);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_Name() {
        return full_Name;
    }

    public void setFull_Name(String full_Name) {
        this.full_Name = full_Name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public TrafficLightColor getColor() {
        return color;
    }

    public void setColor(TrafficLightColor color) {
        this.color = color;
    }
}
