package com.example.simpletasks.domain.popups;

import android.app.Dialog;
import android.content.Context;

public interface IDialogBuilder {

    IDialogBuilder setTwoButtonLayout(int leftBtnText, int rightBtnText);

    IDialogBuilder setCenterButtonLayout(int btnText);

    IDialogBuilder setDescriptionText(int textId);

    IDialogBuilder setContext(Context context);

    IDialogBuilder setAction(Runnable action);

    Dialog build();
}
