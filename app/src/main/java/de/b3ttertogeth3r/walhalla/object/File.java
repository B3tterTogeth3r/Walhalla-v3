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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.MyObject;
import de.b3ttertogeth3r.walhalla.design.Date;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.interfaces.Validate;

public class File extends MyObject implements Validate {
    private String name;
    private String path;
    private String uploadedBy;
    private String description;
    private String id;

    public File () {
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public TableRow getView (Context context) {
        TableRow layout = new TableRow(context);
        layout.addView(new Date(context, time));
        //layout with the title and the description
        LinearLayout titleDescription = new LinearLayout(context);
        titleDescription.addView(new Title(context, name));
        titleDescription.addView(new Text(context, description));
        layout.addView(titleDescription);
        layout.addView(new Image(context, R.drawable.ic_arrow_right));
        layout.setOnClickListener(v -> MainActivity.openExternal.file(this)
        );
        return layout;
    }

    @NonNull
    @Override
    public String toString () {
        return name + " uploaded by " + uploadedBy;
    }


    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getPath () {
        return path;
    }

    public void setPath (String path) {
        this.path = path;
    }

    public String getUploadedBy () {
        return uploadedBy;
    }

    public void setUploadedBy (String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    @Override
    public Timestamp getTime () {
        return time;
    }

    @Override
    public void setTime (Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean validate () {
        return false;
    }
}
