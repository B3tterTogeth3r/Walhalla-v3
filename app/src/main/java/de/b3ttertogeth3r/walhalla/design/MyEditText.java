package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import static de.b3ttertogeth3r.walhalla.enums.Editable.EMPTY;

public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {

    private de.b3ttertogeth3r.walhalla.enums.Editable editable = EMPTY;

    public MyEditText (Context context) {
        super(context);
        design(context);
    }

    public MyEditText(Context context, EditListener listener){
        super(context);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                listener.sendLiveChange(s.toString(), getEditable());
            }
        });
        design(context);
    }

    public void setEditable(de.b3ttertogeth3r.walhalla.enums.Editable editable){
        this.editable = editable;
    }

    public de.b3ttertogeth3r.walhalla.enums.Editable getEditable(){
        return editable;
    }

    private void design(@NonNull Context context){
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(padding, (int) (0.5 * padding), padding, padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1);
        setLayoutParams(params);
    }
}
