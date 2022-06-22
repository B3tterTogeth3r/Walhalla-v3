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

package de.b3ttertogeth3r.walhalla.abstract_classes;

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
 * @see android.view.View.OnTouchListener Android default OnTouchListener
 * @see ClickSwipeListener
 * @author B3tterTogeth3r
 * @since 2.0
 * @version 1.0
 */
public class TouchListener<T> implements OnTouchListener, ClickSwipeListener<T> {
    private static final String TAG = "LinearLayoutTouchListen";
    private static final int MIN_DISTANCE = 100;
    private static final int HALF_SECOND_IN_NANO = 800000000;
    private float downX, downY, upX, upY;
    private Timestamp startTime;
    private final T object;

    public TouchListener(T object) {
        this.object = object;
    }

    public boolean onTouch(View v, @NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                startTime = new Timestamp(new Date());
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // longClick?
                if ((Math.abs(Timestamp.now().getNanoseconds() - startTime.getNanoseconds())) > HALF_SECOND_IN_NANO) {
                    onLongClick(object, v);
                    return true;
                }
                // swipe horizontal?
                else if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // swipe right
                    if (deltaX < 0) {
                        onLeftToRightSwipe(object, v);
                        return true;
                    }
                    // swipe left
                    if (deltaX > 0) {
                        onRightToLeftSwipe(object, v);
                        return true;
                    }
                }
                // swipe vertical?
                else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // swipe down
                    if (deltaY < 0) {
                        onTopToBottomSwipe(object, v);
                        return true;
                    }
                    // swipe up
                    if (deltaY > 0) {
                        onBottomToTopSwipe(object, v);
                        return true;
                    }
                }
                // no long click, no horizontally or vertically swipe, so just a Click
                else {
                    onClick(object, v);
                    return true;
                }
            }
        }
        return false;
    }
}
