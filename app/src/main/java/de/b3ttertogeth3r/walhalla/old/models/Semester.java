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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Semester {
    private int id;
    private Timestamp begin, end;
    private String name_long, name_short;

    public Semester () {
    }

    public Semester (int id, Timestamp begin, Timestamp end, String name_long, String name_short) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.name_long = name_long;
        this.name_short = name_short;
    }

    public Semester(@NonNull Set<String> set){
        List<String> stringSet = new ArrayList<>(set);
        if(stringSet.size() == 5 || stringSet.size() == 4) {
            List<Long> numbers = new ArrayList<>();
            while(!stringSet.isEmpty()){
                try{
                    numbers.add(Long.parseLong(stringSet.get(0)));
                } catch (Exception e){
                    if(stringSet.get(0).contains("emester")){
                        name_long = stringSet.get(0);
                    } else {
                        this.name_short = stringSet.get(0);
                    }
                }
                stringSet.remove(0);
            }
            Collections.sort(numbers);
            if(numbers.size() == 3){
                this.id = Integer.parseInt(numbers.get(0) + "");
                this.begin = new Timestamp(new Date(numbers.get(1)));
                this.end = new Timestamp(new Date(numbers.get(2)));
            }
        }
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public Timestamp getBegin () {
        return begin;
    }

    public void setBegin (Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd () {
        return end;
    }

    public void setEnd (Timestamp end) {
        this.end = end;
    }

    /**
     * long value of the semester. not i.e. <code>SS21</code>
     * @return the long name
     */
    public String getName_long () {
        return name_long;
    }

    public void setName_long (String name_long) {
        this.name_long = name_long;
    }

    /**
     * the short value of the semester, like i.e. <code>SS21</code>
     * @return the short name
     */
    public String getName_short () {
        return name_short;
    }

    public void setName_short (String name_short) {
        this.name_short = name_short;
    }

    @Override
    public boolean equals (@Nullable Object obj) {
        try {
            Semester sem = (Semester) obj;
            return sem != null && begin.equals(sem.begin) && end.equals(sem.end) && name_long.equals(sem.name_long) && name_short.equals(sem.name_short);
        } catch (Exception e) {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString () {
        return super.toString();
    }

    @Exclude
    public Set<String> getSet (){
        Set<String> stringSet = new HashSet<>();
        stringSet.add(String.valueOf(this.id));
        stringSet.add(String.valueOf(this.begin.getSeconds()));
        stringSet.add(String.valueOf(this.end.getSeconds()));
        stringSet.add(this.name_long);
        stringSet.add(this.name_short);
        return stringSet;
    }
}
