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

import com.google.firebase.Timestamp;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

/**
 * @author B3tterTogeth3r
 * @version 3.0
 * @see de.b3ttertogeth3r.walhalla.abstract_generic.MyObject
 * @see Validate
 * @since 1.0
 */
public class Event extends MyObject implements Validate, Cloneable {
    private Timestamp end;
    private String title;
    private String description;
    private Collar collar;
    private Punctuality punctuality;
    private Visibility visibility;
    private String id;

    public Event() {
    }

    public Event(Timestamp start, Timestamp end, String title, String description, Collar collar,
                 Punctuality punctuality, Visibility visibility) {
        this.time = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.collar = collar;
        this.punctuality = punctuality;
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collar getCollar() {
        return collar;
    }

    public void setCollar(Collar collar) {
        this.collar = collar;
    }

    public Punctuality getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Punctuality punctuality) {
        this.punctuality = punctuality;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public Event clone() {
        try {
            Event clone = (Event) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
