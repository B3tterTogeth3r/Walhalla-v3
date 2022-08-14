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

package de.b3ttertogeth3r.walhalla.enums;

/**
 * Group of Ranks to determine visibilities of sites, events, news entries and more.
 * <br>Entries are:
 * <ol start=0>
 *     <li>{@link #PUBLIC}</li>
 *     <li>{@link #SIGNED_IN}</li>
 *     <li>{@link #ACTIVE}</li>
 *     <li>{@link #PHILISTINES}</li>
 *     <li>{@link #BOARD}</li>
 * </ol>
 *
 * @author B3tterTogeth3r
 * @since 2.0
 */
public enum Visibility {
    /** used for public access sites, events and news entries */
    PUBLIC,
    /** used for sites that are only accessible for signed in users */
    SIGNED_IN,
    /** The site, event or news entry is only visible to active members of the fraternity */
    ACTIVE,
    /** The site, event or news entry is only visible to philistine members of the fraternity */
    PHILISTINES,
    /** The site, event or news entry is only visible to active board members */
    BOARD

}
