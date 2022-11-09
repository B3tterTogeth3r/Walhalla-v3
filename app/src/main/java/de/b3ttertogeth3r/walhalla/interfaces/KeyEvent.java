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

package de.b3ttertogeth3r.walhalla.interfaces;

public interface KeyEvent extends android.view.KeyEvent.Callback {
    @Override
    default boolean onKeyDown(int i, android.view.KeyEvent keyEvent) {
        return keyDown(i, keyEvent);
    }

    @Override
    default boolean onKeyLongPress(int i, android.view.KeyEvent keyEvent) {
        return false;
    }

    @Override
    default boolean onKeyUp(int i, android.view.KeyEvent keyEvent) {
        return false;
    }

    @Override
    default boolean onKeyMultiple(int i, int i1, android.view.KeyEvent keyEvent) {
        return false;
    }

    boolean keyDown(int i, android.view.KeyEvent keyEvent);
}
