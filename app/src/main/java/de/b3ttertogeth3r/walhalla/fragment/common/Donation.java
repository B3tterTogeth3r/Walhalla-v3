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

package de.b3ttertogeth3r.walhalla.fragment.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Toast;

public class Donation extends Fragment {
    private static final String TAG = "Donation";

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(TAG);
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        de.b3ttertogeth3r.walhalla.design.LinearLayout l1 = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireActivity());
        de.b3ttertogeth3r.walhalla.design.LinearLayout l2 = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireActivity());
        de.b3ttertogeth3r.walhalla.design.LinearLayout l3 = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireActivity());
        de.b3ttertogeth3r.walhalla.design.LinearLayout l4 = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireActivity());

        String t1S = "Konto der Aktivitas";
        Text t1 = new Text(requireActivity(), t1S);
        String iban1 = "DE10 7509 0300 0103 0118 10";
        Button b1 = new Button(requireActivity(), iban1);
        b1.setOnClickListener(view1 -> {
            ClipData clip = ClipData.newPlainText(t1S, iban1);
            clipboard.setPrimaryClip(clip);
            Toast.makeToast(requireActivity(), R.string.copied_to_clipboard).show();
        });
        l1.addView(t1);
        l1.addView(b1);

        String t2S = "Hausbauverein Walhalla e.V.";
        Text t2 = new Text(requireActivity(), t2S);
        String iban2 = "DE80 7905 0000 0000 0679 59";
        Button b2 = new Button(requireActivity(), iban2);
        b2.setOnClickListener(view1 -> {
            ClipData clip = ClipData.newPlainText(t2S, iban2);
            clipboard.setPrimaryClip(clip);
            Toast.makeToast(requireActivity(), R.string.copied_to_clipboard).show();
        });
        l2.addView(t2);
        l2.addView(b2);

        String t3S = "K.St.V. Walhalla";
        Text t3 = new Text(requireActivity(), t3S);
        String iban3 = "DE50 7905 0000 0000 2099 57";
        Button b3 = new Button(requireActivity(), iban3);
        b3.setOnClickListener(view1 -> {
            ClipData clip = ClipData.newPlainText(t3S, iban3);
            clipboard.setPrimaryClip(clip);
            Toast.makeToast(requireActivity(), R.string.copied_to_clipboard).show();
        });
        l3.addView(t3);
        l3.addView(b3);

        String t4S = "Spendenkonto Hausbau";
        Text t4 = new Text(requireActivity(), t4S);
        String iban4 = "DE39 7905 0000 0047 3816 11";
        Button b4 = new Button(requireActivity(), iban4);
        b4.setOnClickListener(view1 -> {
            ClipData clip = ClipData.newPlainText(t4S, iban4);
            clipboard.setPrimaryClip(clip);
            Toast.makeToast(requireActivity(), R.string.copied_to_clipboard).show();
        });
        l4.addView(t4);
        l4.addView(b4);

        view.addView(l1);
        view.addView(l2);
        view.addView(l3);
        view.addView(l4);
        view.invalidate();
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }
}
