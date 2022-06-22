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

package de.b3ttertogeth3r.walhalla.old.fragments_main.program;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.models.Event;
import de.b3ttertogeth3r.walhalla.old.utils.Variables;

public class Details extends DialogFragment {
    private static final String TAG = "Event-Details";
    public static Dialog DIALOG;
    private final Event event;
    private static Details detailsDialog;
    private Toolbar toolbar;
    private static Date start_date;
    TextView title, description,  time;

    public Details(Event event) {
        this.event = event;
    }

    public static void display(FragmentManager fragmentManager, Event event){
        Details.detailsDialog = new Details(event);
        detailsDialog.show(fragmentManager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.event_detail, container, false);
        toolbar = view.findViewById(R.id.program_details_close);
        return view;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //toolbar menu item left (close), right calendar
        //Fill TVs with content of the event
        //Format year numeric, month text, day numeric and hour:minute both with 2 digits
        //(dd.MM.YYYY at hh:mm o'clock [ct/st]
        //Add Maps API with the marker at the events position, if event has no position,
        //set marker to: LatLng(49.784389, 9.924648);
    }

    @Override
    public void onStart() {
        super.onStart();
        DIALOG = getDialog();
        if (DIALOG != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(DIALOG.getWindow()).setLayout(width, height);
            DIALOG.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
        start_date = Calendar.getInstance(Variables.LOCALE).getTime();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
}
