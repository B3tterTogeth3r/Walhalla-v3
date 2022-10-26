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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.interfaces.object.IFile;

/**
 * Object to store values a file has in Firestore. Every file has an {@link #ID}, a {@link #NAME},
 * a {@link #PATH}, a {@link #UPLOADED_BY} and a {@link #DESCRIPTION}.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see de.b3ttertogeth3r.walhalla.interfaces.object.Validate
 * @see de.b3ttertogeth3r.walhalla.abstract_generic.MyObject
 * @since 2.0
 */
public class File extends MyObject implements IFile {
    private String name;
    private String path;
    private String uploadedBy;
    private String description;
    private String id;

    public File() {
    }

    public File(String name, String path, String uploadedBy, String description) {
        this.name = name;
        this.path = path;
        this.uploadedBy = uploadedBy;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " uploaded by " + uploadedBy;
    }


    public String getViewString() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public boolean validate() {
        return (!name.isEmpty() && !path.isEmpty());
    }

    @Override
    public ProfileRow getView(FragmentActivity activity) {
        ProfileRow row = new ProfileRow(activity, true);
        row.setTitle(getName());
        row.setContent(getDescription());
        return row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
