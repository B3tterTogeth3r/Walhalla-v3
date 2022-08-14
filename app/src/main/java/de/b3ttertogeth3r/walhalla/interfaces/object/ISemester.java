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

package de.b3ttertogeth3r.walhalla.interfaces.object;

import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;

/**
 * An interface for the {@link de.b3ttertogeth3r.walhalla.object.Semester Semester} Object.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 */
public interface ISemester {
    /**
     * The ID of the semester
     *
     * @see de.b3ttertogeth3r.walhalla.annotation.SemesterRange SemesterRange
     */
    int ID = -1;
    /**
     * The long name of the semester
     */
    int NAME_LONG = 0;
    /**
     * The short name of the semester
     */
    int NAME_SHORT = 1;
    /**
     * The time the semester starts.
     */
    int START_TIME = 2;
    /**
     * The time the semester ends.
     */
    int END_TIME = 3;

    @SemesterRange
    int getId();

    void setId(@SemesterRange int id);
}
