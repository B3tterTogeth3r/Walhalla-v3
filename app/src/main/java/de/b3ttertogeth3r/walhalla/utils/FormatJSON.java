package de.b3ttertogeth3r.walhalla.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.models.Paragraph;

public class FormatJSON implements Runnable {
    private static final String TAG = "FormatJSON";
    private final Page page;
    private final String configData;

    public FormatJSON (Page page, String configData) {
        this.page = page;
        this.configData = configData;
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
            default:
                Log.e(TAG, "run: page has nothing to format");
        }
    }

    private void room (String assetData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(assetData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            SiteData.roomsList.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.roomsList.add(toList(paragraph_list, page));
                    }
                }
            } else {
                Log.d(TAG, "room: local version");
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.roomsList.add(toList(paragraph_list, page));
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
            Log.d(TAG, "loadJSONFromAsset: " + fileName);
            InputStream is = App.getContext().getAssets().open(fileName);
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
            SiteData.own_history.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.own_history.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.own_history.add(toList(paragraph_list, page));
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
            SiteData.about_us.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.about_us.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.about_us.add(toList(paragraph_list, page));
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
            SiteData.frat_germany.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.frat_germany.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.frat_germany.add(toList(paragraph_list, page));
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
            SiteData.frat_wuerzburg.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                for (int i = 1; i < config.length(); i++) {
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.frat_wuerzburg.add(toList(paragraph_list, page));
                    }
                }
            } else {
                for (int i = 1; i < local.length(); i++) {
                    JSONObject paragraph_list = local.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
                        SiteData.frat_wuerzburg.add(toList(paragraph_list, page));
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
            SiteData.semester_notes.clear();
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                JSONArray paragraph_list = config.getJSONArray("list");
                int size = paragraph_list.length();
                for (int i = 0; i < size; i++) {
                    SiteData.semester_notes.add(paragraph_list.getString(i));
                }
            } else {
                JSONArray paragraph_list = local.getJSONArray("list");
                int size = paragraph_list.length();
                for (int i = 0; i < size; i++) {
                    SiteData.semester_notes.add(paragraph_list.getString(i));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "semester_notes: ", e);
        }
    }

    @NonNull
    private ArrayList<Paragraph> toList (@NonNull JSONObject paragraph_list, Page page) {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        for (int j = 0; j < paragraph_list.length(); j++) {
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
            } catch (Exception e) {
                Log.e(TAG, page.getName() + ": ", e);
            }
        }
        return paragraphs;
    }

}
