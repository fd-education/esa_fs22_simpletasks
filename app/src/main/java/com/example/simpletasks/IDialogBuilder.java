package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;

public interface IDialogBuilder {

    IDialogBuilder setTwoButtonLayout(String leftBtnText, String rightBtnText);

    IDialogBuilder setCenterButtonLayout(String btnText);

    IDialogBuilder setDescriptionText(String text);

    void getResult();

    IDialogBuilder setContext(Context context);

    Dialog build();
}
