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

package de.b3ttertogeth3r.walhalla.abstract_classes;

import androidx.annotation.Nullable;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.interfaces.LoaderResultListener;
import de.b3ttertogeth3r.walhalla.interfaces.OnFailureListener;
import de.b3ttertogeth3r.walhalla.interfaces.OnSuccessListener;

/**
 * A listener for download results. It can display a loading circle over the whole ui to prevent the
 * user of tapping anything else while waiting.
 *
 * @param <T> Type of the awaited result.
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public class Loader<T> implements LoaderResultListener<T> {
    private static final String TAG = "Loader";
    private final boolean startLoadingCircle;
    private OnSuccessListener<T> resultListenerSuccess;
    private OnFailureListener<T> resultListenerFail;
    private T result;
    private boolean hasResult = false;
    private Exception e;

    /**
     * Constructor with the ability to display the loading circle.
     *
     * @param startLoadingCircle if true, loading circle is displayed.
     * @since 1.0
     */
    public Loader(boolean startLoadingCircle) {
        this.startLoadingCircle = startLoadingCircle;
        onStart();
    }

    /**
     * Logging the generic type of the instance.<br>
     * If {@link #startLoadingCircle} is true, the loading circle is made visible via the {@link
     * MainActivity#loadingInterface}
     *
     * @since 1.0
     */
    private void onStart() {
        if (startLoadingCircle) {
            MainActivity.loadingInterface.start();
        }
    }

    /**
     * Constructor in which the loading circle is not going to be displayed.
     *
     * @since 1.0
     */
    public Loader() {
        this.startLoadingCircle = false;
        onStart();
    }

    @Override
    public void onSuccessListener(@Nullable T result) {
        try {
            resultListenerSuccess.onSuccessListener(result);
        } catch (Exception ex) {
            onFailureListener(ex);
        }
    }

    @Override
    public void onFailureListener(Exception e) {
        resultListenerFail.onFailureListener(e);
    }

    /**
     * Download done with no result.
     *
     * @since 1.0
     */
    public Loader<T> done() {
        if (startLoadingCircle) {
            MainActivity.loadingInterface.stop();
        }
        if (resultListenerSuccess != null) {
            try {
                resultListenerSuccess.onSuccessListener(null);
            } catch (Exception e) {
                if (resultListenerFail != null)
                    resultListenerFail.onFailureListener(e);
            }
        } else {
            hasResult = true;
            result = null;
        }
        return this;
    }

    /**
     * Download done with a result
     *
     * @param result downloaded value(s)
     * @since 1.0
     */
    public Loader<T> done(@Nullable T result) {
        if (startLoadingCircle) {
            MainActivity.loadingInterface.stop();
        }
        if (resultListenerSuccess != null) {
            try {
                resultListenerSuccess.onSuccessListener(result);
            } catch (Exception e) {
                if (resultListenerFail != null)
                    resultListenerFail.onFailureListener(e);
            }
        } else {
            this.hasResult = true;
            this.result = result;
        }
        return this;
    }

    public Loader<T> setOnSuccessListener(OnSuccessListener<T> onSuccessListener) {
        this.resultListenerSuccess = onSuccessListener;
        if (hasResult) {
            try {
                onSuccessListener.onSuccessListener(result);
                result = null;
            } catch (Exception e) {
                done(e);
            }
        }
        return this;
    }

    /**
     * Download didn't finish successfully.
     *
     * @param e An exception is to be thrown.
     * @since 1.0
     */
    public Loader<T> done(Exception e) {
        if (startLoadingCircle) {
            MainActivity.loadingInterface.stop();
        }
        if (resultListenerFail != null) {
            onFailureListener(e);
        } else {
            this.e = e;
        }
        return this;
    }

    public Loader<T> setOnFailListener(OnFailureListener<T> onFailureListener) {
        this.resultListenerFail = onFailureListener;
        if (e != null) {
            onFailureListener.onFailureListener(e);
        }
        return this;
    }
}
