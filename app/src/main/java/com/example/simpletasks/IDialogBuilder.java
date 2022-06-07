package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;

public interface IDialogBuilder {

    IDialogBuilder setTwoButtonLayout(String leftBtnText, String rightBtnText);

    IDialogBuilder setCenterButtonLayout(String btnText);

    IDialogBuilder setDescriptionText(int textId);

    void getResult();

    IDialogBuilder setContext(Context context);

    Dialog build();
}
