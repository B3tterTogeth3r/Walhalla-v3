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

package de.b3ttertogeth3r.walhalla.dialog;

import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.object.Movement;

public class MovementEditDialog extends Dialog<Movement> {
    private static final String TAG = "MovementEditDialog";
    private final Movement movement;

    public MovementEditDialog(Movement movement) {
        super(DialogSize.WRAP_CONTENT);
        this.movement = movement;
    }

    @NonNull
    public static MovementEditDialog create(FragmentManager manager, Movement movement) throws CreateDialogException {
        try {
            MovementEditDialog dialog = new MovementEditDialog(movement);
            dialog.show(manager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("Unable to create dialog", e);
        }
    }

    @Override
    @Deprecated
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        EditText additional = new EditText(requireContext());
        EditText purpose = new EditText(requireContext());
        EditText amount = new EditText(requireContext());
        Button recipe = new Button(requireContext());
        purpose.setDescription(R.string.balance_purpose);
        amount.setDescription(R.string.balance_amount);
        recipe.setText(R.string.balance_recipe);
        additional.setDescription(R.string.balance_add);
        purpose.setText(movement.getPurpose());
        // TODO: 02.12.22 format to € X,XX
        amount.setText(movement.getAmount().toString());
        additional.setText(movement.getAdd());
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {

    }
}
