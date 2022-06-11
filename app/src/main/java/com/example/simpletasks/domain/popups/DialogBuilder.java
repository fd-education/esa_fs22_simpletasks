package com.example.simpletasks.domain.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.example.simpletasks.R;
import com.example.simpletasks.domain.popups.IDialogBuilder;

/**
 * Builds a custom dialog Use like new DialogBuilder().setContext(context).build().show();
 */
public class DialogBuilder implements IDialogBuilder {
    private Context context;
    private int cancelBtnTextId;
    private int actionBtnTextId;
    private int btnTextId;
    private int descriptionText;
    private Runnable action;
    private boolean isTwoButtonLayout;
    private Object[] descriptionFormatArgs;

    /**
     * This method sets the layout of a 2 button popup.
     *
     * @param cancelBtnTextId Text of the left Button
     * @param actionBtnTextId Text of the right Button
     * @return returns the text for the buttons to build them
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
     * @return the text for the button to build
     */
    @Override
    public IDialogBuilder setCenterButtonLayout(int btnTextId) {
        this.btnTextId = btnTextId;
        isTwoButtonLayout = false;
        return this;
    }

    /**
     * This method sets the text for the description
     *
     * @param textId the text id to fill in
     * @return the text which needs to be filled by the Builder
     */
    @Override
    public IDialogBuilder setDescriptionText(int textId) {
        this.descriptionText = textId;
        return this;

    }

    /**
     * This method sets the text for the description and also accepts formatting arguments for the string
     *
     * @param textId the text id to fill in
     * @return the text which needs to be filled by the Builder
     */
    @Override
    public IDialogBuilder setDescriptionText(int textId, Object... formatArgs) {
        this.descriptionText = textId;
        this.descriptionFormatArgs = formatArgs;
        return this;
    }

    /**
     * This Method returns all of the context need via the interface
     *
     * @param context the context which is provided by the Builder Interface
     * @return the context to be returned for the build
     */
    @Override
    public IDialogBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * sets the action which needs to be executed
     *
     * @param action sets the content which action should be executed
     * @return the action to provide it for the builder
     */
    @Override
    public IDialogBuilder setAction(Runnable action) {
        this.action = action;
        return this;
    }

    /**
     * The build method which builds the popups in the app and provides the buttons and
     * Textfield with the text
     *
     * @return the dialog which fills the popups with text
     */
    @Override
    public Dialog build() {
        Dialog dialog = new Dialog(context);

        if (isTwoButtonLayout) {
            dialog.setContentView(R.layout.popup_two_button);
        } else {
            dialog.setContentView(R.layout.popup_one_button);
        }
        TextView descriptionView = dialog.findViewById(R.id.popupText);
        if (descriptionFormatArgs == null) {
            descriptionView.setText(descriptionText);
        } else {
            descriptionView.setText(context.getString(descriptionText, descriptionFormatArgs));
        }
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
