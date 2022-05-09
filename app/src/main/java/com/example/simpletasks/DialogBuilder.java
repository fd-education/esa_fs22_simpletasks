package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;

public class DialogBuilder  {
    private final String leftBtnld;
    private final String leftBtnText;
    private final String rightBtnld;
    private final String rightBtnText;

    public DialogBuilder(String leftBtnld, String leftBtnText, String rightBtnld, String rightBtnText) {
        this.leftBtnld = leftBtnld;
        this.leftBtnText = leftBtnText;
        this.rightBtnld = rightBtnld;
        this.rightBtnText = rightBtnText;
    }


    public void setTwoButtonLayout(String leftBtnld, String leftBtnText, String rightBtnld, String rightBtnText) {

    }

    public void setCenterButtonLayout(String btnld, String btnText) {

    }

    public void setMainText(String text) {

    }

    public void getResult() {
        
    }
    public Dialog build() {
        Dialog dialog = new Dialog(this);
        validateDialogObject(dialog);
        return dialog;
    }

    private void validateDialogObject(Dialog dialog) {
    }
}
