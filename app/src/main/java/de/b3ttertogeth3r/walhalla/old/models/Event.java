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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.old.interfaces.ChangeListener;

/**
 * This class contains every value an event of the fraternity could have.
 * Containing the start and end time, the punctuality, the title,
 * which kind of collar is needed, a description of the event, whether it is a
 * meeting or not and its location name and coordinates.
 */
public class Event {
    private Timestamp end, begin;
    private String id, punctuality, title, description, collar, location_name;
    private GeoPoint location_coordinates;
    private boolean hasDetails, isMeeting;
    /** list of ranks, the event is visible to
     * TODO Maybe extendable to analytics groups */
    private ArrayList<String> visibleTo;
    private ChangeListener<Event> listener = null;

    public Event () {
    }

    public Event (Timestamp end, Timestamp begin, String id, String punctuality, String title,
                  String description, String collar, String location_name,
                  GeoPoint location_coordinates, boolean hasDetails,
                  boolean isMeeting, ArrayList<String> visibleTo) {
        this.end = end;
        this.begin = begin;
        this.id = id;
        this.punctuality = punctuality;
        this.title = title;
        this.description = description;
        this.collar = collar;
        this.location_name = location_name;
        this.location_coordinates = location_coordinates;
        this.hasDetails = hasDetails;
        this.isMeeting = isMeeting;
        this.visibleTo = visibleTo;
    }

    public Timestamp getEnd () {
        return end;
    }

    public void setEnd (Timestamp end) {
        this.end = end;
        if(listener != null) {
            listener.change(this);
        }
    }

    public Timestamp getBegin () {
        return begin;
    }

    public void setBegin (Timestamp begin) {
        this.begin = begin;
        if(listener != null) {
            listener.change(this);
        }
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getPunctuality () {
        return punctuality;
    }

    public void setPunctuality (String punctuality) {
        this.punctuality = punctuality;
        if(listener != null) {
            listener.change(this);
        }
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
        if(listener != null) {
            listener.change(this);
        }
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
        if(listener != null) {
            listener.change(this);
        }
    }

    public String getCollar () {
        return collar;
    }

    public void setCollar (String collar) {
        this.collar = collar;
        if(listener != null) {
            listener.change(this);
        }
    }

    public String getLocation_name () {
        return location_name;
    }

    public void setLocation_name (String location_name) {
        this.location_name = location_name;
        if(listener != null) {
            listener.change(this);
        }
    }

    public GeoPoint getLocation_coordinates () {
        return location_coordinates;
    }

    public void setLocation_coordinates (GeoPoint location_coordinates) {
        this.location_coordinates = location_coordinates;
        if(listener != null) {
            listener.change(this);
        }
    }

    public boolean isHasDetails () {
        return hasDetails;
    }

    public void setHasDetails (boolean hasDetails) {
        this.hasDetails = hasDetails;
        if(listener != null) {
            listener.change(this);
        }
    }

    public boolean isMeeting () {
        return isMeeting;
    }

    public void setMeeting (boolean meeting) {
        isMeeting = meeting;
        if(listener != null) {
            listener.change(this);
        }
    }

    public ArrayList<String> getVisibleTo () {
        return visibleTo;
    }

    public void setVisibleTo (ArrayList<String> visibleTo) {
        this.visibleTo = visibleTo;
        if(listener != null) {
            listener.change(this);
        }
    }

    @Exclude
    public boolean exists () {
        return !(title == null || title.isEmpty());
    }

    /**
     * @return the day of the event start
     */
    @Exclude
    public int getDayOfYearStart () {
        Calendar start = Calendar.getInstance();
        start.setTime(begin.toDate());
        return start.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * @return the month the event ends
     */
    @Exclude
    public int getMonth () {
        Calendar start = Calendar.getInstance();
        start.setTime(getEnd().toDate());
        return start.get(Calendar.MONTH);
    }

    @Exclude
    public void setChangeListener(ChangeListener<Event> listener) {
        this.listener = listener;
    }

    @Exclude
    public boolean isValid () {
        return exists() && getBegin() != null && getEnd() != null;
    }

    @Exclude
    public String getBeginDateString () {
        String result = "";
        if(begin != null){
            Calendar c = Calendar.getInstance();
            c.setTime(begin.toDate());
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            String monthStr;
            if(month <10) {
                monthStr = "0" + month;
            } else {
                monthStr = String.valueOf(month);
            }
            result = day + "." + monthStr + "." + year;
        }
        return result;
    }

    @Exclude
    public String getBeginTimeString(){
        String result = "";
        if (begin != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(begin.toDate());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            String minuteStr;
            if(minute <10){
                minuteStr = "0" + minute;
            } else {
                minuteStr = String.valueOf(minute);
            }
            result = hour + ":" + minuteStr;
        }
        return result;
    }
}
