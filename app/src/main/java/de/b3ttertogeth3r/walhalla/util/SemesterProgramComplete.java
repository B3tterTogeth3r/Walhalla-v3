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

package de.b3ttertogeth3r.walhalla.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Walhalla;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Semester;

/**
 * TODO find another way or find a way to create it in here
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://www.vogella.com/tutorials/JavaPDF/article.html">Creating PDF with Java and
 * iText by Lars Vogel</a>
 * @since 2.0
 */
public class SemesterProgramComplete {
    private static final String TAG = "SemesterProgramComplete";
    private static final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subtitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);
    private static final Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
    private final Context context;
    private final Semester sem;
    private final IFirestoreDownload download;
    private final Toast toast;

    public SemesterProgramComplete(Context context, Semester semester) throws FileNotFoundException, DocumentException {
        this.context = context;
        this.sem = semester;
        this.toast = new Toast(context);
        download = new FirestoreMock.Download();
        Document doc = new Document();
        PdfWriter.getInstance(doc,
                new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + java.io.File.separator + "programWalhalla"));
        doc.open();
        MainActivity.loadingInterface.start();
        addMetaData(doc);
    }

    /**
     * Add Meta data to the file with the following settings:
     * <ul>
     *     <li>Title: {@link #sem semester}s long name</li>
     *     <li>Author: The senior of the semester</li>
     *     <li>Creator: The app with iTextPDF</li>
     *     <li>Subject: Program of the fraternities name</li>
     * </ul>
     * <br>
     * resumes with {@link #addTitlePage(Document, ArrayList)} )}
     *
     * @param doc document
     * @since 1.0
     */
    private void addMetaData(@NonNull Document doc) {
        download.board(Rank.ACTIVE, sem.getId())
                .setOnSuccessListener(result -> {
                    doc.addTitle(sem.getName_long());
                    if(result != null)
                    for (BoardMember bm : result) {
                        if (bm.getCharge() == Charge.X) {
                            doc.addAuthor(bm.getFull_name());
                            doc.addSubject("Semesterprogramm " + sem.getName_short() + " " + bm.getFull_name());
                        }
                    }
                    doc.addCreator("Erstellt mit der App");
                    try {
                        addTitlePage(doc, result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).setOnFailListener(e ->
                        ToastList.addToast(
                                toast.setMessage("Beim Erstellen des Programms ist etwas schief gelaufen. " +
                                        "Versuche es später erneut."))
                );
    }

    /**
     * Add the title page with the name of the fraternity at the top, the large shield in the middle
     * and the long name of the semester at the bottom.
     *
     * @param doc  document
     * @param list previous downloaded list of {@link BoardMember}s
     * @since 1.0
     */
    private void addTitlePage(@NonNull Document doc, ArrayList<BoardMember> list) throws IOException {
        Paragraph title = new Paragraph();
        title.add(new Paragraph(Walhalla.NAME_1.toString(), titleFont));
        title.add(new Paragraph(Walhalla.NAME_2.toString(), subtitleFont));

        Drawable d = ContextCompat.getDrawable(context, R.drawable.wappen_2017);
        assert d != null;
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        Image image = null;
        try {
            image = Image.getInstance(bitmapdata);
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        title.add(image);

        title.add(new Paragraph(sem.getName_long()));
        try {
            doc.add(title);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addGreeting(doc, list);
    }

    /**
     * @param doc  document
     * @param list previous downloaded list of {@link BoardMember}s
     * @since 1.0
     */
    private void addGreeting(@NonNull Document doc, ArrayList<BoardMember> list) {
        doc.newPage();
        // add content
        download.semesterGreeting(sem.getId())
                .setOnSuccessListener(result ->
                        addAboutUs(doc, list)
                )
                .setOnFailListener(e ->
                        ToastList.addToast(
                                toast.setMessage("Beim Erstellen des Programms ist etwas schief gelaufen. " +
                                        "Versuche es später erneut.")));
    }

    /**
     * @param doc  document
     * @param list previous downloaded list of {@link BoardMember}s
     */
    private void addAboutUs(@NonNull Document doc, ArrayList<BoardMember> list) {
        doc.newPage();
        //add content
        addBoard(doc, list);
    }

    /**
     * @param doc  document
     * @param list previous downloaded list of {@link BoardMember}s
     * @since 1.0
     */
    private void addBoard(@NonNull Document doc, ArrayList<BoardMember> list) {
        doc.newPage();
        //add content
        addEvents(doc);
    }

    /**
     * All public events of the selected {@link #sem Semester}
     *
     * @param doc document
     * @since 1.0
     */
    private void addEvents(@NonNull Document doc) {
        doc.newPage();
        //add content
        download.semesterEvents(sem.getId())
                .setOnSuccessListener(result -> addNotes(doc))
                .setOnFailListener(e ->
                        ToastList.addToast(toast.setMessage("Beim Erstellen des Programms ist etwas schief gelaufen. Versuche es später erneut.")));
    }

    /**
     * A list of the selected notes for the selected {@link #sem semester}.
     *
     * @param doc document
     * @since 1.0
     */
    private void addNotes(@NonNull Document doc) {
        doc.newPage();
        //add content
        download.semesterNotes(sem.getId())
                .setOnSuccessListener(result -> addFux(doc))
                .setOnFailListener(e -> {
                    ToastList.addToast(toast.setMessage("Beim Erstellen des Programms ist etwas schief gelaufen. " +
                            "Versuche es später erneut."));
                });
    }

    /**
     * A list of the "Fuxenstunden" which will be hold this semester. They change according to the
     * semester. An even semester-id results in different ones than a semester with an odd
     * semester-id.
     *
     * @param doc document
     * @since 1.0
     */
    private void addFux(@NonNull Document doc) {
        doc.newPage();
        //add content
        addHistory(doc);
    }

    /**
     * A page with the short history of the fraternity.
     *
     * @param doc document
     * @since 1.0
     */
    private void addHistory(@NonNull Document doc) {
        doc.newPage();
        //add content
        addPhilistines(doc);
    }

    /**
     * A page with the BoarMembers of the philistines. Below them their IBANs are displayed. In
     * contrast to the the student board they don't have an image.
     *
     * @param doc document
     * @since 1.0
     */
    private void addPhilistines(@NonNull Document doc) {
        doc.newPage();
        //add content
        download.board(Rank.PHILISTINES, sem.getId())
                .setOnSuccessListener(result -> {
                    addMapNotes(doc);
                })
                .setOnFailListener(e ->
                        ToastList.addToast(toast.setMessage("Beim Erstellen des Programms ist etwas schief gelaufen. Versuche es später erneut.")));
    }

    /**
     * A page with either an image of the location of the fraternity or an empty page to take notes.
     * Which it is is chosen at random.
     *
     * @param doc document
     * @since 1.0
     */
    private void addMapNotes(@NonNull Document doc) {
        doc.newPage();
        //add content
        addLastPage(doc);
    }

    /**
     * A page with one of 3 Images and the full address with email and website at the bottom
     *
     * @param doc document
     * @since 1.0
     */
    private void addLastPage(@NonNull Document doc) {
        doc.newPage();
        //add content
        close(doc);
    }

    /**
     * close the edited document
     *
     * @param doc document
     * @since 1.0
     */
    private void close(@NonNull Document doc) {
        MainActivity.loadingInterface.stop();
        doc.close();
    }
}
