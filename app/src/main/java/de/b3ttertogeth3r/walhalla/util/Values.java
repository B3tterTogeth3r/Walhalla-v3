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

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig;
import de.b3ttertogeth3r.walhalla.object.Semester;

public class Values {
    public static final Locale LOCALE = new Locale("de", "DE");
    public static final String[] MONTHS = {App.getContext().getString(R.string.month_jan),
            App.getContext().getString(R.string.month_feb), App.getContext().getString(R.string.month_mar),
            App.getContext().getString(R.string.month_apr), App.getContext().getString(R.string.month_may),
            App.getContext().getString(R.string.month_jun), App.getContext().getString(R.string.month_jul),
            App.getContext().getString(R.string.month_aug), App.getContext().getString(R.string.month_sep),
            App.getContext().getString(R.string.month_oct), App.getContext().getString(R.string.month_nov),
            App.getContext().getString(R.string.month_dec)};
    /**
     * Semester list of all semesters until SS 2064
     *
     * @see de.b3ttertogeth3r.walhalla.annotation.SemesterRange SemesterRange
     */
    public static final ArrayList<Semester> semesterList = getSemesters();
    public static Semester currentSemester = getCurrentSemester();

    @NonNull
    private static Semester getCurrentSemester() {
        Timestamp time = Timestamp.now();
        for (Semester s : Values.semesterList) {
            if (s.getStart().toDate().before(time.toDate()) &&
                    s.getEnd().toDate().after(time.toDate())) {
                return s;
            }
        }
        return getSemesters().get(Firebase.remoteConfig().getInt(IRemoteConfig.CURRENT_SEMESTER));
    }

    @NonNull
    private static ArrayList<Semester> getSemesters() {
        ArrayList<Semester> semesters = new ArrayList<>();
        int l = 1864;
        int s = 64;
        for (int i = 0; i < 400; i++) {
            Calendar start = Calendar.getInstance();
            start.set(l, 9, 1, 0, 0, 0);
            Calendar end = Calendar.getInstance();
            end.set((l + 1), 2, 30, 23, 59, 59);
            String longStr = "Wintersemester " + l + " / " + (l + 1);
            String shortStr = "WS" + s + "/" + (s + 1);
            semesters.add(new Semester(i, longStr, shortStr,
                    new Timestamp(start.getTime()), new Timestamp(end.getTime())));

            i++;
            s++;
            l++;
            if (s == 100) {
                s = 0;
            }
            start.set(l, 3, 1, 0, 0, 0);
            end.set(l, 8, 31, 23, 59, 59);
            longStr = "Sommersemester " + l;
            if (s > 10) {
                shortStr = "SS0" + s;
            } else {
                shortStr = "SS" + s;
            }
            semesters.add(new Semester(i, longStr, shortStr,
                    new Timestamp(start.getTime()), new Timestamp(end.getTime())));

        }
        return semesters;
    }
}
