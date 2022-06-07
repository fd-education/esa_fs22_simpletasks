package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;

public interface IDialogBuilder {

    IDialogBuilder setTwoButtonLayout(int leftBtnText, int rightBtnText);

    IDialogBuilder setCenterButtonLayout(int btnText);

    IDialogBuilder setDescriptionText(int textId);

    IDialogBuilder setDescriptionText(int textId, Object... formatArgs);

    IDialogBuilder setContext(Context context);

    IDialogBuilder setAction(Runnable action);

    Dialog build();
}
