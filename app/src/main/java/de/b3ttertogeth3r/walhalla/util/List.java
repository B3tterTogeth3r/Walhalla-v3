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

package de.b3ttertogeth3r.walhalla.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_generic.MyObject;

public class List <T extends MyObject> extends ArrayList<T> {
    public void sort() {
        super.sort((o1, o2) -> Integer.compare(o1.getTime().compareTo(o2.getTime()),
                (o2.getTime().compareTo(o1.getTime()))));
    }

    @Override
    public T remove (int index) {
        T t = super.remove(index);
        sort();
        return t;
    }

    @Override
    public boolean remove (@Nullable Object o) {
        boolean v = super.remove(o);
        sort();
        return v;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        sort();
    }

    @Override
    public boolean add (T t) {
        boolean value = super.add(t);
        sort();
        return value;
    }

    @Override
    public void add (int index, T element) {
        super.add(index, element);
        sort();
    }

    @Override
    public T set (int index, T element) {
        T t = super.set(index, element);
        sort();
        return t;
    }
}
