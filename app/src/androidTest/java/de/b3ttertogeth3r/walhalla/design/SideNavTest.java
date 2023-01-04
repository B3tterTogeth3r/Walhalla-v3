/*
 * Copyright (c) 2022-2023.
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

package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.Log;

import junit.framework.TestCase;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Response;

public class SideNavTest extends TestCase {
    Context ctx = App.getContext();

    public void testFillHeadView() {
        assertEquals(Response.OK, new SideNav(ctx).fillHeadView());
    }

    public void testChangePage() {

        int result = SideNav.changePage(R.string.menu_home, null);
        Log.e("SideNavTest", "testChangePage: result int = " + result);
    }

    public void testSetListener() {
    }

    public void testOnNavigationItemSelected() {
    }
}