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

package de.b3ttertogeth3r.walhalla.util;

import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.COLLAR;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.DESCRIPTION;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.END;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.PUNCTUALITY;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.TIME;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.TITLE;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.VISIBILITY;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.BIRTHDAY;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.ENABLED;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.FIRST_NAME;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.ID;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.JOINED;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.LAST_NAME;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.MAIL;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.MAJOR;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.MOBILE;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.NICKNAME;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.ORIGIN;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.PASSWORD;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.PASSWORD_STRING;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IPerson.RANK;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.b3ttertogeth3r.walhalla.interfaces.object.IEvent;

public class Dev {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ID, FIRST_NAME, LAST_NAME, ORIGIN, MAJOR, NICKNAME, PASSWORD_STRING, MOBILE, MAIL, JOINED, BIRTHDAY, ENABLED, RANK, PASSWORD})
    public @interface PersonValue {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IEvent.ID, TITLE, DESCRIPTION, COLLAR, PUNCTUALITY, TIME, END, VISIBILITY})
    public @interface EventValue {
    }

    /**
     * Semester IDs are only allowed to be between 0 and 366
     */
    @IntRange(from = 0, to = 399)
    public @interface SemesterRange {
    }
}
