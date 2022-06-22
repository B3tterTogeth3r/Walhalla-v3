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

import androidx.annotation.Nullable;

/**
 * Extracted Interface from {@link de.b3ttertogeth3r.walhalla.abstract_classes.Loader Loader}
 *
 * @param <T> Type of Loader
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 * @see de.b3ttertogeth3r.walhalla.abstract_classes.Loader Loader
 */
public interface LoaderResultListener<T> {
    /**
     * @param result Result of the download or null
     * @since 1.0
     */
    default void onSuccessListener(@Nullable T result) {
    }

    /**
     * @param e Download threw an exception
     * @since 1.0
     */
    default void onFailureListener(Exception e) {
    }
}
