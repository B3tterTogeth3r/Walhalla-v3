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

package de.b3ttertogeth3r.walhalla.annotation;

import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.COLLAR;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.DESCRIPTION;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.END;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.PUNCTUALITY;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.TIME;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.TITLE;
import static de.b3ttertogeth3r.walhalla.interfaces.object.IEvent.VISIBILITY;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.b3ttertogeth3r.walhalla.interfaces.object.IEvent;

@Retention(RetentionPolicy.SOURCE)
@IntDef({IEvent.ID, TITLE, DESCRIPTION, COLLAR, PUNCTUALITY, TIME, END, VISIBILITY})
public @interface EventValue {
}