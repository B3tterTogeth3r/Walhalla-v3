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

package de.b3ttertogeth3r.walhalla.fragment.common;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.News;

public class NewsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "News";
    private final IFirestoreDownload download = new FirestoreMock.Download();
    private LinearLayout newsLayout;
    private ArrayList<News> newsList = new ArrayList<>();
    private int position, padding;
    private int max_position = 0;
    private Button previous, next;

    @Override
    public void start() {
        download.news(Visibility.PUBLIC)
                .setOnSuccessListener(result -> {
                    newsList = result;
                    if (result != null) {
                        max_position = result.size() - 1;
                    } else {
                        max_position = 0;
                    }
                    loadEntry(0);
                }).setOnFailListener(e -> {
                });
        try {
            padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    4f,
                    requireContext().getResources().getDisplayMetrics()
            );
        } catch (Exception e) {
            padding = 20;
        }
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void stop() {

    }

    @Override
    public void viewCreated() {
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        //loadEntry(position);
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_messages);
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        //region top row buttons
        LinearLayout buttonRow = new LinearLayout(requireContext());
        /*LinearLayout.LayoutParams buttonRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );*/
        previous = new Button(getContext());
        next = new Button(getContext());
        previous.setText(R.string.previous);
        next.setText(R.string.older);
/*
        RelativeLayout.LayoutParams pParams = Button.getParams();
        pParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        previous.setLayoutParams(pParams);

        RelativeLayout.LayoutParams nParams = Button.getParams();
        nParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        next.setLayoutParams(nParams);*/

        buttonRow.addView(next);
        buttonRow.addView(previous);
        view.addView(buttonRow);//, buttonRowParams);
        //endregion

        //region news entry
        newsLayout = new LinearLayout(requireContext());
        view.addView(newsLayout);
        //endregion

        //region advertisement
        // TODO: 30.05.22 add app to GoogleAdMob
        AdView adView = new AdView(requireContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.adUnitId));
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adView.setLayoutParams(adParams);
        //endregion
    }

    private void loadEntry(int number) {
        try {
            newsLayout.removeAllViews();
            newsLayout.removeAllViewsInLayout();
            newsLayout.addView(newsList.get(number).getView(requireContext()));
            newsLayout.invalidate();
        } catch (NoDataException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            if (position == 0) {
                position = max_position;
            } else {
                position--;
            }
        } else if (v == previous) {
            if (position == max_position) {
                position = 0;
            } else {
                position++;
            }
        }
        loadEntry(position);
    }
}