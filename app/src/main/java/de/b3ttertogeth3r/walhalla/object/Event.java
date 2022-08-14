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

import com.google.firebase.Timestamp;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.annotation.EventValue;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.interfaces.object.IEvent;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

/**
 * @author B3tterTogeth3r
 * @version 3.0
 * @see de.b3ttertogeth3r.walhalla.abstract_generic.MyObject
 * @see Validate
 * @since 1.0
 */
public class Event extends MyObject implements IEvent, Cloneable {
    private String id;
    private String title;
    private String description;
    private Timestamp end;
    private Collar collar;
    private Punctuality punctuality;
    private Visibility visibility;

    public Event() {
    }

    public Event(String id, String title, String description, Timestamp start, Timestamp end,
                 Collar collar, Punctuality punctuality, Visibility visibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = start;
        this.end = end;
        this.collar = collar;
        this.punctuality = punctuality;
        this.visibility = visibility;
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

    @Override
    public boolean validate() {
        if (getVisibility() == null) {
            setVisibility(Visibility.PUBLIC);
        }
        if (getPunctuality() == null) {
            setPunctuality(Punctuality.CT);
        }
        if (getCollar() == null) {
            setCollar(Collar.O);
        }
        return (getTime() != null && !getTitle().isEmpty());
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Punctuality getPunctuality() {
        return punctuality;
    }

    public Collar getCollar() {
        return collar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCollar(Collar collar) {
        this.collar = collar;
    }

    public void setPunctuality(Punctuality punctuality) {
        this.punctuality = punctuality;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public de.b3ttertogeth3r.walhalla.design.Event getView(FragmentActivity context) {
        return de.b3ttertogeth3r.walhalla.design.Event.create(context, null, this);
    }

    @Override
    public Object getValue(@EventValue int valueID) {
        switch (valueID) {
            case ID:
                return getId();
            case TITLE:
                return getTitle();
            case DESCRIPTION:
                return getDescription();
            case COLLAR:
                return getCollar();
            case PUNCTUALITY:
                return getPunctuality();
            case TIME:
                return getTime();
            case END:
                return getEnd();
            case VISIBILITY:
                return getVisibility();
            default:
                return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setValue(@EventValue int valueID, @NonNull Object object) {
        try {
            switch (valueID) {
                case ID:
                    setId((String) object);
                    break;
                case TITLE:
                    setTitle((String) object);
                    break;
                case DESCRIPTION:
                    setDescription((String) object);
                    break;
                case COLLAR:
                    setCollar((Collar) object);
                    break;
                case PUNCTUALITY:
                    setPunctuality((Punctuality) object);
                    break;
                case TIME:
                    setTime((Timestamp) object);
                    break;
                case END:
                    setEnd((Timestamp) object);
                    break;
                case VISIBILITY:
                    setVisibility((Visibility) object);
                    break;
                default:
                    break;
            }
        } catch (Exception ignored) {
        }
    }

    @NonNull
    @Override
    public Event clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Event) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
