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

package de.b3ttertogeth3r.walhalla.old.utils;

import static de.b3ttertogeth3r.walhalla.old.enums.Charge.FM;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.NONE;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.VX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.X;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.XX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.XXX;

import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.b3ttertogeth3r.walhalla.old.App;
import de.b3ttertogeth3r.walhalla.old.enums.Page;
import de.b3ttertogeth3r.walhalla.old.models.Paragraph;

public class FormatJSON implements Runnable {
    private static final String TAG = "FormatJSON";
    private final Page page;
    private final String configData;

    public FormatJSON (Page page, String configData) {
        this.page = page;
        this.configData = configData;
        // TODO: 08.02.22 on first app start configData == null -> Fix the JSONObject getter
    }

    @Override
    public void run () {
        switch (page) {
            case ROOM:
                room(loadJSONFromAsset(Page.ROOM.getName()));
                break;
            case OWN_HISTORY:
                own_history(loadJSONFromAsset(Page.OWN_HISTORY.getName()));
                break;
            case ABOUT_US:
                about_us(loadJSONFromAsset(Page.ABOUT_US.getName()));
                break;
            case FRAT_GER:
                frat_germany(loadJSONFromAsset(Page.FRAT_GER.getName()));
                break;
            case FRAT_WUE:
                frat_wuerzburg(loadJSONFromAsset(Page.FRAT_WUE.getName()));
                break;
            case SEMESTER_NOTES:
                semester_notes(loadJSONFromAsset(Page.SEMESTER_NOTES.getName()));
                break;
            case CHARGEN_DESCRIPTION:
                chargen_description(loadJSONFromAsset(Page.CHARGEN_DESCRIPTION.getName()));
                break;
            default:
                Log.e(TAG, "run: " + page.getName() + " has nothing to format");
                break;
        }
    }

    private void chargen_description (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject local = new JSONObject(localData);
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.chargen_description.clear();
            JSONObject object;
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                object = config;
            } else {
                object = local;
            }
            String x = object.getString(X.getName());
            String vx = object.getString(VX.getName());
            String fm = object.getString(FM.getName());
            String xx = object.getString(XX.getName());
            String xxx = object.getString(XXX.getName());
            String desc = object.getString(NONE.getName());
            RemoteConfigData.chargen_description.addAll(Arrays.asList(x, vx, fm, xx, xxx, desc));
        } catch (Exception e) {
            Log.e(TAG, "chargen_description: ", e);
        }
    }

    private void room (String assetData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(assetData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.roomsList.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.roomsList.add(toList(paragraph_list, page));
                    }
                }
            } else {
                Log.d(TAG, "room: local version");
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.roomsList.add(toList(paragraph_list, page));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "room: ", e);
        }
    }

    @Nullable
    private String loadJSONFromAsset (@NonNull String name) {
        String result;
        try {
            String fileName = name + ".json";
            // Log.d(TAG, "loadJSONFromAsset: " + fileName);
            AssetManager manager = App.getContext().getApplicationContext().getAssets();
            InputStream is = manager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            int amount = is.read(buffer, 0, size);
            is.close();
            result = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    private void own_history (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.own_history.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    try {
                        JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                        if (paragraph_list.length() != 0) {
                            RemoteConfigData.own_history.add(toList(paragraph_list, page));
                        }
                    } catch (Exception ignored){
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.own_history.add(toList(paragraph_list, page));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "own_history: ", e);
        }
    }

    private void about_us (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.about_us.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.about_us.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.about_us.add(toList(paragraph_list, page));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "about_us: ", e);
        }
    }

    private void frat_germany (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.frat_germany.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.frat_germany.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.frat_germany.add(toList(paragraph_list, page));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "frat_germany: ", e);
        }
    }

    private void frat_wuerzburg (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.frat_wuerzburg.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.frat_wuerzburg.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        RemoteConfigData.frat_wuerzburg.add(toList(paragraph_list, page));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "frat_wuerzburg: ", e);
        }
    }

    private void semester_notes (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            RemoteConfigData.semester_notes.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                JSONArray paragraph_list = config.getJSONArray("list");
                int size = paragraph_list.length();
                for (int i = 0; i < size; i++) {
                    RemoteConfigData.semester_notes.add(paragraph_list.getString(i));
                }
            } else {
                JSONArray paragraph_list = local.getJSONArray("list");
                int size = paragraph_list.length();
                for (int i = 0; i < size; i++) {
                    RemoteConfigData.semester_notes.add(paragraph_list.getString(i));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "semester_notes: ", e);
        }
    }

    @NonNull
    private ArrayList<Paragraph> toList (@NonNull JSONObject paragraph_list, Page page) {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        int j;
        for (j = 0; j < paragraph_list.length(); j++) {
            try {
                JSONObject content =
                        paragraph_list.getJSONObject(String.valueOf(j));
                Paragraph p = new Paragraph();
                p.setKind(content.getString("kind"));
                p.setPosition(content.getInt("position"));
                JSONArray array = content.getJSONArray("value");
                List<String> value = new ArrayList<>();
                for (int a = 0; a < array.length(); a++) {
                    value.add(array.getString(a));
                }
                p.setValue(value);
                paragraphs.add(p);
            } catch (Exception ignored) {
                Log.e(TAG, "Parsing error in " + page.getName() + " at position " + j);
            }
        }
        return paragraphs;
    }

}
