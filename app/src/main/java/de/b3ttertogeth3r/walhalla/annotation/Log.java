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


import static de.b3ttertogeth3r.walhalla.interfaces.ILog.DEBUG;
import static de.b3ttertogeth3r.walhalla.interfaces.ILog.ERROR;
import static de.b3ttertogeth3r.walhalla.interfaces.ILog.INFO;
import static de.b3ttertogeth3r.walhalla.interfaces.ILog.VERBOSE;
import static de.b3ttertogeth3r.walhalla.interfaces.ILog.WARN;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({VERBOSE,DEBUG, INFO, WARN, ERROR})
public @interface Log {
}
