package de.b3ttertogeth3r.walhalla;

import static de.b3ttertogeth3r.walhalla.utils.Variables.SIGN_IN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.enums.Walhalla;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Realtime;
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.fragments_main.AboutUsFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.BalanceFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.ChargenFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.ChargenPhilFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.FratGermanyFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.FratWueFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.GreetingFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.HistoryFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.HomeFragment;
import de.b3ttertogeth3r.walhalla.fragments_main.RoomFragment;
import de.b3ttertogeth3r.walhalla.interfaces.OpenExternal;
import de.b3ttertogeth3r.walhalla.models.ProfileError;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.MyLog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OpenExternal, FirebaseAuth.AuthStateListener {
    public static final String TAG = "MainActivity";
    /** For easier access to this view for Toast and SnackBar messages */
    @SuppressLint("StaticFieldLeak")
    public static View parentLayout;
    public static OpenExternal externalListener;
    public static InAppMessage inAppMessage;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce = false;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        externalListener = this;
        inAppMessage = this::openInAppMessageDialog;
        parentLayout = findViewById(android.R.id.content);
        de.b3ttertogeth3r.walhalla.App.setContext(MainActivity.this);
        de.b3ttertogeth3r.walhalla.App.setFragmentManager(getFragmentManager());
        RemoteConfig.apply();

        // region the default ui
        // Set content
        setContentView(R.layout.activity_main);


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
                switchPage(CacheData.getIntentStartPage());
            }
            /* backup to copy
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new de.walhalla.app2.fragments.home.Fragment()).commit();
             */
        }
        // endregion
        Crashlytics.sendUnsent();
    }

    @Override
    protected void onStop () {
        //save, that the app has been started before
        CacheData.firstStart();
        super.onStop();
    }

    private void openInAppMessageDialog (String title, String message, @NonNull String page) {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.later, null)
                .setCancelable(true)
                .setIcon(R.drawable.ic_error_outline);

        switch (page) {
            case "balance":
                builder
                        .setPositiveButton(R.string.yes,
                                (dialog, which) -> switchPage(R.string.menu_balance));
                break;
            case "program":
                builder
                        .setPositiveButton(R.string.yes,
                                (dialog, which) -> switchPage(R.string.menu_program));
                break;
            case "news":
                builder
                        .setPositiveButton(R.string.yes,
                                (dialog, which) -> switchPage(R.string.menu_messages));
                break;
        }

        runOnUiThread(() -> {
            if (CacheData.getFirstStart()) {
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });
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
        if (Authentication.isSignIn()) {
            FirebaseUser user = Authentication.getUser();
            try {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .fitCenter()
                        .placeholder(R.drawable.wappen_2017_v2)
                        .into(image);
            } catch (Exception ignored) {
            }
            title.setText(user.getDisplayName());
            street.setText(user.getEmail());
            city.setText(user.getPhoneNumber());
        } else {
            image.setImageResource(R.drawable.wappen_2017);
            title.setText(Walhalla.NAME.toString());
            street.setText(Walhalla.ADH.toString());
            city.setVisibility(View.GONE);
        }

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
        if (Authentication.isSignIn()) {
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
            if (!CacheData.getCharge().getName().isEmpty()) {
                Menu menuCharge = menu.addSubMenu(R.string.menu_board_only);
                //menuCharge.add(0, R.string.menu_new_person, 0, R.string.menu_new_person)
                // .setIcon(R.drawable.ic_person_add);
                //menuCharge.add(0, R.string.menu_user, 0, R.string.menu_user)
                //        .setIcon(R.drawable.ic_user_add);*/
                menuCharge.add(0, R.string.menu_all_profiles, 0, R.string.menu_all_profiles)
                        .setIcon(R.drawable.ic_supervised_user);
                //menuCharge.add(0, R.string.menu_new_semester, 0, R.string.menu_new_semester);
            }

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
        //System.out.println(item);
        switch (item) {
            case R.string.menu_home:
                Analytics.screenChange(item, getString(R.string.menu_home));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.program:
            case R.string.menu_program:
                Analytics.screenChange(item, getString(R.string.menu_program));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments_main.program.Fragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.row2first:
                if (Authentication.isSignIn()) {
                    switchPage(R.string.menu_balance);
                } else {
                    switchPage(R.string.menu_rooms);
                }
                break;
            case R.id.login:
                if (Authentication.isSignIn()) {
                    switchPage(R.string.menu_logout);
                } else {
                    switchPage(R.string.menu_login);
                }
                break;
            case R.string.menu_rooms:
                Analytics.screenChange(item, getString(R.string.menu_rooms));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new RoomFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_login:
                Analytics.screenChange(item, getString(R.string.menu_login));
                Intent startSignIn = new Intent(this, SignInActivity.class);
                // startSignIn.addFlags(Intent.FLAG_ACT)
                startActivityForResult(startSignIn, SIGN_IN);
                break;
            case R.id.greeting:
            case R.string.menu_greeting:
                Analytics.screenChange(item, getString(R.string.menu_greeting));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new GreetingFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_profile:
                Analytics.screenChange(item, getString(R.string.menu_profile));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments_main.profile.Fragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_balance:
                Analytics.screenChange(item, getString(R.string.menu_balance));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new BalanceFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.chargen:
            case R.string.menu_chargen:
                Analytics.screenChange(item, getString(R.string.menu_chargen));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChargenFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_chargen_phil:
                Analytics.screenChange(item, getString(R.string.menu_chargen_phil));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new ChargenPhilFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_drinks:
                Analytics.screenChange(item, getString(R.string.menu_drinks));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments_main.drinks.Fragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_about_us:
                Analytics.screenChange(item, getString(R.string.menu_about_us));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutUsFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_history:
                Analytics.screenChange(item, getString(R.string.menu_more_history));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_frat_wue:
                Analytics.screenChange(item, getString(R.string.menu_more_frat_wue));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new FratWueFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_frat_organisation:
                Analytics.screenChange(item, getString(R.string.menu_more_frat_organisation));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new FratGermanyFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_all_profiles:
                Analytics.screenChange(item, getString(R.string.menu_all_profiles));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments_main.all_users.Fragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_logout:
                Authentication.signOut();
                break;
            case R.id.news:
            case R.string.menu_messages:
                MainActivity.externalListener.switchFragment(item);
                Analytics.screenChange(item, getString(R.string.menu_messages));
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new de.b3ttertogeth3r.walhalla.fragments_main.news.Fragment())
                        .addToBackStack(MainActivity.TAG)
                        .commit();
                break;
            default:
                Snackbar.make(parentLayout, "page not found" + item,
                        Snackbar.LENGTH_LONG).show();
                Crashlytics.error(TAG, "page with id " + item + "doesn't exist");
                break;
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
        if (fragmentManager.getBackStackEntryCount() == 1) {
            //If drawer is open, show possibility to close the app via the back-button.
            if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
                if (doubleBackToExitPressedOnce) {
                    //Button pressed a second time within a second.
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.exit_app_via_back, Toast.LENGTH_LONG).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
            } else { //Otherwise open the left menu
                drawerlayout.openDrawer(GravityCompat.START);
            }
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            MyLog.d(TAG, "onActivityResult: Sign in done");
            MyToast toast = new MyToast(this);
            if (resultCode == Activity.RESULT_OK) {
                toast.setMessage(R.string.fui_welcome_back_email_header);
                recreate();
            } else if (resultCode == Activity.RESULT_CANCELED) {
               toast.setMessage(R.string.sign_in_canceled);
            } else {
                toast.setMessage(R.string.sign_in_failed);
            }
            toast.show();
        } else {
            MyLog.d(TAG, "onActivityResult: no data in intent");
        }
    }

    @Override
    public void browser (@NonNull String link) {
        if (link.length() != 0) {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
        } else {
            browser();
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

    @Override
    public void switchFragment (int resId) {
        switchPage(resId);
    }

    @Override
    public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
        recreate();
    }

    public interface InAppMessage {
        void displayMessage (String title, String message, String page);
    }
}