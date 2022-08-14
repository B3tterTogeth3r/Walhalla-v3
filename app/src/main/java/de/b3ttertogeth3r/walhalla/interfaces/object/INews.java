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

package de.b3ttertogeth3r.walhalla.interfaces.object;

import android.content.Context;

import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.object.News;
import de.b3ttertogeth3r.walhalla.object.Text;

/**
 * Collection of values and functions inside the {@link News} class.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 3.0
 */
public interface INews extends Validate{
    /**
     * ID of the {@link News} entry
     */
    int ID = -1;
    /**
     * Title of the {@link News} entry
     */
    int TITLE = 0;
    /**
     * User group the {@link News} entry  is visible to
     */
    int VISIBILITY = 1;
    /**
     * Amount of {@link Text} object the {@link News} entry has in its subcollection
     */
    int AMOUNT = 2;
    /**
     * UID of the user the {@link News} entry was uploaded by
     */
    int UPLOADED_BY = 3;


    /**
     * Display the news entry and download the description/content
     *
     * @param context context to create sub views inside the layout
     * @return Layout displaying this entry
     * @throws NoDataException Exception caught by the implementing class
     * @since 2.0
     */
    LinearLayout getView(Context context) throws NoDataException;
}
