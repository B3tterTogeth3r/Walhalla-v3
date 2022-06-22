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

package de.b3ttertogeth3r.walhalla.old.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.App;
import de.b3ttertogeth3r.walhalla.old.MainActivity;
import de.b3ttertogeth3r.walhalla.old.adapter.MyImageItem;
import de.b3ttertogeth3r.walhalla.old.design.MyEditText;
import de.b3ttertogeth3r.walhalla.old.design.MyImageButton;
import de.b3ttertogeth3r.walhalla.old.design.MyLinearLayout;
import de.b3ttertogeth3r.walhalla.old.design.MyToast;
import de.b3ttertogeth3r.walhalla.old.enums.Charge;
import de.b3ttertogeth3r.walhalla.old.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.old.firebase.Functions;
import de.b3ttertogeth3r.walhalla.old.firebase.Storage;
import de.b3ttertogeth3r.walhalla.old.fragments_main.ChargenFragment;
import de.b3ttertogeth3r.walhalla.old.fragments_main.ChargenPhilFragment;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.models.Image;
import de.b3ttertogeth3r.walhalla.old.models.Person;
import de.b3ttertogeth3r.walhalla.old.models.SearchModel;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;
import de.b3ttertogeth3r.walhalla.old.utils.Format;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class EditChargeDialog extends DialogFragment implements DialogInterface.OnClickListener,
        View.OnClickListener {
    private static final String TAG = "EditChargeDialog";
    private static final int IMAGE_SELECT_INTENT = 129;
    private static FragmentManager fm;
    private final Charge kind;
    private final de.b3ttertogeth3r.walhalla.old.models.Charge charge;
    private MyEditText first_name, last_name, pob, mobile, major, mail;
    private MyImageButton image;
    private Bitmap imageBitmap;

    public EditChargeDialog (Charge kind,
                             de.b3ttertogeth3r.walhalla.old.models.Charge charge) {
        this.kind = kind;
        this.charge = charge;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        first_name = new MyEditText(requireContext());
        last_name = new MyEditText(requireContext());
        pob = new MyEditText(requireContext());
        mobile = new MyEditText(requireContext());
        major = new MyEditText(requireContext());
        image = new MyImageButton(requireContext());
        mail = new MyEditText(requireContext());
        MyLinearLayout layout = new MyLinearLayout(getContext());
        layout.setBackground(null);
        layout.setOrientation(LinearLayout.VERTICAL);

        first_name.setHint(R.string.first_name);
        last_name.setHint(R.string.last_name);
        pob.setHint(R.string.pob);
        mobile.setHint(R.string.mobile);
        major.setHint(R.string.major);
        mail.setHint(R.string.fui_email_hint);
        image.setText(R.string.select_image);
        image.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.wappen_round));
        image.setOnClickListener(this);

        if (charge != null) {
            first_name.setContent(charge.getFirst_Name());
            last_name.setContent(charge.getLast_Name());
            pob.setContent(charge.getPoB());
            mobile.setContent(charge.getMobile());
            mobile.setInputType(InputType.TYPE_CLASS_PHONE);
            major.setContent(charge.getMajor());
            mail.setContent(charge.getMail());
            mail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            if (charge.getPicture_path() != null && !charge.getPicture_path().isEmpty()) {
                StorageReference imageRef =
                        Storage.downloadImage(charge.getPicture_path());
                Glide.with(this)
                        .load(imageRef)
                        .placeholder(R.drawable.wappen_round)
                        .fitCenter()
                        .into(image.getImageView());
            }
        }

        layout.addView(first_name);
        layout.addView(last_name);
        layout.addView(pob);
        layout.addView(major);
        layout.addView(mobile);
        layout.addView(mail);
        layout.addView(image);

        builder.setTitle(kind.getName())
                .setView(layout)
                .setNeutralButton(R.string.select_person, this)
                .setPositiveButton(R.string.send, this)
                .setNegativeButton(R.string.abort, this)
                .setCancelable(false);

        return builder.create();
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dismiss();
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            charge.setFirst_Name(first_name.getString());
            charge.setLast_Name(last_name.getString());
            charge.setMobile(mobile.getString());
            charge.setMajor(major.getString());
            charge.setMail(mail.getString());
            charge.setPoB(pob.getString());

            //  Upload changes or new charge image
            if (imageBitmap != null) {
                String imageName =
                        (charge.getFullName() + " " + CacheData.getChosenSemester().getName_short()) + ".jpeg";
                charge.setPicture_path("/images/" + Format.imageName(imageName));
                //Upload image and change file name
                Firestore.uploadImage(imageBitmap, imageName,
                        new MyCompleteListener<Void>() {
                            @Override
                            public void onSuccess (Void result) {

                            }

                            @Override
                            public void onFailure (Exception exception) {
                                Crashlytics.error(MyCompleteListener.TAG,
                                        "ImageUploadFailure", exception);
                            }
                        });
            }

            // If person with this name exists, copy uid;
            // else create person and auth account and insert uid
            // upload charge
            if (charge.getId() != null && !charge.getId().isEmpty()) {
                uploadCharge(charge);
            } else if (charge.getMail().isEmpty()) {
                uploadCharge(charge);
            } else {
                Firestore.findUserByName(charge.getFirst_Name(), charge.getLast_Name(),
                        new MyCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess (QuerySnapshot querySnapshot) {
                                // Get uid from Person
                                try {
                                    DocumentSnapshot ds = querySnapshot.getDocuments().get(0);
                                    Person p = ds.toObject(Person.class);
                                    if (p != null) {
                                        if (!p.getId().isEmpty()) {
                                            charge.setId(p.getId());
                                        }
                                        uploadCharge(charge);
                                    } else {
                                        onFailure(null);
                                    }
                                } catch (Exception e) {
                                    onFailure(e);
                                }
                            }

                            @Override
                            public void onFailure (Exception exception) {
                                // Create user and add uid to the charge
                                Functions.createUser(charge, new MyCompleteListener<String>() {
                                    @Override
                                    public void onSuccess (String string) {
                                        charge.setId(string);
                                        uploadCharge(charge);
                                        Firestore.uploadPerson(charge.getPerson());
                                    }

                                    @Override
                                    public void onFailure (Exception exception) {
                                        Crashlytics.error(TAG, "Creating user did not work",
                                                exception);
                                    }
                                });
                            }
                        });
            }
            dismiss();
        } else if (which == DialogInterface.BUTTON_NEUTRAL) {
            // open dialog with all persons
            Firestore.findAllPerson(new MyCompleteListener<QuerySnapshot>() {
                @Override
                public void onSuccess (QuerySnapshot querySnapshot) {
                    ArrayList<Person> peopleList = new ArrayList<>();
                    ArrayList<SearchModel> list = new ArrayList<>();
                    for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                        try {
                            Person p = ds.toObject(Person.class);
                            if (p != null) {
                                peopleList.add(p);
                                list.add(new SearchModel(p.getFullName()));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    //Start person search dialog
                    showSearchDialog(peopleList, list);
                }

                @Override
                public void onFailure (Exception exception) {

                }
            });
        }
    }

    private void uploadCharge (de.b3ttertogeth3r.walhalla.old.models.Charge charge) {
        Firestore.uploadCharge(CacheData.getChosenSemester(), charge, kind,
                new MyCompleteListener<Void>() {

                    @Override
                    public void onSuccess (Void result) {
                        MyToast toast = new MyToast(MainActivity.parentLayout.getContext());
                        toast.setText(R.string.upload_complete);
                        toast.show();
                    }

                    @Override
                    public void onFailure (Exception exception) {
                        Crashlytics.error(TAG, "editing/adding charge did not work",
                                exception);
                    }
                });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void showSearchDialog (ArrayList<Person> peopleList, ArrayList<SearchModel> modelList) {
        try {
            //TODO add adapter with persons profile pic before name
            new SimpleSearchDialogCompat(App.getContext(), "Search...", "Search for a person",
                    null, modelList,
                    (SearchResultListener<Searchable>) (dialog, item, position) -> {
                        Log.d(TAG, "onSelected: " + peopleList.get(position).getFullName());
                        de.b3ttertogeth3r.walhalla.old.models.Charge c =
                                new de.b3ttertogeth3r.walhalla.old.models.Charge(peopleList.get(position));
                        dialog.dismiss();
                        EditChargeDialog.display(fm, kind, c);
                    }
            ).show();
        } catch (Exception e) {
            Log.e(TAG, "showSearchDialog: ", e);
        }
    }

    /**
     * Display the dialog and adding it to the given fragment.
     *
     * @param fragmentManager
     *         The FragmentManager this fragment will be added to.
     *         tag – The tag for this fragment, as per FragmentTransaction.add.
     * @param kind
     *         The Charge that is to be added or edit
     * @param charge
     *         the object to be updated
     */
    public static void display (FragmentManager fragmentManager, Charge kind,
                                de.b3ttertogeth3r.walhalla.old.models.Charge charge) {
        try {
            EditChargeDialog.fm = fragmentManager;
            EditChargeDialog dialog = new EditChargeDialog(kind, charge);
            dialog.setCancelable(false);
            dialog.show(fragmentManager, TAG);
        } catch (Exception e) {
            Crashlytics.error(TAG, "Could not start dialog");
        }
    }

    @Override
    public void onClick (View v) {
        if (v == image) {
            // select an image
            Log.d(TAG, "onClick: image selected");
            // Make user choose between online images and upload a new image
            // If user chooses from uploaded images, set the image into the imageView
            // and insert the path into the Charge object
            CharSequence[] selector = new String[2];
            selector[0] = getString(R.string.from_online);
            selector[1] = getString(R.string.from_device);
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.choose)
                    .setNegativeButton(R.string.abort, null)
                    .setItems(selector, (dialog, which) -> {
                        if (which == 0) {
                            Log.d(TAG, "onClick: select one from online images");
                            selectImages();
                        } else if (which == 1) {
                            Log.d(TAG, "onClick: select a local image");
                            Intent selectImage = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(selectImage, IMAGE_SELECT_INTENT);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void selectImages () {
        Firestore.findAllImages(new MyCompleteListener<QuerySnapshot>() {
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    onFailure();
                }
                ArrayList<Image> imageList = new ArrayList<>();
                for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                    Image image = ds.toObject(Image.class);
                    if (image != null) {
                        imageList.add(image);
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Images");
                MyImageItem adapter = new MyImageItem(requireContext(), imageList);
                builder.setAdapter(adapter, (dialog, which) -> {
                    Log.e(TAG, "selected: " + imageList.get(which).getDescription());
                    image.setImage(imageList.get(which).getLarge_path());
                    charge.setPicture_path(imageList.get(which).getLarge_path());
                })
                        .setNegativeButton(R.string.abort, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailure (Exception exception) {
                Crashlytics.error(TAG, "loading all Images did not work", exception);
            }
        });
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_INTENT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        Uri selectedData = data.getData();
                        imageBitmap =
                                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedData);
                        image.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onDismiss (@NonNull DialogInterface dialog) {
        // reload Fragment
        switch(kind){
            case X:
            case VX:
            case FM:
            case XX:
            case XXX:
                Analytics.screenChange(2131230862, getString(R.string.menu_chargen));
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChargenFragment()).commit();
                break;
            case AH_X:
            case AH_XX:
            case AH_XXX:
            case AH_HW:
                Analytics.screenChange(2131689727, getString(R.string.menu_chargen_phil));
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChargenPhilFragment()).commit();

                break;
        }
        super.onDismiss(dialog);
    }
}
