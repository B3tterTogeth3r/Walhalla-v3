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

package de.b3ttertogeth3r.walhalla.abstract_generic;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.Date;

import de.b3ttertogeth3r.walhalla.interfaces.ClickSwipeListener;

/**
 * This onTouchListener is to define if the users swiped, short clicked or long clicked on the view.
 * Depending on the motion this listener returns the a {@link ClickSwipeListener} function.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see android.view.View.OnTouchListener Android default OnTouchListener
 * @see ClickSwipeListener
 * @since 2.0
 */
public class Touch implements OnTouchListener, ClickSwipeListener {
    private static final String TAG = "TouchListen";
    private static final int MIN_DISTANCE = 100;
    private static final int HALF_SECOND_IN_NANO = 800000000;
    private float downX;
    private float downY;
    private Timestamp startTime;

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, @NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                startTime = new Timestamp(new Date());
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float upX = event.getX();
                float upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // longClick?
                if ((Math.abs(Timestamp.now().getNanoseconds() - startTime.getNanoseconds())) > HALF_SECOND_IN_NANO) {
                    onLongClick(v);
                    return true;
                }
                // swipe horizontal?
                else if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // swipe right
                    if (deltaX < 0) {
                        onLeftToRightSwipe(v);
                        return true;
                    }
                    // swipe left
                    if (deltaX > 0) {
                        onRightToLeftSwipe(v);
                        return true;
                    }
                }
                // swipe vertical?
                else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // swipe down
                    if (deltaY < 0) {
                        onTopToBottomSwipe(v);
                        return true;
                    }
                    // swipe up
                    if (deltaY > 0) {
                        onBottomToTopSwipe(v);
                        return true;
                    }
                }
                // no long click, no horizontally or vertically swipe, so just a Click
                else {
                    onClick(v);
                    return true;
                }
            }
        }
        return false;
    }
}
