package de.b3ttertogeth3r.walhalla.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import de.b3ttertogeth3r.walhalla.interfaces.CustomFirebaseCompleteListener;
import de.b3ttertogeth3r.walhalla.utils.Format;

/**
 * local functions for the apps Firebase storage buckets.
 *
 * @see <a href="https://firebase.google.com/docs/storage">Storage</a>
 */
public class Storage {
    private static final String TAG = "Storage";
    protected static FirebaseStorage STORAGE;
    /** Reference to the root path */
    protected static StorageReference REFERENCE;
    private static final long ONE_MEGABYTE = 1024 * 1024;


    /**
     * Get the uri of an image path in the default storage bucket
     * x
     * @param imagePath of the storage path
     * @param listener get the resulting uri or an exception
     */
    public static void getUri (@NonNull String imagePath, @NonNull CustomFirebaseCompleteListener listener) {
        REFERENCE.child(imagePath)
                .getDownloadUrl()
                .addOnSuccessListener(listener::onSuccess)
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Download an image from the {@link Storage Image Storage Bukket}
     *
     * @param reference_path
     *         path to put into the storage reference
     * @return reference to put into firebase glide
     * @see
     * <a href="https://firebase.google.com/docs/storage/android/download-files#downloading_images_with_firebaseui">Downloading
     * Images with FirebaseU</a>
     */
    @NonNull
    public static StorageReference downloadImage (String reference_path) {
        return REFERENCE.child(reference_path);
    }

    /**
     * Upload an image into the recipe bucket of the firebase storage.
     *
     * @param recipeBitmap
     *         Bitmap of the file
     * @param name
     *         name of the file to upload
     */
    public static void uploadRecipe (Bitmap recipeBitmap, String name) {
        String imageName = Format.imageName(name);
        REFERENCE.child("recipe/" + imageName)
                .putBytes(compressImage(recipeBitmap))
                .addOnSuccessListener(taskSnapshot -> Crashlytics.log(TAG + ":onSuccess: " +
                        "upload of image " + imageName + "complete."))
                .addOnFailureListener(e -> Crashlytics.log(TAG + ":onFailure: upload of image" +
                        " " + imageName + " failed.", e));
    }

    @NonNull
    private static byte[] compressImage (@NonNull Bitmap original) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static void uploadImage (Bitmap imageBitmap, String name,
                                    @NonNull CustomFirebaseCompleteListener listener) {
        String imagePath = "images/" + Format.imageName(name);
        REFERENCE.child(imagePath)
                .putBytes(compressImage(imageBitmap))
                .addOnSuccessListener(task -> listener.onSuccess(imagePath))
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Download the requested recipe
     *
     * @param recipe_name
     *         the name of the recipe to download
     * @return bitmap value of the recipe
     */
    public static Bitmap downloadRecipe (String recipe_name) {
        final Bitmap[] result = {null};
        REFERENCE.child("images" + recipe_name).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            result[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Crashlytics.log(TAG + ":downloadImage:onFailure: download failed", exception);
        });

        while (result[0] == null) {
            // wait
        }
        return result[0];
    }

    /**
     * Upload the protocol file of a specific meeting
     */
    public static void uploadProtocol () {
        // TODO: upload protocol of a meeting only as a pdf file
    }

    /**
     * download the protocol file of a specific meeting.
     *
     * @param protocol_name
     *         name of the file
     */
    public static void downloadProtocol (String protocol_name) {
        // TODO: download protocol of a meeting, if it has a pdf file.
    }
}
