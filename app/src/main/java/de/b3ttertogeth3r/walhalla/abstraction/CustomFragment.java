package de.b3ttertogeth3r.walhalla.abstraction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;

/**
 * This class is so that every fragment inside the app looks the same, has similar runtime and a
 * similar looking basic code for better understanding. Because every {@code fragment} inside the
 * app needs the same start method, the same variable for the {@link #toolbar}, should register all
 * its {@link #registration realtimelistener} in the same list, so they can be all stopped at {@link
 * #onStop()} this super class was created.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public abstract class CustomFragment extends Fragment implements FirebaseAuth.IdTokenListener {
    private static final String TAG = "CustomFragment";
    /**
     * A list to collect all realtime listeners into the Firestore database.
     *
     * @see ListenerRegistration
     * @see Firestore
     * @since 1.0
     */
    public ArrayList<ListenerRegistration> registration;
    /**
     * The top Toolbar of the whole application
     *
     * @see Toolbar
     */
    public Toolbar toolbar;

    @NonNull
    @Override
    public String toString () {
        return "CustomFragment by B3tterTogeth3r";
    }

    /**
     * @implNote Don't call in the extending classes of this {@link CustomFragment}
     * @deprecated use abstract subclass
     */
    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * @param inflater
     *         The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container
     *         If non-null, this is the parent view that the fragment's UI should be attached to.
     *         The fragment should not add the view itself, but this can be used to generate the
     *         LayoutParams of the view.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @return Return the View for the fragment's UI, or null.
     * @deprecated use abstract {@link #createView(View, LayoutInflater)} instead.
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        createView(view, inflater);
        return view;
    }

    /**
     * @param view
     *         The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @deprecated use abstract {@link #viewCreated()} instead
     */
    @Override
    public void onViewCreated (@NonNull @NotNull View view,
                               @Nullable Bundle savedInstanceState) {
        try {
            toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.findViewById(R.id.custom_title).setVisibility(View.GONE);
            toolbar.setTitle("Walhalla");
            toolbar.getMenu().clear();
        } catch (Exception ignored) {
        } finally {
            super.onViewCreated(view, savedInstanceState);
            hideKeyBoard();
            viewCreated();
            toolbarContent();
        }
    }

    /**
     * @deprecated use abstract {@link #start()} instead
     */
    @Override
    public void onStart () {
        try {
            super.onStart();
            registration = new ArrayList<>();
        } finally {
            start();
            analyticsProperties();
        }
    }

    /**
     * called before {@link #createView(View, LayoutInflater)} and {@link #viewCreated()}. In it
     * {@link #registration} should be set before the site start.
     */
    public abstract void start ();

    /**
     * Set the properties for firebase analytics. It will be run after {@link #start()}
     */
    public abstract void analyticsProperties ();

    @Override
    public void onResume () {
        super.onResume();
        try {
            toolbarContent();
        } catch (Exception e) {
            Crashlytics.error(TAG, "Refilling toolbar at onResume did not work", e);
        }
    }

    /**
     * @deprecated use abstract {@link #stop()} instead
     */
    @Override
    public void onStop () {
        super.onStop();
        try {
            for (ListenerRegistration reg : registration) {
                reg.remove();
            }
            registration.clear();
        } catch (Exception e) {
            Crashlytics.error(TAG, "Something went wrong while removing the snapshot listener",
                    e);
        } finally {
            toolbar.getMenu().clear();
            toolbar.setTitle(R.string.app_name);
            toolbar.setSubtitle("");
            stop();
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally tied to {@link #onStop()
     * onStop} of the containing Activity's lifecycle.
     *
     * @implNote Called after every entry in {@link #registration} got stopped and the list
     * cleared.
     * @see #onStop()
     */
    public abstract void stop ();

    /**
     * Hides the keyboard on the start of a new Fragment
     *
     * @see
     * <a href="https://stackoverflow.com/questions/7940765/how-to-hide-the-soft-keyboard-inside-a-fragment">Ian
     * G. Clifton's answer</a>
     * @see InputMethodManager
     */
    private void hideKeyBoard () {
        try {
            final InputMethodManager imm =
                    (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(TAG, "hideKeyBoard: ", e);
        }
    }

    /**
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void viewCreated ();

    /**
     * Called before {@link #viewCreated() viewCreated} returns a result. This is to format the
     * toolbar in every Subclass the same way.
     *
     * @see #toolbar
     */
    public abstract void toolbarContent ();

    /**
     * Create the view and initialize the necessary variables for the site.
     *
     * @param view
     *         inflated View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param inflater
     *         LayoutInflater for inflating new Layouts into the view
     * @implNote <b>DON'T CALL FUNCTIONS THAT WORK WITH DATA OF {@link #start()} IN HERE</b>
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void createView (@NonNull @NotNull View view,
                                     @NonNull @NotNull LayoutInflater inflater);

    @Override
    public void onIdTokenChanged (@NonNull FirebaseAuth firebaseAuth) {
        authStatusChanged();
    }

    /**
     * Fired after the authentication token changed into a valid state
     */
    public abstract void authStatusChanged ();
}
