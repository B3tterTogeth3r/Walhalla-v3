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

package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.BaseAdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.common.internal.Preconditions;

import de.b3ttertogeth3r.walhalla.R;

public class AdView extends BaseAdView {
    private static final String TAG = "AdView";

    public AdView(@NonNull Context context) {
        super(context, 0);
        Preconditions.checkNotNull(context, "Context cannot be null");
        design(context, AdSize.BANNER);
    }

    private void design(@NonNull Context context, @NonNull AdSize adSize) {
        setAdSize(adSize);
        setAdUnitId(context.getString(R.string.adUnitId));
        MobileAds.initialize(context, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        loadAd(adRequest);
    }

    public AdView(@NonNull Context context, AdSize adSize) {
        super(context, 0);
        Preconditions.checkNotNull(context, "Context cannot be null");
        design(context, adSize);
    }

    public AdView(@NonNull Context context, @NonNull AttributeSet attrs, int adViewType) {
        super(context, 0);
        Preconditions.checkNotNull(context, "Context cannot be null");
        design(context, AdSize.BANNER);
    }

    @NonNull
    public final VideoController zza() {
        return this.zza.zzf();
    }
}
