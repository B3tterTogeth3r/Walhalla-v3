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

/**
 * Thrown when the download finds no data.
 *
 * @author B3tterTogeth3r
 * @since 2.0
 */
public class NoDataException extends Exception {
    private static final String TAG = "NoDataException";

    /**
     * Constructs a {@code NoDataException} with no detail message
     */
    public NoDataException () {
        super();
    }

    /**
     * Constructs a {@code NoDataException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoDataException(String message) {
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public String toString() {
        return TAG + ": " + getMessage();
    }
}
