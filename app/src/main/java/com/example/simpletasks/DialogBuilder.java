package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

/**
 * Builds a custom dialog
 * Use like new DialogBuilder().setContext(context).build().show();
 */
public class DialogBuilder implements IDialogBuilder {
    private Context context;
    private String leftBtnText;
    private String rightBtnText;
    private String btnText;
    private int descriptionId;

    public DialogBuilder() {

    }

    @Override
    public IDialogBuilder setTwoButtonLayout(String leftBtnText, String rightBtnText) {
        this.leftBtnText = leftBtnText;
        this.rightBtnText = rightBtnText;
        return this;
    }

    @Override
    public IDialogBuilder setCenterButtonLayout(String btnText) {
        this.btnText = btnText;
        return this;
    }


    @Override
    public IDialogBuilder setDescriptionText(int textId) {
        this.descriptionId = textId;
        return this;

    }

    @Override
    public void getResult() {

    }

    @Override
    public IDialogBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public Dialog build() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup);
        TextView descriptionView = dialog.findViewById(R.id.popupText);
        descriptionView.setText(descriptionId);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
