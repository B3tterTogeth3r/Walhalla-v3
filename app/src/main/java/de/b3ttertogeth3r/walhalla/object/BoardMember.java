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

import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.ALIGN_PARENT_START;

import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.dialog.ChargenDetail;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.interfaces.object.IBoardMember;

public class BoardMember implements IBoardMember {
    private String full_name = "";
    private String mobile = "";
    private String major = "";
    private String mail = "";
    private String from = "";
    @SemesterRange
    private int semester = 0;
    private Charge charge = Charge.NONE;
    private DocumentReference image = null;
    private String uid = "";
    private String id = "";

    public BoardMember(String full_name, Charge charge) {
        this.full_name = full_name;
        this.charge = charge;
    }

    public BoardMember(@NonNull Person person) {
        this.full_name = person.getFull_Name();
        this.mobile = person.getMobile();
        this.major = person.getMajor();
        this.mail = person.getMail();
        this.from = person.getOrigin();
        this.uid = person.getId();
    }

    public BoardMember(String full_name, String mobile, String major, String mail, String from,
                       Charge charge, int semester, DocumentReference image, String uid) {
        this.full_name = full_name;
        this.mobile = mobile;
        this.major = major;
        this.mail = mail;
        this.from = from;
        this.charge = charge;
        this.semester = semester;
        this.image = image;
        this.uid = uid;
    }

    public BoardMember() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @SemesterRange
    public int getSemester() {
        return semester;
    }

    public void setSemester(@SemesterRange int semester) {
        this.semester = semester;
    }

    public RelativeLayout getView(@NonNull FragmentActivity activity) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                120f,
                activity.getResources().getDisplayMetrics()
        );
        RelativeLayout layout = new RelativeLayout(activity);

        layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.border_bottom_black));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layout.setLayoutParams(params);
        LinearLayout llayout = new LinearLayout(activity);
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        linearParams.addRule(ALIGN_PARENT_START);
        llayout.setBackground(null);

        Title title = new Title(activity, getCharge().toLongString());
        llayout.addView(title);

        Text name = new Text(activity, getFull_name());
        name.setPadding(name.getPaddingLeft(), 0, 0, 0);
        llayout.addView(name);

        Text major = new Text(activity);
        major.setPadding(major.getPaddingLeft(), 0, 0, 0);
        String majorStr = activity.getString(R.string.major_short) + " " + getMajor();
        major.setText(majorStr);
        llayout.addView(major);

        Text PoB = new Text(activity);
        PoB.setPadding(PoB.getPaddingLeft(), 0, 0, 0);
        String from = activity.getString(R.string.from) + " " + getFrom();
        PoB.setText(from);
        llayout.addView(PoB);

        Text mobile = new Text(activity, getMobile());
        mobile.setPadding(mobile.getPaddingLeft(), 0, 0, 0);
        llayout.addView(mobile);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                padding, padding);
        imageParams.addRule(ALIGN_PARENT_END);
        imageParams.setMargins(0, (padding / 12), (padding / 12), 0);

        RelativeLayout imageView = new RelativeLayout(activity);
        imageView.setBackground(ContextCompat.getDrawable(activity, R.drawable.border_round));
        ImageView image = new ImageView(activity);
        image.setPadding(4, 4, 4, 4);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.wappen_2017));

        imageView.addView(image);
        layout.addView(llayout, linearParams);
        layout.addView(imageView, imageParams);

        if (getImage() != null) {
            Glide.with(activity)
                    .load(getImage())
                    .placeholder(R.drawable.wappen_2017)
                    .centerCrop()
                    .into(image);
        } else {
            Glide.with(activity)
                    .load(R.drawable.wappen_2017)
                    .placeholder(R.drawable.wappen_2017)
                    .centerCrop()
                    .into(image);
        }

        layout.setOnClickListener(v -> {
            ChargenDetail detail = new ChargenDetail(DialogSize.WRAP_CONTENT, this);
            detail.show(activity.getSupportFragmentManager(), "BoardMember");
        });

        return layout;
    }

    public Charge getCharge() {
        return charge;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getFrom() {
        return from;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public DocumentReference getImage() {
        return image;
    }

    public void setImage(DocumentReference image) {
        this.image = image;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
