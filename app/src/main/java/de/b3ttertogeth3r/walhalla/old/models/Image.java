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

public class Image {
    @Exclude
    private String id;
    private String description;
    private String icon_path;
    private String large_path;

    public Image () {
    }

    public Image (String description, String icon_path, String large_path) {
        this.description = description;
        this.icon_path = icon_path;
        this.large_path = large_path;
    }

    @Exclude
    public String getId () {
        return id;
    }

    @Exclude
    public void setId (String id) {
        this.id = id;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getIcon_path () {
        return icon_path;
    }

    public void setIcon_path (String icon_path) {
        this.icon_path = icon_path;
    }

    public String getLarge_path () {
        return large_path;
    }

    public void setLarge_path (String large_path) {
        this.large_path = large_path;
    }
}
