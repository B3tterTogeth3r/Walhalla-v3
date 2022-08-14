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

import com.google.firebase.firestore.GeoPoint;

import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

public class Location implements Validate {
    String name;
    GeoPoint coordinates;
    private String id;

    public Location () {
        this.name = "K.St.V. Walhalla";
        this.coordinates = new GeoPoint(49.784420, 9.924580);
    }

    public Location (String name, GeoPoint coordinates) {
        this.name = name;
        this.coordinates = coordinates;
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

    public GeoPoint getCoordinates () {
        return coordinates;
    }

    public void setCoordinates (GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    @NonNull
    @Override
    public String toString () {
        return name + " at " + coordinates.toString();
    }

    @Override
    public boolean validate () {
        return false;
    }
}
