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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.UnaryOperator;

import de.b3ttertogeth3r.walhalla.object.Text;

public class Paragraph<T extends Text> extends ArrayList<Text> {

    @Override
    public void trimToSize() {
        super.trimToSize();
        sort();
    }

    @Override
    public Text set(int index, Text element) {
        Text result = super.set(index, element);
        sort();
        return result;
    }

    @Override
    public boolean add(Text text) {
        boolean result = super.add(text);
        sort();
        return result;
    }

    public void sort() {
        super.sort(Comparator.comparingInt(Text::getPosition));
    }

    @Override
    public void add(int index, Text element) {
        super.add(index, element);
        sort();
    }

    @Override
    public Text remove(int index) {
        Text result = super.remove(index);
        sort();
        return result;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        boolean result = super.remove(o);
        sort();
        return result;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Text> c) {
        boolean result = super.addAll(c);
        sort();
        return result;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends Text> c) {
        boolean result = super.addAll(index, c);
        sort();
        return result;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        sort();
    }

    @Override
    public void replaceAll(@NonNull UnaryOperator<Text> operator) {
        super.replaceAll(operator);
        sort();
    }

    @Override
    public void sort(@Nullable Comparator<? super Text> c) {
        super.sort(c);
    }
}
