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
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;
import de.b3ttertogeth3r.walhalla.interfaces.object.IPerson;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;
import de.b3ttertogeth3r.walhalla.util.Values;

/**
 * A class to store the data a person has saved in the Firestore database.
 * <p>
 * Also it can create a view of the given data if {@link #getViewAll(Context)},
 * {@link #getViewDisplay(Context)} or {@link #getViewEdit(Context, View.OnClickListener)} is called.
 *
 * @author B3tterTogeth3r
 * @version 2.1
 * @see Validate
 * @see IPerson
 * @since 1.0
 */
public class Person implements IPerson {
    private static final String TAG = "Person";
    // TODO: 05.07.22 add possibility to ad titles i.e. a phd.
    private String first_Name;
    private String last_Name;
    private String origin;
    private String major;
    private String nickname;
    private String passwordString;
    private String mobile;
    private String mail;
    private int joined = -1;
    private Timestamp birthday;
    private Rank rank;
    private boolean enabled = false;
    private boolean password = false;
    private String id;

    //region constructors
    public Person() {
    }

    public Person(String first_Name, String last_Name, String origin, String major,
                  String nickname, String passwordString, String mobile, String mail,
                  int joined, Timestamp birthday, Rank rank, boolean enabled, boolean password, String id) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.origin = origin;
        this.major = major;
        this.nickname = nickname;
        this.passwordString = passwordString;
        this.mobile = mobile;
        this.mail = mail;
        this.joined = joined;
        this.birthday = birthday;
        this.rank = rank;
        this.enabled = enabled;
        this.password = password;
        this.id = id;
    }
    public Person(String first_Name, String last_Name, String origin, String major,
                  String nickname, String passwordString, String mobile, String mail,
                  int joined, Timestamp birthday, Rank rank, boolean enabled, boolean password) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.origin = origin;
        this.major = major;
        this.nickname = nickname;
        this.passwordString = passwordString;
        this.mobile = mobile;
        this.mail = mail;
        this.joined = joined;
        this.birthday = birthday;
        this.rank = rank;
        this.enabled = enabled;
        this.password = password;
    }

    public Person(String first_Name, String last_Name, String origin, String major,
                  String nickname, String mobile, String mail,
                  int joined, Timestamp birthday, Rank rank, boolean enabled, boolean password) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.origin = origin;
        this.major = major;
        this.nickname = nickname;
        this.mobile = mobile;
        this.mail = mail;
        this.joined = joined;
        this.birthday = birthday;
        this.rank = rank;
        this.enabled = enabled;
        this.password = password;
    }

    public Person(String first_Name, String last_Name, String origin, String major,
                  String nickname, String mobile, String mail, int joined, Timestamp birthday,
                  Rank rank) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.origin = origin;
        this.major = major;
        this.nickname = nickname;
        this.mobile = mobile;
        this.mail = mail;
        this.joined = joined;
        this.birthday = birthday;
        this.rank = rank;
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

    @NonNull
    @Override
    public String toString() {
        return TAG + ": " + id + " - " + getFull_Name();
    }

    @Override
    @Exclude
    public boolean validate() {
        return (this.first_Name != null && !this.first_Name.isEmpty() &&
                this.last_Name != null && !this.last_Name.isEmpty() &&
                rank != null && joined != -1);
    }

    @Override
    @Exclude
    public String getFull_Name() {
        return getFirst_Name() + " " + getLast_Name();
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

    @Override
    @Exclude
    public TrafficLightColor getSecurity() {
        if (!validateFratData() || !validatePersonal()) {
            return TrafficLightColor.BLACK;
        }
        if (!enabled) {
            return TrafficLightColor.RED;
        } else if (!password) {
            return TrafficLightColor.YELLOW;
        } else {
            return TrafficLightColor.GREEN;
        }
    }

    @Override
    @Exclude
    public String getValue(int value) {
        return null;
    }

    @Override
    @Exclude
    public void setValue(int value, String content) {
    }

    @Override
    @Exclude
    public View getViewAll(@NonNull Context context) {
        return new ProfileRow(context, getFull_Name(), getNickname(), getSecurity());
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

    /**
     * @param context  context to create the layout and the text fields
     * @param listener OnClickListener for the row
     * @return LinearLayout filled with {@link ProfileRow} elements
     * @since 2.0
     */
    @Exclude
    public LinearLayout getViewEdit(Context context, View.OnClickListener listener) {
        return designDisplayEdit(context, true, listener);
    }

    @Exclude
    @NonNull
    private LinearLayout designDisplayEdit(Context context, boolean isEdit, @Nullable View.OnClickListener listener) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        if ((getMail() != null && !getMail().isEmpty()) || isEdit) {
            ProfileRow name = new ProfileRow(context, false);
            name.setId(R.id.email);
            name.setTitle(R.string.mail).setContent(getMail()).setOnClickListener(listener);
            layout.addView(name);
        }

        if ((getFirst_Name() != null && !getFirst_Name().isEmpty()) || isEdit) {
            ProfileRow name = new ProfileRow(context, isEdit);
            name.setId(R.id.first_name);
            name.setTitle(R.string.first_name).setContent(getFirst_Name()).setOnClickListener(listener);
            layout.addView(name);
        }

        if ((getLast_Name() != null && !getLast_Name().isEmpty()) || isEdit) {
            ProfileRow name = new ProfileRow(context, isEdit);
            name.setId(R.id.last_name);
            name.setTitle(R.string.last_name).setContent(getLast_Name()).setOnClickListener(listener);
            layout.addView(name);
        }

        if ((getMobile() != null && !getMobile().isEmpty()) || isEdit) {
            ProfileRow name = new ProfileRow(context, isEdit);
            name.setId(R.id.mobile);
            name.setTitle(R.string.mobile).setContent(getMobile()).setOnClickListener(listener);
            layout.addView(name);
        }

        if ((getOrigin() != null && !getOrigin().isEmpty()) || isEdit) {
            ProfileRow from = new ProfileRow(context, isEdit);
            from.setId(R.id.from);
            from.setTitle(R.string.pob).setContent(getOrigin()).setOnClickListener(listener);
            layout.addView(from);
        }

        if ((getMajor() != null && !getMajor().isEmpty()) || isEdit) {
            ProfileRow major = new ProfileRow(context, isEdit);
            major.setId(R.id.major);
            major.setTitle(R.string.major).setContent(getMajor()).setOnClickListener(listener);
            layout.addView(major);
        }

        if ((getNickname() != null && !getNickname().isEmpty()) || isEdit) {
            ProfileRow nick = new ProfileRow(context, isEdit);
            nick.setId(R.id.nickname);
            nick.setTitle(R.string.nickname).setContent(getNickname()).setOnClickListener(listener);
            layout.addView(nick);
        }

        if (getJoined() != -1 || isEdit) {
            ProfileRow join = new ProfileRow(context, isEdit);
            join.setId(R.id.joined);
            String semName = context.getString(R.string.dialog_semester_select);
            if (getJoined() != -1) {
                semName = Values.semesterList.get(getJoined()).getName_long();
            }
            join.setTitle(R.string.joined).setContent(semName).setOnClickListener(listener);
            layout.addView(join);
        }

        if (getBirthday() != null || isEdit) {
            ProfileRow dob = new ProfileRow(context, isEdit);
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
            dob.setId(R.id.dob);
            dob.setTitle(R.string.dob).setContent(date.format(getBirthday().toDate())).setOnClickListener(listener);
            layout.addView(dob);
        }

        if (getRank() != null || isEdit) {
            ProfileRow ra = new ProfileRow(context, isEdit);
            ra.setId(R.id.rank);
            ra.setTitle(R.string.rank).setContent(getRank().toString()).setOnClickListener(listener);
            layout.addView(ra);
        }

        return layout;
    }

    public String getMail() {
        return mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    @Exclude
    public LinearLayout getViewDisplay(Context context) {
        return designDisplayEdit(context, false, null);
    }

    @Exclude
    public boolean validatePersonal() {
        return (this.first_Name != null && !this.first_Name.isEmpty() &&
                this.last_Name != null && !this.last_Name.isEmpty());
    }

    @Override
    @Exclude
    public boolean validateFratData() {
        return (this.rank != null && joined != -1);
    }


}
