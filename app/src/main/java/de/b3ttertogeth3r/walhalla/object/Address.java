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

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.interfaces.object.IAddress;

/**
 * A class to save addresses. Every address needs at least the following values: {@link #STREET},
 * {@link #NUMBER}, {@link #ZIP}, {@link #CITY}. Additional values are: {@link #STATE}, {@link #COUNTRY}.
 * The {@link #ID} is the id in the Firestore database.
 *
 * @author B3tterTogeth3r
 * @see de.b3ttertogeth3r.walhalla.interfaces.object.IAddress
 * @see de.b3ttertogeth3r.walhalla.interfaces.object.Validate Validate
 */
public class Address implements IAddress {
    private static final String TAG = "Address";
    /**
     * ID of the {@link de.b3ttertogeth3r.walhalla.firebase.Firestore Firestore} path
     */
    private String id;
    /**
     * Name of the street.
     */
    private String street;
    /**
     * number of the house in the street.
     */
    private String number;
    /**
     * zip code of the city.
     */
    private String zip;
    /**
     * Name of the city.
     */
    private String city;
    /**
     * Name of the state in a country.
     */
    private String state;
    /**
     * Name of the country.
     */
    private String country;

    public Address() {
    }

    public Address(String street, String number, String zip, String city) {
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
    }

    public Address(String street, String number, String zip, String city, String state,
                   String country) {
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Address(String id, String street, String number, String zip, String city, String state, String country) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @NonNull
    @Override
    public String toString() {
        if (validate()) {
            String result = "";
            result = result + street + " " + number;
            result = result + "\n" + zip + " " + city;
            if (state != null && !state.isEmpty()) {
                result = result + "\n" + state;
            }
            if (country != null && !country.isEmpty()) {
                result = result + "\n" + country;
            }
            return result;
        } else
            return "";
    }

    /**
     * Check for a valid address.
     *
     * @return true, if the necessary fields are set.
     * @see Address
     * @since 2.0
     */
    @Override
    public boolean validate() {
        return (street != null && !street.isEmpty() &&
                number != null && !number.isEmpty() &&
                city != null && !city.isEmpty() &&
                zip != null && !zip.isEmpty());
    }

    @Override
    public String getValue(int valueId) {
        switch (valueId) {
            case ID:
                return getId();
            case STREET:
                return getStreet();
            case NUMBER:
                return getNumber();
            case ZIP:
                return getZip();
            case CITY:
                return getCity();
            case STATE:
                return getState();
            case COUNTRY:
                return getCountry();
            default:
                return null;
        }
    }

    @Override
    public void setValue(int valueId, String value) {
        switch (valueId) {
            case ID:
                setId(value);
                break;
            case STREET:
                setStreet(value);
                break;
            case NUMBER:
                setNumber(value);
                break;
            case ZIP:
                setZip(value);
                break;
            case CITY:
                setCity(value);
                break;
            case STATE:
                setState(value);
                break;
            case COUNTRY:
                setCountry(value);
                break;
            default:
                break;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
