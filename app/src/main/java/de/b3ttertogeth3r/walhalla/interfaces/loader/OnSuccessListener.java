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

package de.b3ttertogeth3r.walhalla.interfaces.loader;

import androidx.annotation.Nullable;

/**
 * Extracted Interface from {@link de.b3ttertogeth3r.walhalla.abstract_generic.Loader Loader}
 *
 * @param <T> Type of Loader
 * @author B3tterTogeth3r
 * @version 1.0
 * @see de.b3ttertogeth3r.walhalla.abstract_generic.Loader Loader
 * @since 2.0
 */
public interface OnSuccessListener<T> {
    /**
     * @param result Result of the download or null
     * @since 1.0
     */
    void onSuccessListener(@Nullable T result) throws Exception;
}
