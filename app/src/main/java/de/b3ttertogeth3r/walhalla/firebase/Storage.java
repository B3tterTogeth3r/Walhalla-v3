package de.b3ttertogeth3r.walhalla.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
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
    public static void getUri (@NonNull String imagePath, @NonNull MyCompleteListener<Uri> listener) {
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
    public static void uploadRecipe (Bitmap recipeBitmap, String name, @NonNull MyCompleteListener<Void> listener) {
        String imageName = Format.imageName(name);
        REFERENCE.child("recipe/" + imageName)
                .putBytes(compressImage(recipeBitmap))
                .addOnSuccessListener(taskSnapshot -> listener.onSuccess(null))
                .addOnFailureListener(listener::onFailure);
    }

    @NonNull
    private static byte[] compressImage (@NonNull Bitmap original) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static void uploadImage (Bitmap imageBitmap, String name,
                                    @NonNull MyCompleteListener<Uri> listener) {
        String imagePath = "images/" + Format.imageName(name);
        REFERENCE.child(imagePath)
                .putBytes(compressImage(imageBitmap))
                .addOnSuccessListener(taskSnapshot -> listener.onSuccess(taskSnapshot.getUploadSessionUri()))
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Download the requested recipe
     *
     * @param recipe_name
     *         the name of the recipe to download
     */
    public static void downloadRecipe (String recipe_name, @NonNull MyCompleteListener<Bitmap> listener) {
        REFERENCE.child("images" + recipe_name).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Bitmap result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            listener.onSuccess(result);
        }).addOnFailureListener(listener::onFailure);
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
