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

package de.b3ttertogeth3r.walhalla.object;

import android.content.Context;
import android.widget.TableLayout;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;
import de.b3ttertogeth3r.walhalla.interfaces.Validate;
import de.b3ttertogeth3r.walhalla.util.TrafficLight;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Person implements Validate {
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
    public Person () {
    }

    public Person (String first_Name, String last_Name, String origin, String major,
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
    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }

    public Timestamp getBirthday () {
        return birthday;
    }

    public void setBirthday (Timestamp birthday) {
        this.birthday = birthday;
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPassword () {
        return password;
    }

    public void setPassword (boolean password) {
        this.password = password;
    }

    public String getMail () {
        return mail;
    }

    public void setMail (String mail) {
        this.mail = mail;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getPasswordString () {
        return passwordString;
    }

    public void setPasswordString (String passwordString) {
        this.passwordString = passwordString;
    }

    //region getView
    public TableRow getViewAll (Context context) {
        TableRow row = new TableRow(context);
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getFull_Name()));
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getNickname()));
        row.addView(new de.b3ttertogeth3r.walhalla.design.Text(context, getRank().toString()));
        TrafficLight tl;
        if (!enabled) {
            tl= new TrafficLight(context, TrafficLightColor.RED);
        } else if (!password) {
            tl = new TrafficLight(context, TrafficLightColor.YELLOW);
        } else {
            tl = new TrafficLight(context, TrafficLightColor.GREEN);
        }
        row.addView(tl);
        row.addView(new Image(context, R.drawable.ic_arrow_right));
        return row;
    }

    public String getFull_Name () {
        return getFirst_Name() + " " + getLast_Name();
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname (String nickname) {
        this.nickname = nickname;
    }

    public Rank getRank () {
        return rank;
    }

    public void setRank (Rank rank) {
        this.rank = rank;
    }

    public String getFirst_Name () {
        return first_Name;
    }

    public void setFirst_Name (String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name () {
        return last_Name;
    }

    public void setLast_Name (String last_Name) {
        this.last_Name = last_Name;
    }

    /**
     * @param context
     *         context to create the layout and the text fields
     * @return TableLayout
     */
    public TableLayout getViewEdit (Context context) {
        return designDisplayEdit(context, true);
    }

    @NonNull
    private TableLayout designDisplayEdit (Context context, boolean isEdit) {
        TableLayout layout = new TableLayout(context);
        TableRow name = new TableRow(context);
        name.addView(new Text(context, context.getString(R.string.full_name)));
        name.addView(new Text(context, getFull_Name()));
        if (isEdit) {
            name.addView(new Image(context, R.drawable.ic_arrow_right));
            name.setOnClickListener(null);
        }
        layout.addView(name);

        TableRow from = new TableRow(context);
        from.addView(new Text(context, context.getString(R.string.full_name)));
        from.addView(new Text(context, getOrigin()));
        if (isEdit) {
            from.addView(new Image(context, R.drawable.ic_arrow_right));
            from.setOnClickListener(null);
        }
        layout.addView(from);

        TableRow major = new TableRow(context);
        major.addView(new Text(context, context.getString(R.string.full_name)));
        major.addView(new Text(context, getMajor()));
        if (isEdit) {
            major.addView(new Image(context, R.drawable.ic_arrow_right));
            major.setOnClickListener(null);
        }
        layout.addView(major);

        TableRow nick = new TableRow(context);
        nick.addView(new Text(context, context.getString(R.string.full_name)));
        nick.addView(new Text(context, getNickname()));
        if (isEdit) {
            nick.addView(new Image(context, R.drawable.ic_arrow_right));
            nick.setOnClickListener(null);
        }
        layout.addView(nick);

        TableRow join = new TableRow(context);
        join.addView(new Text(context, context.getString(R.string.full_name)));
        join.addView(new Text(context, String.valueOf(getJoined())));
        if (isEdit) {
            join.addView(new Image(context, R.drawable.ic_arrow_right));
            join.setOnClickListener(null);
        }
        layout.addView(join);

        TableRow dob = new TableRow(context);
        dob.addView(new Text(context, context.getString(R.string.full_name)));
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
        dob.addView(new Text(context, date.format(birthday.toDate())));
        if (isEdit) {
            dob.addView(new Image(context, R.drawable.ic_arrow_right));
            dob.setOnClickListener(null);
        }
        layout.addView(dob);

        TableRow ra = new TableRow(context);
        ra.addView(new Text(context, context.getString(R.string.full_name)));
        ra.addView(new Text(context, getRank().toString()));
        if (isEdit) {
            ra.addView(new Image(context, R.drawable.ic_arrow_right));
            ra.setOnClickListener(null);
        }
        layout.addView(ra);

        return layout;
    }

    public String getOrigin () {
        return origin;
    }

    public void setOrigin (String origin) {
        this.origin = origin;
    }

    public String getMajor () {
        return major;
    }
    //endregion

    public void setMajor (String major) {
        this.major = major;
    }

    public int getJoined () {
        return joined;
    }

    public void setJoined (int joined) {
        this.joined = joined;
    }

    public TableLayout getViewDisplay (Context context) {
        return designDisplayEdit(context, false);
    }
    //endregion

    @Override
    public boolean validate () {
        return false;
    }
}
