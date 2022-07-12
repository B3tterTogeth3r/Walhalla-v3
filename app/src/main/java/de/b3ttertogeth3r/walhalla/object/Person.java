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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;
import de.b3ttertogeth3r.walhalla.interfaces.Validate;
import de.b3ttertogeth3r.walhalla.util.TrafficLight;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Person implements Validate {
    // TODO: 05.07.22 add possibility to ad titles i.e. a phd.
    private String first_Name;
    private String last_Name;
    private String origin;
    private String major;
    private String nickname;
    private String passwordString;
    private String mobile;
    private String mail;
    private int joined;
    private Timestamp birthday;
    private Rank rank;
    private boolean enabled, password;
    private String id;

    //region constructors
    public Person() {
    }

    public Person(String first_Name, String last_Name, String origin, String major,
                  String nickname, String passwordString, int joined, Timestamp birthday,
                  Rank rank, boolean enabled, boolean password) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.origin = origin;
        this.major = major;
        this.nickname = nickname;
        this.passwordString = passwordString;
        this.joined = joined;
        this.birthday = birthday;
        this.rank = rank;
        this.enabled = enabled;
        this.password = password;
    }
    //endregion

    //region getter and setters
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public TableRow getViewAll(Context context) {
        TableRow row = new TableRow(context);
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getFull_Name()));
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getNickname()));
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getRank().toString()));
        TrafficLight tl;
        if (!enabled) {
            tl = new TrafficLight(context, TrafficLightColor.RED);
        } else if (!password) {
            tl = new TrafficLight(context, TrafficLightColor.YELLOW);
        } else {
            tl = new TrafficLight(context, TrafficLightColor.GREEN);
        }
        row.addView(tl);
        row.addView(new Image(context, R.drawable.ic_arrow_right));
        return row;
    }

    public String getFull_Name() {
        return getFirst_Name() + " " + getLast_Name();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    /**
     * @param context context to create the layout and the text fields
     * @return LinearLayout filled with {@link ProfileRow} elements
     */
    public LinearLayout getViewEdit(Context context, View.OnClickListener listener) {
        return designDisplayEdit(context, true, listener);
    }

    @NonNull
    private LinearLayout designDisplayEdit(Context context, boolean isEdit, @Nullable View.OnClickListener listener) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        if ((getFirst_Name() != null && getLast_Name() != null &&
                !getFirst_Name().isEmpty() && !getLast_Name().isEmpty()) || isEdit) {
            ProfileRow name = new ProfileRow(context, isEdit);
            name.setTitle(R.string.full_name).setContent(getFull_Name()).setOnClickListener(listener);
            layout.addView(name);
        }

        if (getOrigin() != null && !getOrigin().isEmpty() || isEdit) {
            ProfileRow from = new ProfileRow(context, isEdit);
            from.setTitle(R.string.pob).setContent(getOrigin()).setOnClickListener(listener);
            layout.addView(from);
        }

        if (getMajor() != null && !getMajor().isEmpty() || isEdit) {
            ProfileRow major = new ProfileRow(context, isEdit);
            major.setTitle(R.string.major).setContent(getMajor()).setOnClickListener(listener);
            layout.addView(major);
        }

        if (getNickname() != null && !getNickname().isEmpty() || isEdit) {
            ProfileRow nick = new ProfileRow(context, isEdit);
            nick.setTitle(R.string.nickname).setContent(getNickname()).setOnClickListener(listener);
            layout.addView(nick);
        }

        if (getJoined() != 0 || isEdit) {
            ProfileRow join = new ProfileRow(context, isEdit);
            join.setTitle(R.string.joined).setContent(String.valueOf(getJoined())).setOnClickListener(listener);
            layout.addView(join);
        }

        if (getBirthday() != null || isEdit) {
            ProfileRow dob = new ProfileRow(context, isEdit);
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
            dob.setTitle(R.string.dob).setContent(date.format(getBirthday().toDate())).setOnClickListener(listener);
            layout.addView(dob);
        }

        if (getRank() != null || isEdit) {
            ProfileRow ra = new ProfileRow(context, isEdit);
            ra.setTitle(R.string.rank).setContent(getRank().toString()).setOnClickListener(listener);
            layout.addView(ra);
        }

        return layout;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getJoined() {
        return joined;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }

    public LinearLayout getViewDisplay(Context context) {
        return designDisplayEdit(context, false, null);
    }
    //endregion

    @Override
    public boolean validate() {
        return (this.first_Name != null && !this.first_Name.isEmpty() &&
                this.last_Name != null && !this.last_Name.isEmpty() &&
                rank != null && joined != 0);
    }

    public boolean validatePersonal() {
        return (this.first_Name != null && !this.first_Name.isEmpty() &&
                this.last_Name != null && !this.last_Name.isEmpty());
    }
}
