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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

public class Chore extends MyObject implements Validate {
    String id;
    DocumentReference toEvent;
    DocumentReference toPerson;
    boolean done = false;
    de.b3ttertogeth3r.walhalla.enums.Chore chore;
    /**
     * {@link Event#TITLE Event.ID} of the event
     */
    String event;
    /**
     * {@link de.b3ttertogeth3r.walhalla.object.Person#FIRST_NAME Person.FULL_NAME} of the Person
     */
    String person;

    public Chore() {
    }

    public DocumentReference getToPerson() {
        return toPerson;
    }

    public void setToPerson(DocumentReference toPerson) {
        this.toPerson = toPerson;
    }

    public Timestamp getDueDate() {
        return time;
    }

    public void setDueDate(Timestamp dueDate) {
        this.time = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean validate() {
        return (getToEvent() != null && getChore() != null
                && !getPerson().isEmpty());
    }

    public DocumentReference getToEvent() {
        return toEvent;
    }

    public void setToEvent(DocumentReference toEvent) {
        this.toEvent = toEvent;
    }

    public de.b3ttertogeth3r.walhalla.enums.Chore getChore() {
        return chore;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setChore(de.b3ttertogeth3r.walhalla.enums.Chore chore) {
        this.chore = chore;
    }

    @NonNull
    @Override
    public String toString() {
        if (getId() != null) {
            return "Chore ID:" + getId();
        } else if (getChore() != null) {
            return "Chore: " + getChore().name();
        }
        return super.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
