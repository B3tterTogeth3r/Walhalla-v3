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

package de.b3ttertogeth3r.walhalla.interfaces.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;

public abstract class OnCompleteListener<TResult, T> implements com.google.android.gms.tasks.OnCompleteListener<TResult> {
    final Loader<T> loader;

    public OnCompleteListener(Loader<T> loader) {
        this.loader = loader;
    }

    @Override
    public void onComplete(@NonNull Task<TResult> task) {
        if (task.getException() != null) {
            loader.done(task.getException());
            return;
        }
        if (task.getResult() == null) {
            loader.done(new NoDataException("No download data found"));
            return;
        }
        try {
            done(task.getResult());
        } catch (Exception e) {
            loader.done(e);
        }
    }

    public abstract void done(TResult result);
}
