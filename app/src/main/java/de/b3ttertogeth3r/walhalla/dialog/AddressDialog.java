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

package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.nio.channels.UnsupportedAddressTypeException;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.object.Address;

public class AddressDialog extends Dialog<Address> {
    private static final String TAG = "AddressDialog";
    private final Address address;
    private EditText number, street, zip, city, state, country;

    public AddressDialog(Address address) {
        super(DialogSize.WRAP_CONTENT);
        this.address = address;
    }

    @NonNull
    public static AddressDialog display(@NonNull FragmentManager manager, Address address) throws CreateDialogException {
        try {
            AddressDialog dialog = new AddressDialog(address);
            dialog.show(manager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create " + TAG, e);
        }
    }

    @NonNull
    public static AddressDialog display(@NonNull FragmentManager manager) throws CreateDialogException {
        try {
            AddressDialog dialog = new AddressDialog(new Address());
            dialog.show(manager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create " + TAG, e);
        }
    }

    @Override
    public Address done() {
        address.setStreet(street.getText());
        address.setCity(city.getText());
        address.setNumber(number.getText());
        address.setZip(zip.getText());
        address.setCountry(country.getText());
        address.setState(state.getText());

        if (address.validate()) {
            return address;
        } else {
            throw new UnsupportedAddressTypeException();
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        //region init edit text fields
        number = new EditText(requireContext());
        number.setId(R.id.number);
        street = new EditText(requireContext());
        street.setId(R.id.street);
        zip = new EditText(requireContext());
        zip.setId(R.id.zip);
        city = new EditText(requireContext());
        city.setId(R.id.city);
        state = new EditText(requireContext());
        state.setId(R.id.state);
        country = new EditText(requireContext());
        country.setId(R.id.country);
        //endregion

        //region design
        //region number
        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        numberParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        number.setLayoutParams(numberParams);
        number.setMinEms(5);
        number.setMaxEms(5);
        number.setDescription(R.string.number);
        number.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        container.addView(number);
        //endregion

        //region street
        RelativeLayout.LayoutParams streetParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        streetParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        streetParams.addRule(RelativeLayout.LEFT_OF, number.getId());
        street.setLayoutParams(streetParams);
        street.setDescription(R.string.street);
        container.addView(street);
        //endregion

        //region zip
        RelativeLayout.LayoutParams zipParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        zipParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        zipParams.addRule(RelativeLayout.BELOW, street.getId());
        zip.setLayoutParams(zipParams);
        zip.setMaxEms(5);
        zip.setMinEms(5);
        zip.setDescription(R.string.zip);
        zip.setInputType(InputType.TYPE_CLASS_NUMBER);
        container.addView(zip);
        //endregion

        //region city
        RelativeLayout.LayoutParams cityParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cityParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cityParams.addRule(RelativeLayout.RIGHT_OF, zip.getId());
        cityParams.addRule(RelativeLayout.BELOW, street.getId());
        city.setLayoutParams(cityParams);
        city.setDescription(R.string.city);
        container.addView(city);
        //endregion

        //region state
        RelativeLayout.LayoutParams stateParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        stateParams.addRule(RelativeLayout.BELOW, city.getId());
        stateParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        stateParams.addRule(RelativeLayout.LEFT_OF, country.getId());
        state.setLayoutParams(stateParams);
        state.setDescription(R.string.state);
        state.setMinEms(10);
        state.setMaxEms(10);
        container.addView(state);
        //endregion

        //region country
        RelativeLayout.LayoutParams countryParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        countryParams.addRule(RelativeLayout.BELOW, zip.getId());
        countryParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        country.setLayoutParams(countryParams);
        country.setDescription(R.string.country);
        country.setMaxEms(10);
        country.setMinEms(10);
        container.addView(country);
        //endregion
        //endregion

        //region fill with address content
        number.setContent(address.getNumber());
        street.setContent(address.getStreet());
        zip.setContent(address.getZip());
        city.setContent(address.getCity());
        state.setContent(address.getState());
        country.setContent(address.getCountry());
        //endregion
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, this);
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.edit);
    }
}
