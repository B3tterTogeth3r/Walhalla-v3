package de.b3ttertogeth3r.walhalla.models;

import static de.b3ttertogeth3r.walhalla.enums.Address.*;

import java.util.Map;

public class Charge {
    String id, name, major, from;
    de.b3ttertogeth3r.walhalla.models.Address address;
    String mobile, number;

    public Charge (String id, String name, String major, String from, de.b3ttertogeth3r.walhalla.models.Address address,
                   String mobile,
                   String number) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.from = from;
        this.address = address;
        this.mobile = mobile;
        this.number = number;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getMajor () {
        return major;
    }

    public void setMajor (String major) {
        this.major = major;
    }

    public String getFrom () {
        return from;
    }

    public void setFrom (String from) {
        this.from = from;
    }

    public de.b3ttertogeth3r.walhalla.models.Address getAddress () {
        return address;
    }

    public void setAddress (Map<String, String> address) {
        if (address == null || address.isEmpty()) {
            throw new NullPointerException("Address cannot be set empty.");
        }
        this.address = new de.b3ttertogeth3r.walhalla.models.Address(address.get(STREET.toString()), address.get(NUMBER.toString()),
                address.get(ZIP.toString()), address.get(CITY.toString()));
    }

    public String getMobile () {
        return mobile;
    }

    public void setMobile (String mobile) {
        this.mobile = mobile;
    }

    public String getNumber () {
        return number;
    }

    public void setNumber (String number) {
        this.number = number;
    }
}
