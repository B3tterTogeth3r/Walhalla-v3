package de.b3ttertogeth3r.walhalla.models;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Charge {

    private String id;
    private String PoB;
    private String first_Name;
    private String last_Name;
    private String mail;
    private String mobile;
    private Map<String, Object> address = new HashMap<>();
    private String picture_path;
    private String uid;
    private String major;

    public Charge () {
    }

    @Exclude
    public String getId(){
        return this.id;
    }

    @Exclude
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return value of {@code first_Name} and {@code last_Name} combined with a space
     * @see #getFirst_Name()
     * @see #getLast_Name()
     * @since 1.1
     */
    @NotNull
    @Exclude
    public String getFullName () {
        return getFirst_Name() + " " + getLast_Name();
    }

    public String getMajor () {
        return major;
    }

    public void setMajor (String major) {
        this.major = major;
    }

    public String getPoB () {
        return PoB;
    }

    public void setPoB (String poB) {
        PoB = poB;
    }

    public String getFirst_Name () {
        return first_Name;
    }

    public void setFirst_Name (String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name () {
        return last_Name;
    }

    public void setLast_Name (String last_Name) {
        this.last_Name = last_Name;
    }

    public String getMail () {
        return mail;
    }

    public void setMail (String mail) {
        this.mail = mail;
    }

    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }

    public Map<String, Object> getAddress () {
        return address;
    }

    public void setAddress (Map<String, Object> address) {
        this.address = address;
    }

    public String getPicture_path () {
        return picture_path;
    }

    public void setPicture_path (String picture_path) {
        this.picture_path = picture_path;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getAddressStr () {
        String result = "";
        try {
            result = address.get(Person.ADDRESS_STREET).toString() + " " +
                    address.get(Person.ADDRESS_NUMBER).toString() + "\n" +
                    address.get(Person.ADDRESS_ZIP_CODE).toString() + " " +
                    address.get(Person.ADDRESS_CITY).toString();
        } catch (Exception ignored) {
        }
        return result;
    }
}
