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

package de.b3ttertogeth3r.walhalla.object;

import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.TextType;
import de.b3ttertogeth3r.walhalla.interfaces.object.Validate;

public class Text implements Validate {
    int position;
    ArrayList<String> value;
    TextType kind;
    private String id;

    public Text(int position, ArrayList<String> value, TextType kind) {
        this.position = position;
        this.value = value;
        this.kind = kind;
    }

    public Text() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public View getView(FragmentActivity activity) {
        LinearLayout view = new LinearLayout(activity);
        if (kind != null)
            try {
                switch (kind) {
                    case BULLET_ITEM:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.BulletItem(activity, s));
                        }
                        break;
                    case BUTTON:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.Button(activity, s));
                        }
                        break;
                    case EMAIL:
                        // TODO: 22.06.22 create email design thingy
                    case LINK:
                        // TODO: 22.06.22 create special link buttons
                    case TEXT:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.Text(activity, s));
                        }
                        break;
                    case IMAGE:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.Image(activity, s));
                        }
                        break;
                    case SUBTITLE1:
                    case SUBTITLE2:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.Subtitle1(activity, s));
                        }
                        break;
                    case TABLE_ROW:
                        view.addView(new de.b3ttertogeth3r.walhalla.design.TableRow(activity, value));
                        break;
                    case TITLE:
                        for (String s : value) {
                            view.addView(new de.b3ttertogeth3r.walhalla.design.Title(activity, s));
                        }
                        break;
                    case TOAST:
                        for (String s : value) {
                            Toast.makeToast(activity, s).show();
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e("object/Text", "getView: " + getPosition(), e);
            }
        return view;
    }

    public TextType getKind() {
        return kind;
    }

    public void setKind(TextType kind) {
        this.kind = kind;
    }
}
