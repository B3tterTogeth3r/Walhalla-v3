/*
 * Copyright (c) 2022-2022.
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

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.enums.TextType;
import de.b3ttertogeth3r.walhalla.interfaces.loader.LoaderResultListener;
import de.b3ttertogeth3r.walhalla.interfaces.loader.OnFailureListener;
import de.b3ttertogeth3r.walhalla.interfaces.loader.OnSuccessListener;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Text;

public class FormatJSON implements LoaderResultListener<ArrayList<Paragraph<Text>>> {
    private static final String TAG = "FormatJSON";
    private final String rawData;
    private final ArrayList<Paragraph<Text>> list = new ArrayList<>();
    private OnSuccessListener<ArrayList<Paragraph<Text>>> resultListenerSuccess;
    private OnFailureListener<ArrayList<Paragraph<Text>>> resultListenerFail;

    public FormatJSON(String rawData) {
        this.rawData = rawData;
    }

    public void start() {
        try {
            JSONObject config = new JSONObject(rawData);
            for (int i = 1; i < config.length(); i++) {
                JSONObject paragraph_list = config.getJSONObject("paragraph_" + i);
                if (paragraph_list.length() != 0) {
                    list.add(toList(paragraph_list));
                    if (resultListenerSuccess != null) {
                        resultListenerSuccess.onSuccessListener(list);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "chargen_description: ", e);
            if (resultListenerFail != null) {
                resultListenerFail.onFailureListener(e);
            }
        }
    }

    @NonNull
    private Paragraph<Text> toList(@NonNull JSONObject paragraph_list) {
        Paragraph<Text> paragraphs = new Paragraph<>();
        int j;
        for (j = 0; j < paragraph_list.length(); j++) {
            try {
                JSONObject content =
                        paragraph_list.getJSONObject(String.valueOf(j));
                Text p = new Text();
                // TODO does it work like this?
                try {
                    p.setKind(TextType.find(content.getString("kind")));
                } catch (Exception e) {
                    p.setKind(TextType.find(content.getInt("kind")));
                    Log.e(TAG, "toList: ", e);
                }
                p.setPosition(content.getInt("position"));
                JSONArray array = content.getJSONArray("value");
                ArrayList<String> value = new ArrayList<>();
                for (int a = 0; a < array.length(); a++) {
                    value.add(array.getString(a));
                }
                p.setValue(value);
                paragraphs.add(p);
            } catch (Exception e) {
                Log.e(TAG, "Parsing error in toList at position " + j, e);
            }
        }
        return paragraphs;
    }

    public FormatJSON setOnSuccessListener(OnSuccessListener<ArrayList<Paragraph<Text>>> onSuccessListener) {
        this.resultListenerSuccess = onSuccessListener;
        return this;
    }

    public FormatJSON setOnFailListener(OnFailureListener<ArrayList<Paragraph<Text>>> onFailureListener) {
        this.resultListenerFail = onFailureListener;
        return this;
    }
}
