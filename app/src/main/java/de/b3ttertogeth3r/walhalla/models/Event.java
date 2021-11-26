package de.b3ttertogeth3r.walhalla.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;

/**
 * This class contains every value an event of the fraternity could have.
 * Containing the start and end time, the punctuality, the title,
 * which kind of collar is needed, a description of the event, whether it is a
 * meeting or not and its location name and coordinates.
 */
public class Event {
    private Timestamp end, begin;
    private String id, punct, title, description, collar, location_name;
    private GeoPoint location_coordinates;
    private boolean meeting = false;
    private String publicity;

    public Event () {
    }

    public Event (Timestamp end, Timestamp begin, String punct, String title, String description,
                  String collar, String location_name, GeoPoint location_coordinates,
                  String publicity) {
        this.end = end;
        this.begin = begin;
        this.punct = punct;
        this.title = title;
        this.description = description;
        this.collar = collar;
        this.location_name = location_name;
        this.location_coordinates = location_coordinates;
        this.publicity = publicity;
    }

    public Event (Timestamp end, Timestamp begin, String punct, String title, String description,
                  String collar, String location_name, GeoPoint location_coordinates,
                  String publicity,
                  boolean meeting) {
        this.end = end;
        this.begin = begin;
        this.punct = punct;
        this.title = title;
        this.description = description;
        this.collar = collar;
        this.location_name = location_name;
        this.location_coordinates = location_coordinates;
        this.publicity = publicity;
        this.meeting = meeting;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public Timestamp getBegin () {
        return begin;
    }

    public void setBegin (Timestamp begin) {
        this.begin = begin;
    }

    public String getPunct () {
        return punct;
    }

    public void setPunct (String punct) {
        this.punct = punct;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getDescription () {
        if(description == null || description.equals("null")) {
            return "";
        }
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getCollar () {
        return collar;
    }

    public void setCollar (String collar) {
        this.collar = collar;
    }

    public String getLocation_name () {
        return location_name;
    }

    public void setLocation_name (String location_name) {
        this.location_name = location_name;
    }

    public GeoPoint getLocation_coordinates () {
        return location_coordinates;
    }

    public void setLocation_coordinates (GeoPoint location_coordinates) {
        this.location_coordinates = location_coordinates;
    }

    public boolean isMeeting () {
        return meeting;
    }

    public void setMeeting (boolean meeting) {
        this.meeting = meeting;
    }

    public String getPublicity () {
        return publicity;
    }

    public void setPublicity (String publicity) {
        this.publicity = publicity;
    }

    public boolean exists () {
        return !title.isEmpty();
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

    public Timestamp getEnd () {
        return end;
    }

    public void setEnd (Timestamp end) {
        this.end = end;
    }
}
