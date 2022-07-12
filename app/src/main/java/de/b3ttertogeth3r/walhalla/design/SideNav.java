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

package de.b3ttertogeth3r.walhalla.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Walhalla;
import de.b3ttertogeth3r.walhalla.fragment.Home;
import de.b3ttertogeth3r.walhalla.fragment.board.allUsers;
import de.b3ttertogeth3r.walhalla.fragment.common.AboutUs;
import de.b3ttertogeth3r.walhalla.fragment.common.Donation;
import de.b3ttertogeth3r.walhalla.fragment.common.Fraternities_city;
import de.b3ttertogeth3r.walhalla.fragment.common.Fraternities_germany;
import de.b3ttertogeth3r.walhalla.fragment.common.GreetingFragment;
import de.b3ttertogeth3r.walhalla.fragment.common.NewsFragment;
import de.b3ttertogeth3r.walhalla.fragment.common.PhilistinesBoard;
import de.b3ttertogeth3r.walhalla.fragment.common.Program;
import de.b3ttertogeth3r.walhalla.fragment.common.Rooms;
import de.b3ttertogeth3r.walhalla.fragment.common.StudentBoard;
import de.b3ttertogeth3r.walhalla.fragment.sign_in.SignInHome;
import de.b3ttertogeth3r.walhalla.fragment.signed_in.Balance;
import de.b3ttertogeth3r.walhalla.fragment.signed_in.Drinks;
import de.b3ttertogeth3r.walhalla.fragment.signed_in.Profile;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.ISideNav;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.mock.AuthMock;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.old.fragments_main.HistoryFragment;


/**
 * <p>
 * Filling the left side navigation with data starting with the {@code HeaderView} view.<br>If
 * a user is signed in the users name, email address and phone number. Also the users profile
 * picture should be displayed via {@link com.bumptech.glide.Glide Glide}.<br>If no user is
 * signed in only the name of the fraternity, the address and the default image is displayed.
 * </p>
 * <p>
 * Below the {@code HeaderView} comes the {@code NavigationBody}. The menu list is cleared
 * everytime this function is called. First are the public sites with:
 * <ul>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.Home Home}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.AboutUs AboutUs}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.Rooms Rooms}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.Program Program}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.NewsFragment News}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.StudentBoard StudentBoard}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.PhilistinesBoard
 *     PhilistinesBoard}</li>
 * </ul>
 * If no user is signed in, the next section is just the login menu
 * <ul><li>{@link de.b3ttertogeth3r.walhalla.fragment.sign_in.SignInHome SignInHome}</li></ul>
 * If a user is signed in, the following sites are displayed:
 * <ul>
 *     <li>{@link SignOut}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.signed_in.Profile Profile}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.signed_in.Balance Balance}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.signed_in.Chores Chores}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.signed_in.Drinks Drinks}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.signed_in.Transcript Transcript}</li>
 * </ul>
 * If the signed in user is an active board member the following sites are also available:
 * <ul>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.board.Account Account}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.board.AddChoresToEvent AddChoresToEvent
 *     }</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.board.allUsers allUsers}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.board.EditEvent EditEvent}</li>
 * </ul>
 * After that there are some sites with some more information about the fraternity, a
 * donation page and a settings page.
 * <ul>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.OwnHistory OwnHistory}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.Fraternities_city FraternitiesCity}
 *     </li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.Fraternities_germany
 *     FraternitiesGermany}</li>
 *     <li>{@link de.b3ttertogeth3r.walhalla.fragment.common.Donation Donate}</li>
 *     <li>{@link Settings}</li>
 * </ul>
 * </p>
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public class SideNav extends NavigationView implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private static final String TAG = "SideNav";
    private static IAuth auth;
    private ISideNav listener;

    public SideNav(@NonNull Context context) {
        super(context);
        auth = new AuthMock();
        setNavigationItemSelectedListener(this);
        fillHeadView();
        fillSideNav();
    }

    private void fillHeadView() {
        View view = getHeaderView(0);
        ImageView image = view.findViewById(R.id.nav_headder);
        TextView title = view.findViewById(R.id.nav_title);
        TextView street = view.findViewById(R.id.nav_street);
        TextView city = view.findViewById(R.id.nav_city);
        if (auth.isSignIn()) {
            FirebaseUser user = auth.getUser();
            try {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .fitCenter()
                        .placeholder(R.drawable.wappen_2017_v2)
                        .into(image);
                title.setText(user.getDisplayName());
                street.setText(user.getEmail());
                city.setText(user.getPhoneNumber());
            } catch (Exception e) {
                image.setImageResource(R.drawable.wappen_2017);
                title.setText(Walhalla.NAME.toString());
                street.setText(Walhalla.ADH.toString());
                city.setVisibility(View.GONE);
            }
        } else {
            image.setImageResource(R.drawable.wappen_2017);
            title.setText(Walhalla.NAME.toString());
            street.setText(Walhalla.ADH.toString());
            city.setVisibility(View.GONE);
        }
    }

    private void fillSideNav() {
        //Navigation Body
        getMenu().clear();
        Menu menu = getMenu();
        menu.clear();
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
        if (auth.isSignIn()) {
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
            /* TODO 01.06.2022 create cache data saves
            if (!CacheData.getCharge().getName().isEmpty()) {
                Menu menuCharge = menu.addSubMenu(R.string.menu_board_only);
                //menuCharge.add(0, R.string.menu_new_person, 0, R.string.menu_new_person)
                // .setIcon(R.drawable.ic_person_add);
                //menuCharge.add(0, R.string.menu_user, 0, R.string.menu_user)
                //        .setIcon(R.drawable.ic_user_add); * /
                menuCharge.add(0, R.string.menu_all_profiles, 0, R.string.menu_all_profiles)
                        .setIcon(R.drawable.ic_supervised_user);
                //menuCharge.add(0, R.string.menu_new_semester, 0, R.string.menu_new_semester);
            }*/

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

        invalidate();
    }

    public SideNav(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        auth = new AuthMock();
        setNavigationItemSelectedListener(this);
        fillHeadView();
        fillSideNav();
    }

    public SideNav(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        auth = new AuthMock();
        setNavigationItemSelectedListener(this);
        fillHeadView();
        fillSideNav();
    }

    @SuppressLint("NonConstantResourceId")
    public static void changePage(int string_value_id, FragmentTransaction transaction) {
        int container = R.id.fragment_container;
        switch (string_value_id) {
            case R.string.menu_login:
                transaction.replace(container, new SignInHome())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.program:
            case R.string.menu_program:
                transaction.replace(container, new Program())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_rooms:
                transaction.replace(container, new Rooms())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.greeting:
            case R.string.menu_greeting:
                transaction.replace(container, new GreetingFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_profile:
                transaction.replace(container, new Profile())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_balance:
                transaction.replace(container, new Balance())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.chargen:
            case R.string.menu_chargen:
                transaction.replace(container, new StudentBoard())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_chargen_phil:
                transaction.replace(container, new PhilistinesBoard())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_drinks:
                transaction.replace(container, new Drinks())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_about_us:
                transaction.replace(container, new AboutUs())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_history:
                transaction.replace(container, new HistoryFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_frat_wue:
                transaction.replace(container, new Fraternities_city())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_more_frat_organisation:
                transaction.replace(container, new Fraternities_germany())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_all_profiles:
                transaction.replace(container, new allUsers())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.news:
            case R.string.menu_messages:
                transaction.replace(container, new NewsFragment())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.string.menu_donate:
                transaction.replace(container, new Donation())
                        .addToBackStack(TAG)
                        .commit();
                break;
            case R.id.row2first:
                if (auth.isSignIn()) {
                    changePage(R.string.menu_balance, transaction);
                } else {
                    changePage(R.string.menu_rooms, transaction);
                }
                break;
            case R.id.login:
                if (auth.isSignIn()) {
                    changePage(R.string.menu_logout, transaction);
                } else {
                    changePage(R.string.menu_login, transaction);
                }
                break;
            case R.string.menu_logout:
                auth.signOut();
            case R.string.menu_home:
            default:
                transaction.replace(container, new Home())
                        .addToBackStack(TAG)
                        .commit();
                break;
        }
    }

    public void setListener(ISideNav listener) {
        this.listener = listener;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        fillHeadView();
        fillSideNav();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "Clicked menu item with the id of:" + item.getItemId());
        changePage(item.getItemId(), listener.clicked(item.getItemId()));
        return false;
    }
}
