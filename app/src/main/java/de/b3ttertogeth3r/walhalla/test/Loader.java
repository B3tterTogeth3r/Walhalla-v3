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

package de.b3ttertogeth3r.walhalla.test;

import java.util.Objects;

import de.b3ttertogeth3r.walhalla.old.utils.MyLog;

public abstract class Loader<T> {
    private static final String TAG = "Loader";
    private LoadingCircle loader = null;
    private String classType;

    public Loader (LoadingCircle loadingCircle) {
        this.loader = loadingCircle;
        onStart();
    }

    private void onStart () {
        classType =
                Objects.requireNonNull(getClass().getGenericSuperclass()).getClass().getTypeName();
        MyLog.i(TAG, "Loading of a " + classType + " object started");
        if (loader != null) {
            loader.start();
        }
    }

    public Loader () {
        onStart();
    }

    protected void done (Object result) {
        MyLog.i(TAG, "Download of a " + classType + " object complete");
        if (result == null) {
            success(null);
        } else {
            try {
                success((T) result);
            } catch (ClassCastException e) {
                exception((Exception) result);
            } catch (Exception exception) {
                exception(exception);
            }
        }
        if (loader != null) {
            loader.stop();
        }
    }

    abstract void success (T result);

    abstract void exception (Exception e);

}

