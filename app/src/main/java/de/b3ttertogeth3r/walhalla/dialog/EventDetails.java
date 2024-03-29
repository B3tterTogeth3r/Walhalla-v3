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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.Event;
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
import de.b3ttertogeth3r.walhalla.object.Location;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Log;
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
    private final de.b3ttertogeth3r.walhalla.object.Event event;
    private final IAuth auth;
    private final IFirestoreDownload download;
    private RelativeLayout view;
    private TableLayout choresTable;
    private final Semester semester;

    public EventDetails(DialogSize size, Semester semester, de.b3ttertogeth3r.walhalla.object.Event event) {
        super(size);
        this.event = event;
        this.semester = semester;
        this.download = Firebase.Firestore.download();
        this.auth = Firebase.authentication();
    }

    @NonNull
    public static EventDetails display(FragmentManager fragmentManager, DialogSize size, Semester semester, de.b3ttertogeth3r.walhalla.object.Event event) throws CreateDialogException {
        try {
            EventDetails dialog = new EventDetails(size, semester, event);
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
        // TODO: 21.07.22 map is not displayed. WHY????
        //https://stackoverflow.com/questions/16551458/android-google-maps-not-displaying
        MapView map = view.findViewById(R.id.maps_fragment);
        if (map != null) {
            map.getMapAsync(this);
            map.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "createDialog: creating maps fragment and api didn't work.");
        }

        AdView adView = view.findViewById(R.id.adView);
        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        container.addView(view);
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
        download.getEventDescription(semester.getId(), event.getId())
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
        download.getEventChores(semester.getId(), event.getId())
                .setOnSuccessListener(result -> {
                    choresTable.removeAllViewsInLayout();
                    if (result != null && !result.isEmpty()) {
                        for (Chore c : result) {
                            choresTable.addView(
                                    Event.create(requireActivity(), null, c, true)
                                            .addTouchListener(new Touch() {

                                            }).show());
                        }
                    }
                    choresTable.invalidate();
                }).setOnFailListener(e -> Log.e(TAG, "onFailureListener: ", e));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setBuildingsEnabled(false);
        try {
            MapsInitializer.initialize(requireActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            download.getEventLocation(semester.getId(), event.getId())
                    .setOnSuccessListener(location -> {
                        if (location == null || !location.validate()) {
                            // set result to the home a default location
                            location = new Location("K.St.V. Walhalla", new GeoPoint(49.784420, 9.924580));
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
