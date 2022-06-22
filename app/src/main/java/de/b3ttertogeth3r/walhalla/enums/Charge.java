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

package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.b3ttertogeth3r.walhalla.object.Text;

public enum Charge {
    X, VX, FM, XX, XXX, AH_X, AH_XXX, AH_XX, AH_HW, VOP, VVOP;

    /**
     * The List contains two items.
     * <ol start=0>
     *     <li>The title and abbreviation of the object</li>
     *     <li>The short description of the job</li>
     * </ol>
     * @return the full description of the
     *
     */
    @NonNull
    @Contract(" -> new")
    public ArrayList<Text> getDescription () {
        Text title = new Text();
        Text description = new Text();
        switch (this) {
            case X:
                title.setValue(new ArrayList<>(Collections.singletonList("Senior (X)")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Der Senior leitet" +
                        " die Aktivitas und vertritt den Verein nach außen. Er übernimmt die " +
                        "Aufgabenverteilung innerhalb der Aktivitas und ist für die Planung, " +
                        "Durchführung und Leitung sämtlicher Verbindungsveranstaltungen " +
                        "verantwortlich.")));
            case VX:
                title.setValue(new ArrayList<>(Collections.singletonList("Consenior (VX)")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Der Consenior " +
                        "unterstützt den Senior bei der Erfüllung seiner Aufgaben und vertritt " +
                        "diesen in Abwesenheit. Darüber hinaus leitet er sämtliche " +
                        "Damefestlichkeiten und ist für die Pflege des Sports (Tennisplatz etc.) " +
                        "zuständig.")));
                break;
            case FM:
                title.setValue(new ArrayList<>(Collections.singletonList("Fuxmajor (FM)")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Der Fuxmajor ist " +
                        "für alle Aspekte der Nachwuchsförderung verantwortlich. Er kümmert sich " +
                        "so besonders um unsere Füchse (Neumitglieder). Jedes Semester wird von " +
                        "ihm ein Herrenessen veranstaltet, bei dem die Füchse unsere Alten Herren" +
                        " bekochen.")));
                break;
            case XX:
                title.setValue(new ArrayList<>(Collections.singletonList("Schriftführer (XX)")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Der Schriftführer" +
                        " hat den gesamten Schriftverkehr der Aktivitas zu besorgen, pflegt das " +
                        "Gästebuch und ist zudem für unseren Facebookauftritt verantwortlich. Er " +
                        "führt eine Liste der aktiven Mitglieder.")));
                break;
            case XXX:
                title.setValue(new ArrayList<>(Collections.singletonList("Kassier (XXX)")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Der Kassier hat " +
                        "die Aktivenkasse und die Aktivenkonten zu verwalten und die " +
                        "dazugehörigen Kassenbücher zu führen. Am Anfang des Semesters stellt er " +
                        "eine finanzielle Planung auf und am Ende des Semesters wird geprüft, ob " +
                        "die Abrechnung korrekt erfolgt ist.")));
                break;
            case VOP:
            case VVOP:
                title.setValue(new ArrayList<>(Collections.singletonList("Vorort")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Übernimmt man auf" +
                        " Verbandsebene eine Charge, heißt diese Vorort. Dieser besteht aus dem " +
                        "Vorortspräsident, dem Vorortsfiezepräsidenten, einem Kassier/Quästor und" +
                        " einem Pressesprecher/Scriptor. Vollständig wird der Vorstand mit einem " +
                        "Beisitzer.")));
                break;
            case AH_X:
            case AH_XXX:
            case AH_XX:
            case AH_HW:
            default:
                title.setValue(new ArrayList<>(Collections.singletonList("Philistervorstand")));
                title.setPosition(0);
                title.setKind(TextType.TITLE);
                description.setKind(TextType.TEXT);
                description.setPosition(1);
                description.setValue(new ArrayList<>(Collections.singletonList("Zusätzlich zum " +
                        "Aktivenvorstand existiert auch ein Vorstand aus den Reihen der Philister" +
                        " (Mitglieder, die das Studium bereits abgeschlossen haben), der " +
                        "regelmäßig neu gewählt wird. Dieser kümmert sich insbesondere um die " +
                        "Verwaltung des Verbindungshauses und um die Mitgliederbeiträge. Außerdem" +
                        " steht er der Aktivitas mit Rat und Tat zur Seite.")));
                break;
        }
        return new ArrayList<>(Arrays.asList(title, description));
    }

    /**
     * @return the abbreviation of the object
     */
    @NonNull
    @Override
    public String toString () {
        return super.toString().replace("_", " ");
    }

    /**
     * @return the full name of the object
     */
    @NonNull
    @Contract(pure = true)
    public String toLongString() {
        switch (this){
            case X:
                return "Senior";
            case VX:
                return "Consenior";
            case FM:
                return "Fuxmajor";
            case XX:
                return "Schriftführer";
            case XXX:
                return "Kassier";
            case AH_X:
                return "Philistersenior";
            case AH_XXX:
                return "Philisterkassier";
            case AH_XX:
                return "Philisterschriftführer";
            case AH_HW:
                return "Hausverwalter";
            case VOP:
                return "Vorortspräsident";
            case VVOP:
                return "Vizevorortspräsident";
            default:
                return "";
        }
    }
}
