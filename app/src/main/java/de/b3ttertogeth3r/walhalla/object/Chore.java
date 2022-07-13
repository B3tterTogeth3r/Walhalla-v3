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
import android.widget.LinearLayout;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.abstract_classes.MyObject;
import de.b3ttertogeth3r.walhalla.design.Date;
import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.TimeFrat;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

public class Chore extends MyObject implements Validate {
    String id;
    DocumentReference toEvent;
    Timestamp dueDate;
    boolean done;
    de.b3ttertogeth3r.walhalla.enums.Chore chore;
    String event;
    String person;

    public Chore () {
    }

    public TableRow getView (Context context, boolean isEvent) {
        TableRow row = new TableRow(context);
        row.addView(new Date(context, dueDate));
        row.addView(new TimeFrat(context, dueDate));
        LinearLayout right = new LinearLayout(context);
        right.setOrientation(LinearLayout.HORIZONTAL);
        if(isEvent) {
            Text eventName = new Text(context, event);
            right.addView(eventName);
        } else {
            Text personName = new Text(context, person);
            right.addView(personName);
        }
        Text choreName = new Text(context, chore.getDescription(context));
        right.addView(choreName);
        row.addView(right);
        return row;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public DocumentReference getToEvent () {
        return toEvent;
    }

    public void setToEvent (DocumentReference toEvent) {
        this.toEvent = toEvent;
    }

    public Timestamp getDueDate () {
        return dueDate;
    }

    public void setDueDate (Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone () {
        return done;
    }

    public void setDone (boolean done) {
        this.done = done;
    }

    public de.b3ttertogeth3r.walhalla.enums.Chore getChore () {
        return chore;
    }

    public void setChore (de.b3ttertogeth3r.walhalla.enums.Chore chore) {
        this.chore = chore;
    }

    public String getEvent () {
        return event;
    }

    public void setEvent (String event) {
        this.event = event;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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
