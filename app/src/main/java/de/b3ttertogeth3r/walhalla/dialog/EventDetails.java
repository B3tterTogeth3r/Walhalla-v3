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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Dialog;
import de.b3ttertogeth3r.walhalla.abstract_classes.Touch;
import de.b3ttertogeth3r.walhalla.design.DEvent;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://stackoverflow.com/questions/35801217/adding-a-googlemap-to-a-fragment-programmatically">Adding
 * A GoogleMap to a Fragment Programmatically</a>
 * @since 2.0
 */
public class EventDetails extends Dialog<Void> implements OnMapReadyCallback {
    private static final String TAG = "EventDetails";
    private final Event event;
    private final IAuth auth;
    private final IFirestoreDownload download;
    private RelativeLayout view;
    private TableLayout choresTable;

    public EventDetails(DialogSize size, Event event) {
        super(size);
        this.event = event;
        this.download = Firebase.firestoreDownload();
        this.auth = Firebase.authentication();
    }

    @NonNull
    public static EventDetails display(FragmentManager fragmentManager, DialogSize size, Event event) throws CreateDialogException {
        try {
            EventDetails dialog = new EventDetails(size, event);
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            Log.e(TAG, "unable to create dialog", e);
            throw new CreateDialogException("unable to create event details dialog.", e);
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        this.view = (RelativeLayout) inflater.inflate(R.layout.event_detail, null);
        LinearLayout title = view.findViewById(R.id.event_title);
        title.addView(new Title(requireContext(), event.getTitle()));

        // time row
        getViews(event.getTime());
        getViews(event.getTime(), event.getPunctuality());
        getViews(event.getCollar());

        LinearLayout description = view.findViewById(R.id.event_description);
        description.addView(new Text(requireContext(), event.getDescription()));
        loadDescription(description);

        // chores
        choresTable = view.findViewById(R.id.event_chores_table);
        if (auth.isSignIn()) {
            choresTable.setVisibility(View.VISIBLE);
            loadChores();
        } else {
            choresTable.setVisibility(View.GONE);
        }

        // map
        // FIXME: 30.06.22 android.view.InflateException: Binary XML file line #180 in de.b3ttertogeth3r.walhalla:layout/event_detail: Binary XML file line #180 in de.b3ttertogeth3r.walhalla:layout/event_detail: Error inflating class fragment
        SupportMapFragment mapFragment = (SupportMapFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.maps_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "createDialog: creating maps fragment and api didn't work.");
        }

        container.addView(view);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {

    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(event.getTitle());
    }

    private void getViews(@NonNull Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
        String timeStr = sdf.format(time.toDate());
        ((TextView) view.findViewById(R.id.event_date_text))
                .setText(timeStr);
    }

    private void getViews(@NonNull Timestamp time, @NonNull Punctuality punctuality) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Values.LOCALE);
        String timeStr = sdf.format(time.toDate());
        timeStr = timeStr + punctuality;
        ((TextView) view.findViewById(R.id.event_time_text))
                .setText(timeStr);
        view.findViewById(R.id.event_time)
                .setOnClickListener(view ->
                        Toast.makeToast(requireContext(), punctuality.getDescription()).show()
                );
    }

    private void getViews(@NonNull Collar collar) {
        ((TextView) view.findViewById(R.id.event_collar_text))
                .setText(collar.toString());
        view.findViewById(R.id.event_collar)
                .setOnClickListener(view ->
                        Toast.makeToast(requireContext(), collar.getDescription()).show()
                );

    }

    private void loadDescription(LinearLayout description) {
        download.eventDescription(event.getId())
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        return;
                    }
                    // TODO: 10.06.22 remove padding from Text
                    // TODO: 10.06.22 if more than 3 in result: add border and make it scrollable
                    description.removeAllViewsInLayout();
                    for (de.b3ttertogeth3r.walhalla.object.Text t : result) {
                        if (!t.getValue().isEmpty()) {
                            for (String s : t.getValue()) {
                                Text text = new Text(requireContext(), s);
                                text.setPadding(0, 0, 0, 0);
                                description.addView(text);
                            }
                        }
                    }
                });
    }

    private void loadChores() {
        download.eventChores(event.getId())
                .setOnSuccessListener(result -> {
                    choresTable.removeAllViewsInLayout();
                    if (result != null && !result.isEmpty()) {
                        for (Chore c : result) {
                            choresTable.addView(
                                    DEvent.create(requireActivity(), null, c)
                                            .addTouchListener(new Touch() {

                                            }).show());
                        }
                    }
                    choresTable.invalidate();
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            download.eventLocation(event.getId())
                    .setOnSuccessListener(location -> {
                        if (location == null) {
                            // TODO: 09.06.22 set result to the home a default location
                            return;
                        }
                        LatLng latLng = new LatLng(location.getCoordinates().getLatitude(),
                                location.getCoordinates().getLongitude());
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(location.getName()));
                    });
        } catch (Exception e) {
            Log.e(TAG, "onMapReady: creating map did not work", e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SupportMapFragment f = (SupportMapFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.maps_fragment);
        if (f != null) {
            try {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
                Log.i(TAG, "onDestroyView: maps_fragment destroyed. :)");
            } catch (Exception exception) {
                Log.e(TAG, "onDestroyView: unable to destroy map_fragment", exception);
            }
        }
    }
}
