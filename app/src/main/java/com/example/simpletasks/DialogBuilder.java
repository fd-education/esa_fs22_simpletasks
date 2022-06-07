package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

/**
 * Builds a custom dialog Use like new DialogBuilder().setContext(context).build().show();
 */
public class DialogBuilder implements IDialogBuilder {
    private Context context;
    private int cancelBtnTextId;
    private int actionBtnTextId;
    private int btnTextId;
    private int descriptionId;
    private Runnable action;
    private boolean isTwoButtonLayout;

    /**
     * This method sets the layout of a 2 button popup.
     *
     * @param cancelBtnTextId Text of the left Button
     * @param actionBtnTextId Text of the right Button
     * @return
     */
    @Override
    public IDialogBuilder setTwoButtonLayout(int cancelBtnTextId, int actionBtnTextId) {
        this.cancelBtnTextId = cancelBtnTextId;
        this.actionBtnTextId = actionBtnTextId;
        isTwoButtonLayout = true;
        return this;
    }

    /**
     * This method sets the layout of a single button popup
     *
     * @param btnTextId sets the text of the button
     * @return
     */
    @Override
    public IDialogBuilder setCenterButtonLayout(int btnTextId) {
        this.btnTextId = btnTextId;
        isTwoButtonLayout = false;
        return this;
    }


    @Override
    public IDialogBuilder setDescriptionText(int textId) {
        this.descriptionId = textId;
        return this;

    }

    @Override
    public IDialogBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public IDialogBuilder setAction(Runnable action) {
        this.action = action;
        return this;
    }

    @Override
    public Dialog build() {
        Dialog dialog = new Dialog(context);

        if (isTwoButtonLayout) {
            dialog.setContentView(R.layout.popup_two_button);
        } else {
            dialog.setContentView(R.layout.popup_one_button);
        }
        TextView descriptionView = dialog.findViewById(R.id.popupText);
        descriptionView.setText(descriptionId);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (isTwoButtonLayout) {
            Button cancelButton = dialog.findViewById(R.id.cancelBTN);
            cancelButton.setOnClickListener(view -> dialog.dismiss());
            cancelButton.setText(cancelBtnTextId);
            Button actionButton = dialog.findViewById(R.id.actionBTN);
            actionButton.setOnClickListener(view -> {
                action.run();
                dialog.dismiss();
            });
            actionButton.setText(actionBtnTextId);
        } else {
            Button acceptButton = dialog.findViewById(R.id.acceptBTN);
            acceptButton.setOnClickListener(view -> {
                if (action != null) {
                    action.run();
                }
                dialog.dismiss();
            });
            acceptButton.setText(btnTextId);
        }

        return dialog;
    }
}
