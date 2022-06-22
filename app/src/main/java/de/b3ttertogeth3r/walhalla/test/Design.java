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

package de.b3ttertogeth3r.walhalla.test;

import android.content.Context;
import android.view.View;

import de.b3ttertogeth3r.walhalla.old.models.Paragraph;

public class Design {
    private final de.b3ttertogeth3r.walhalla.old.enums.Design design;
    private final Context ctx;

    public Design (de.b3ttertogeth3r.walhalla.old.enums.Design design, Context ctx) {
        this.design = design;
        this.ctx = ctx;
    }

    public View getView () {
        switch (design) {
            case LINK:
            case TEXT:
                return new Text(ctx, new Paragraph());
            default:
                return null;
        }
    }

    private static class Text extends androidx.appcompat.widget.AppCompatTextView {
        public Text (Context ctx, Paragraph p) {
            super(ctx);
        }
    }
}
