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

import de.b3ttertogeth3r.walhalla.interfaces.Validate;

public class Address implements Validate {
    private String street, number, zip, city, state, country;
    private String id;

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

    @Override
    public boolean validate() {
        return (street != null && !street.isEmpty() &&
                number != null && !number.isEmpty() &&
                city != null && !city.isEmpty() &&
                zip != null && !zip.isEmpty());
    }
}
