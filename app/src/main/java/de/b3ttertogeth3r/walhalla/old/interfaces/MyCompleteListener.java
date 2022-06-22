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

import androidx.annotation.Nullable;

/**
 * A custom listener to make a notification on firebase download results more easy.
 * I learned Generics with this class and <a href="https://www.youtube.com/watch?v=K1iu1kXkVoA">this
 * video</a>.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see
 * <a href="https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data">Wait
 * on Firebase Result</a> Answer on GitHub I kindly used.
 * @since 1.1
 */
public interface MyCompleteListener<T> {
    String TAG = "OnGetDataListener";

    /**
     * Data returned by the Listener
     *
     * @param result
     *         value of the result. Can be null
     */
    void onSuccess (@Nullable T result);

    /**
     * The loading failed (No error send)
     */
    default void onFailure () {
        onFailure(null);
    }

    /**
     * A loading exception occurred. The listener gets notified with the exception.
     *
     * @param exception
     *         Error exception
     */
    void onFailure (@Nullable Exception exception);

    /**
     * notify of started loading
     */
    default void onBegin () {
    }
}
