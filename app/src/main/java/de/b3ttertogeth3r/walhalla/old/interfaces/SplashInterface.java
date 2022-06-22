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

package de.b3ttertogeth3r.walhalla.old.interfaces;

import de.b3ttertogeth3r.walhalla.old.App;
import de.b3ttertogeth3r.walhalla.old.SplashActivity;

/**
 * @author B3tterTogeth3r
 * @since 1.0
 * @version 1.0
 * @implNote <b>DO NOT IMPLEMENT ANYWHERE EXCEPT {@link SplashActivity SPLASHACTIVITY}</b>
 * @implSpec <b>ONLY IMPLEMENTED BY {@link SplashActivity SPLASHACTIVITY}</b>
 */
public interface SplashInterface {
    /** only use in the constructor of {@link App App} */
    void appDone();
    /** only use in the constructor of {@link de.b3ttertogeth3r.walhalla.old.firebase.Firebase Firebase} */
    void firebaseDone();
}
