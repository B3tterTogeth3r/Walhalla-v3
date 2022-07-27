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

import de.b3ttertogeth3r.walhalla.interfaces.object.ISemester;

public class Semester implements ISemester {
    /**
     * the ID of the semester
     */
    private String id;
    /**
     * The long name of the semester
     */
    private String name_long;
    /**
     * The short name of the semester
     */
    private String name_short;
    /**
     * The time the semester starts.
     */
    private Timestamp start;
    /**
     * The time the semester ends.
     */
    private Timestamp end;

    public Semester() {
    }

    public Semester(int id) {
        this.id = String.valueOf(id);
        this.start = Timestamp.now();
        this.end = Timestamp.now();
    }

    public Semester(String id, String name_long, String name_short, Timestamp start,
                    Timestamp end) {
        this.id = id;
        this.name_long = name_long;
        this.name_short = name_short;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_long() {
        return name_long;
    }

    public void setName_long(String name_long) {
        this.name_long = name_long;
    }

    public String getName_short() {
        return name_short;
    }

    public void setName_short(String name_short) {
        this.name_short = name_short;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
