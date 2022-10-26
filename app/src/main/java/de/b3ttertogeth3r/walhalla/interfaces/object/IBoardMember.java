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

package de.b3ttertogeth3r.walhalla.interfaces.object;

import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public interface IBoardMember extends Validate {
    int ID = -1;
    int FULL_NAME = 0;
    int MOBILE = 1;
    int MAJOR = 2;
    int MAIL = 3;
    int FROM = 4;
    int SEMESTER = 5;
    int CHARGE = 6;
    int IMAGE = 7;
    int UID = 8;

    RelativeLayout getView(@NonNull FragmentActivity activity);
}
