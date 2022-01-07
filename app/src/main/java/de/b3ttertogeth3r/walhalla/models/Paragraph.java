package de.b3ttertogeth3r.walhalla.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

import de.b3ttertogeth3r.walhalla.firebase.Firestore;

/**
 * The {@code Paragraph} class represents all paragraphs that could be designed
 * in the app. All Paragraphs of the same site are saved inside the same collection
 * in {@link Firestore Cloud Firestore}.
 * <br>
 * All paragraphs have at least these three elements:
 * <ul>
 * <li><u>kind:</u> one of the values defined in the enum
 * {@link de.b3ttertogeth3r.walhalla.enums.Design Design}</li>
 * <li><u>position:</u> the number of the position</li>
 * <li><u>value:</u> the list of values</li>
 * </ul>
 */
public class Paragraph {
    @Exclude
    String id;
    String kind;
    int position;
    List<String> value;

    /**
     * I am an empty constructor
     */
    public Paragraph(){}

    /**
     * I have all the values a paragraph could have
     * @param id the id of the paragraph
     * @param kind choose one from the enum {@link de.b3ttertogeth3r.walhalla.enums.Design Design}
     * @param position number of the position
     * @param value the list of values
     */
    public Paragraph (String id, String kind, int position, List<String> value) {
        this.id = id;
        this.kind = kind;
        this.position = position;
        this.value = value;
    }

    /**
     * I have most of the values a paragraph could have
     * @param kind choose one from the enum {@link de.b3ttertogeth3r.walhalla.enums.Design Design}
     * @param position number of the position
     * @param value the list of values
     */
    public Paragraph (String kind, int position, List<String> value) {
        this.kind = kind;
        this.position = position;
        this.value = value;
    }

    @Exclude
    public String getId () {
        return id;
    }

    @Exclude
    public void setId (String id) {
        this.id = id;
    }

    public String getKind () {
        return kind;
    }

    public void setKind (String kind) {
        this.kind = kind;
    }

    public int getPosition () {
        return position;
    }

    public void setPosition (int position) {
        this.position = position;
    }

    public List<String> getValue () {
        return value;
    }

    public void setValue (List<String> value) {
        this.value = value;
    }
}
