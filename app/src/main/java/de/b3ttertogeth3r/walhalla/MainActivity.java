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

package de.b3ttertogeth3r.walhalla;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.Fraternity;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.HideKeyBoard;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.IOnBackPressed;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.ISideNav;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.OpenExternal;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.loader.LoadingCircle;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.ProgressBarAnimation;

public class MainActivity extends AppCompatActivity implements LoadingCircle, ISideNav, OpenExternal,
        HideKeyBoard, FirebaseAuth.AuthStateListener {
    private static final String TAG = "MainActivity";
    public static LoadingCircle loadingInterface;
    public static OpenExternal openExternal;
    public static HideKeyBoard hideKeyBoard;
    private ProgressBar loadingCircle;
    private ProgressBarAnimation loadingAnimation;
    private FragmentManager fragmentManager;
    private boolean doubleBackToExit = false;
    private DrawerLayout drawerLayout;
    private FirebaseAuth.AuthStateListener authStateListener;

    public void hide() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingInterface = this;
        hideKeyBoard = this;
        authStateListener = this;
        Firebase.authentication().addAuthStateListener(authStateListener);
        loadingCircle = findViewById(R.id.progressBarHolder);
        loadingAnimation = new ProgressBarAnimation(loadingCircle, 0, 20);
        fragmentManager = getSupportFragmentManager();
        loadingAnimation.setDuration(100);
        openExternal = this;
        App.setContext(MainActivity.this);

        // Set content
        setContentView(R.layout.activity_main);

        // Init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // left side navigation
        drawerLayout = findViewById(R.id.drawer_layout);

        SideNav navigationView = findViewById(R.id.nav_view);
        navigationView.setListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        if (savedInstanceState == null) {
            SideNav.changePage(R.string.menu_home, fragmentManager.beginTransaction());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Firebase.authentication().removeAuthListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // If the current fragment has a back stack on its own, don't go in here
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            // If backstack is back to the home fragment
            if (fragmentManager.getBackStackEntryCount() == 1) {
                //If drawer is open, show possibility to close the app via the back-button.
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    if (doubleBackToExit) {
                        //Button pressed a second time within a second.
                        this.finishAffinity();
                        return;
                    }
                    this.doubleBackToExit = true;
                    android.widget.Toast.makeText(this, R.string.exit_app_via_back, android.widget.Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(() -> doubleBackToExit = false, 1000);
                } else { //Otherwise open the left menu
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
            // Close drawer and go one back
            else {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    public FragmentTransaction clicked(int itemId) {
        drawerLayout.closeDrawer(GravityCompat.START);
        try {
            stop();
        } catch (Exception ignored) {
            //ignored because on start it is still null
        }
        return fragmentManager.beginTransaction();
    }

    @Override
    public void start() {
        loadingCircle.setVisibility(View.VISIBLE);
        loadingCircle.startAnimation(loadingAnimation);
        // loadingCircle.animate().start(); maybe this is needed, I don't know yet.
    }

    @Override
    public void stop() {
        loadingCircle.clearAnimation();
        loadingCircle.setVisibility(View.GONE);
    }

    @Override
    public void link(String link) {
        if (link == null || link.isEmpty()) {
            link(Fraternity.WEBSITE.toString());
            return;
        } else {
            if (!link.startsWith("http://") || !link.startsWith("https://")) {
                link("http://" + link);
                return;
            }
        }
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(link));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Log.e(TAG, "user has no browser app");
        }
    }

    @Override
    public void sendEmail(String[] recipient, String subject) {
        if (recipient == null || recipient.length == 0) {
            sendEmail(new String[]{Fraternity.EMAIL_INFO.toString()}, subject);
            return;
        }
        if (subject == null || subject.isEmpty()) {
            sendEmail(recipient, "Anfrage aus der App");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.e("TAG", "User has no email app");
        }
    }

    @Override
    public void saveInCalendar(Semester semester, Event event) {
        if (event == null || !event.validate()) {
            Toast.makeToast(getApplicationContext(), "Event nicht gültig").show();
            Log.e(TAG, "Tried to save an incomplete event");
            return;
        }
        // close keyboard if its open
        hideKeyBoard.hide();
        //load the location and the full event description
        Toast errorToast = new Toast(getApplicationContext());
        IFirestoreDownload downloader = Firebase.Firestore.download();
        downloader.getEventDescription(semester.getId(), event.getId())
                .setOnSuccessListener(description -> {
                    downloader.getEventLocation(semester.getId(), event.getId())
                            .setOnSuccessListener(eventLocation -> {
                                Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                                calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                                calendarIntent.putExtra(CalendarContract.Events.TITLE,
                                        event.getTitle());
                                // TODO: 23.05.22 set a reminder a week and another 2 days
                                //  before
                                //  the event start
                                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION,
                                        description);
                                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION,
                                        eventLocation.toString());
                                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,
                                        false);
                                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                        event.getTime());
                                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                        event.getEnd());
                                calendarIntent.putExtra(CalendarContract.Events.EVENT_COLOR_KEY,
                                        ContextCompat.getColor(getApplicationContext(),
                                                R.color.colorPrimary));
                                calendarIntent.putExtra(CalendarContract.Events.EVENT_COLOR,
                                        ContextCompat.getColor(getApplicationContext(),
                                                R.color.colorPrimary));
                                calendarIntent.putExtra(CalendarContract.Events.CALENDAR_COLOR,
                                        ContextCompat.getColor(getApplicationContext(),
                                                R.color.colorPrimary));
                                calendarIntent.putExtra(CalendarContract.Events.CALENDAR_COLOR_KEY,
                                        ContextCompat.getColor(getApplicationContext(),
                                                R.color.colorPrimary));
                                calendarIntent.putExtra(CalendarContract.Events.ORGANIZER,
                                        Fraternity.NAME.toString());

                                if (calendarIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(calendarIntent);
                                } else {
                                    errorToast.setMessage("There is no app that support this " +
                                                    "action.")
                                            .show();
                                    Log.e(TAG, "User has no calendar app");
                                }
                            })
                            .setOnFailListener(e -> {
                                errorToast.setMessage("Location couldn't be downloaded.").show();
                                Log.e(TAG, "Location download didn't work", e);
                            });
                }).setOnFailListener(e -> {
                    errorToast.setMessage("Description couldn't be downloaded.").show();
                    Log.e(TAG, "Description download didn't work", e);
                });
    }

    @Override
    public void file(File file) {
        if (file == null || !file.validate()) {
            Toast.makeToast(getApplicationContext(), "Dateipfad nicht gültig").show();
            Log.e(TAG, "Tried to download a non valid file");
            return;
        }
                /* TODO: 01.06.22 download file and display it
                IStorageDownload download = new StorageMock.Download();
                download.file(file, new Loader<String>(true) {
                    @Override
                    public void onSuccessListener (String result) {
                        // TODO: 24.05.22 open file in device pdf viewer

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(result), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailureListener (Exception e) {
                        Toast toast = new Toast(getApplicationContext());
                        toast.setMessage("Datei konnte nicht herunter geladen werden").show();
                        Log.e(TAG, "File couldn't be downloaded", e);
                    }
                });
                 */
    }

    @Override
    public void image(String link) {
        if (link == null || link.isEmpty()) {
            Toast.makeToast(getApplicationContext(), "No Image link selected.").show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(link), "image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeToast(getApplicationContext(), "Image couldn't be opened. Try again later").show();
            Log.e(TAG, "Image couldn't be opened.");
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        try {
            SideNav.reload.reload();
        } catch (Exception e) {
            Log.e(TAG, "onAuthStateChanged: ", e);
        }
    }
}
