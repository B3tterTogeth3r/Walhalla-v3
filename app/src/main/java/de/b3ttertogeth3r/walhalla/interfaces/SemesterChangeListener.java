package de.b3ttertogeth3r.walhalla.interfaces;

import de.b3ttertogeth3r.walhalla.models.Semester;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

public interface SemesterChangeListener {
    void saveDone();

    default void selectorDone(Semester chosenSemester){
        CacheData.setChosenSemester(chosenSemester);
        saveDone();
    }

    default void joinedDone(Semester semester){}
}
