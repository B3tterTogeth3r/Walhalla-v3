package de.b3ttertogeth3r.walhalla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.annotations.NotNull;

import java.util.EmptyStackException;

import de.b3ttertogeth3r.walhalla.enums.Walhalla;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.OpenExternal;
import de.b3ttertogeth3r.walhalla.models.ProfileError;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OpenExternal {
    private static final String TAG = "MainActivity";
    @SuppressLint("StaticFieldLeak")
    public static View parentLayout;
    public static OpenExternal externalListener;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        externalListener = this;
        de.b3ttertogeth3r.walhalla.App.setContext(MainActivity.this);
        de.b3ttertogeth3r.walhalla.App.setFragmentManager(getFragmentManager());


        // region the default ui
        // Set content
        setContentView(R.layout.activity_main);

        //For easier access to this view for Toast and SnackBar messages
        parentLayout = findViewById(android.R.id.content);

        //Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerlayout = findViewById(R.id.drawer_layout);

        //The left site navigation controller
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fillSideNav();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        // endregion

        // region Open start fragment
        if (savedInstanceState == null) {
            ProfileError error = CacheData.getProfileError();
            if (error != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Profile incomplete")
                        .setMessage(error.getMessage())
                        .setPositiveButton(R.string.edit,
                                (dialog, which) -> switchPage(error.getPage()))
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_error_outline);
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            } else {
                switchPage(CacheData.getStartPage());
            }
            /* backup to copy
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new de.walhalla.app2.fragments.home.Fragment()).commit();
             */
        }
        // endregion
        Firebase.Crashlytics.sendUnsent();
    }

    /**
     * Filling the left side nav with data
     */
    private void fillSideNav () {
        View view = navigationView.getHeaderView(0);
        ImageView image = view.findViewById(R.id.nav_headder);
        TextView title = view.findViewById(R.id.nav_title);
        TextView street = view.findViewById(R.id.nav_street);
        TextView city = view.findViewById(R.id.nav_city);
        image.setImageResource(R.drawable.wappen_2017);
        title.setText(Walhalla.NAME.toString());
        street.setText(Walhalla.ADH.toString());
        city.setVisibility(View.GONE);

        //Navigation Body
        navigationView.getMenu().clear();
        Menu menu = navigationView.getMenu();
        //Public area
        menu.add(0, R.string.menu_home, 0, R.string.menu_home)
                //.setChecked(true)
                .setIcon(R.drawable.ic_home);
        menu.add(0, R.string.menu_about_us, 0, R.string.menu_about_us).setIcon(R.drawable.ic_info);
        menu.add(0, R.string.menu_rooms, 0, R.string.menu_rooms).setIcon(R.drawable.ic_rooms);
        menu.add(0, R.string.menu_program, 0, R.string.menu_program).setIcon(R.drawable.ic_calendar);
        menu.add(0, R.string.menu_messages, 0, R.string.menu_messages).setIcon(R.drawable.ic_message);
        menu.add(0, R.string.menu_chargen, 0, R.string.menu_chargen).setIcon(R.drawable.ic_group);
        menu.add(0, R.string.menu_chargen_phil, 0, R.string.menu_chargen_phil).setIcon(R.drawable.ic_group_line);

        /* Login/Sign up, Logout */
        Menu loginMenu = menu.addSubMenu(R.string.menu_user_editing);
        if (Firebase.Authentication.isSignIn()) {
            loginMenu.add(1, R.string.menu_logout, 0, R.string.menu_logout).setIcon(R.drawable.ic_exit).setCheckable(false);
            loginMenu.add(0, R.string.menu_profile, 0, R.string.menu_profile).setIcon(R.drawable.ic_person);

            loginMenu.add(0, R.string.menu_drinks, 0, R.string.menu_drinks) //Change appearance
                    // depending on who is logged in
                    .setIcon(R.drawable.ic_beer);
            loginMenu.add(0, R.string.menu_balance, 0, R.string.menu_balance);

            //Only visible to members of the fraternity
            Menu menuLogin = menu.addSubMenu(R.string.menu_intern);
            menuLogin.add(0, R.string.menu_transcript, 0, R.string.menu_transcript).setIcon(R.drawable.ic_scriptor);
            menuLogin.add(0, R.string.menu_kartei, 0, R.string.menu_kartei).setIcon(R.drawable.ic_contacts);

            //Only visible to a active board member of the current semester
            //if (User.hasCharge()) {
            Menu menuCharge = menu.addSubMenu(R.string.menu_board_only);
            menuCharge.add(0, R.string.menu_new_person, 0, R.string.menu_new_person).setIcon(R.drawable.ic_person_add);
                /*menuCharge.add(0, R.string.menu_user, 0, R.string.menu_user)
                        .setIcon(R.drawable.ic_user_add);
                /*menuCharge.add(0, R.string.menu_account, 0, R.string.menu_account)
                        .setIcon(R.drawable.ic_account);
            menuCharge.add(0, R.string.menu_new_semester, 0, R.string.menu_new_semester);
            //}
            */

        } else {
            loginMenu.add(1, R.string.menu_login, 0, R.string.menu_login)
                    ///.setCheckable(false)
                    .setIcon(R.drawable.ic_exit);
        }

        loginMenu.setGroupCheckable(1, false, true);

        Menu moreMenu = menu.addSubMenu(R.string.menu_more);
        moreMenu.add(1, R.string.menu_more_history, 1, R.string.menu_more_history);
        moreMenu.add(1, R.string.menu_more_frat_wue, 1, R.string.menu_more_frat_wue);
        moreMenu.add(1, R.string.menu_more_frat_organisation, 1,
                R.string.menu_more_frat_organisation);

        Menu menuEnd = menu.addSubMenu(R.string.menu_other);
        menuEnd.add(0, R.string.menu_settings, 0, R.string.menu_settings).setIcon(R.drawable.ic_settings).setCheckable(false);
        menuEnd.add(0, R.string.menu_donate, 0, R.string.menu_donate).setCheckable(false).setIcon(R.drawable.ic_donate);

        navigationView.invalidate();
    }

    @SuppressLint("NonConstantResourceId")
    private void switchPage (@NonNull Integer item) {
        System.out.println(item);
        switch (item) {
            case R.string.menu_home:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_home));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.home.Fragment()).commit();
                break;
            case R.id.program:
            case R.string.menu_program:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_program));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.program.Fragment()).commit();
                break;
            case R.id.row2first:
                if (Firebase.Authentication.isSignIn()) {
                    Firebase.Analytics.screenChange(item, getString(R.string.menu_balance));
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new de.b3ttertogeth3r.walhalla.fragments.beer.Fragment()).commit();
                } else {
                    Firebase.Analytics.screenChange(item, getString(R.string.menu_rooms));
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new de.b3ttertogeth3r.walhalla.fragments.room.Fragment()).commit();
                }
                break;
            case R.string.menu_rooms:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_rooms));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.room.Fragment()).commit();
                break;
            case R.string.menu_login:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_login));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.signin.Fragment()).commit();
                break;
            case R.string.menu_greeting:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_greeting));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.greeting.Fragment()).commit();
            case R.string.menu_profile:
                Firebase.Analytics.screenChange(item, getString(R.string.menu_profile));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments.profile.Fragment()).commit();
            case R.string.menu_logout:
                break;
            default:
                Firebase.Crashlytics.log(TAG, "page with id " + item + "doesn't exist");
        }
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull @NotNull MenuItem item) {
        switchPage(item.getItemId());
        drawerlayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * If the user presses the back button, the side menu is going to be opened. If the menu is
     * already open, the user can press the button again to close the app.
     */
    @Override
    public void onBackPressed () {
        try {
            App.lastSiteId.pop();
        } catch (EmptyStackException ese) {
            Log.e(TAG, "onBackPressed: EmptyStackException", ese);
        }
        if (App.lastSiteId.isEmpty()) {
            //If drawer is open, show possibility to close the app via the back-button.
            if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
                if (doubleBackToExitPressedOnce) {
                    //Button pressed a second time within half a second.
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.exit_app_via_back, Toast.LENGTH_LONG).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 500);
            } else { //Otherwise open the left menu
                drawerlayout.openDrawer(GravityCompat.START);
            }
        } else {
            switchPage(App.lastSiteId.peek());
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            Log.d(TAG,
                    "onActivityResult: resultCode:" + resultCode + "- requestCode: " + requestCode);
        } else {
            Log.d(TAG, "onActivityResult: no data in intent");
        }
    }

    @Override
    public void browser (@NonNull String link) {
        if (link.length() != 0) {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

    @Override
    public void email (@NonNull String email, String subject) {
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:"));
        String[] recipients = new String[]{email};
        mailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        }
    }

    @Override
    public void intentOpener (Intent intent, int resultCode) {
        this.startActivityForResult(intent, resultCode);
    }
}