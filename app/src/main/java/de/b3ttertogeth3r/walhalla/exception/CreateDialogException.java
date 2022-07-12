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

package de.b3ttertogeth3r.walhalla.exception;

import androidx.annotation.NonNull;

public class CreateDialogException extends Exception {
    private static final String TAG = "CreateDialogException";

    public CreateDialogException() {
        super();
    }

    public CreateDialogException(String message) {
        super(message);
    }

    public CreateDialogException(Throwable cause) {
        super(cause);
    }

    public CreateDialogException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public String toString() {
        return TAG + ": " + getMessage();
    }
}
