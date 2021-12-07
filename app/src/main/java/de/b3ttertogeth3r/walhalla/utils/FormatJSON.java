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

    private void room (String assetData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(assetData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            if (localVersion.getLong("versionNumber") != configVersion.getLong("versionNumber")) {
                SiteData.roomsList.clear();
                ArrayList<Paragraph> paragraphs;
                Log.e(TAG, "room: " + config.length());
                for (int i = 1; i < config.length(); i++) {
                    paragraphs = new ArrayList<>();
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
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
                                Log.e(TAG, "room: ", e);
                            }
                        }
                        SiteData.roomsList.add(paragraphs);
                    }
                }
            }
            //Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
        } catch (Exception e) {
            Log.e(TAG, "room: ", e);
        }
    }

    private void own_history (String localData) {
        try {
            JSONObject config = new JSONObject(configData);
            JSONObject local = new JSONObject(localData);
            JSONObject configVersion = config.getJSONObject("version");
            JSONObject localVersion = local.getJSONObject("version");
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                SiteData.own_history.clear();
                ArrayList<Paragraph> paragraphs;
                Log.e(TAG, "own_history: " + config.length());
                for (int i = 1; i < config.length(); i++) {
                    paragraphs = new ArrayList<>();
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
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
                                Log.e(TAG, "own_history: ", e);
                            }
                        }
                        SiteData.own_history.add(paragraphs);
                    }
                }
            }
            // Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
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
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                SiteData.about_us.clear();
                ArrayList<Paragraph> paragraphs;
                Log.e(TAG, "about_us: " + config.length());
                for (int i = 1; i < config.length(); i++) {
                    paragraphs = new ArrayList<>();
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
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
                                Log.e(TAG, "about_us: ", e);
                            }
                        }
                        SiteData.about_us.add(paragraphs);
                    }
                }
            }
            // Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
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
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                SiteData.frat_germany.clear();
                ArrayList<Paragraph> paragraphs;
                Log.e(TAG, "frat_germany: " + config.length());
                for (int i = 1; i < config.length(); i++) {
                    paragraphs = new ArrayList<>();
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
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
                                Log.e(TAG, "frat_germany: ", e);
                            }
                        }
                        SiteData.frat_germany.add(paragraphs);
                    }
                }
            }
            // Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
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
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                SiteData.frat_wuerzburg.clear();
                ArrayList<Paragraph> paragraphs;
                Log.e(TAG, "frat_wuerzburg: " + config.length());
                for (int i = 1; i < config.length(); i++) {
                    paragraphs = new ArrayList<>();
                    JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                    if (paragraph_list.length() != 0) {
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
                                Log.e(TAG, "frat_wuerzburg: " + j, e);
                            }
                        }
                        SiteData.frat_wuerzburg.add(paragraphs);
                    }
                }
            }
            // Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
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
            if (localVersion.getLong("versionNumber") < configVersion.getLong("versionNumber")) {
                SiteData.semester_notes.clear();
                Log.e(TAG, "semester_notes: " + config.length());
                JSONArray paragraph_list = config.getJSONArray("list");
                int size = paragraph_list.length();
                for(int i =0;i<size;i++) {
                    SiteData.semester_notes.add(paragraph_list.getString(i));
                }
            }
            // Log.d(TAG, "room: roomsList.size() = " + SiteData.roomsList.size());
        } catch (Exception e) {
            Log.e(TAG, "semester_notes: ", e);
        }
    }

}
