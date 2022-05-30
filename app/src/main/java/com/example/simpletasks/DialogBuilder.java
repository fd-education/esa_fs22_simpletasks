/* package com.example.simpletasks;

import android.app.Dialog;
import android.content.Context;

public class DialogBuilder extends Context implements IDialogBuilder {
    private String leftBtnld;
    private String leftBtnText;
    private String rightBtnld;
    private String rightBtnText;

    public DialogBuilder() {

    }


    public void setTwoButtonLayout(String leftBtnld, String leftBtnText, String rightBtnld, String rightBtnText) {
        this.leftBtnld = leftBtnld;
        this.leftBtnText = leftBtnText;
        this.rightBtnld = rightBtnld;
        this.rightBtnText = rightBtnText;

    }

    public void setCenterButtonLayout(String btnld, String btnText) {
        this.btnld = btnld;
        this.btnText = btnText;
    }

    public void setMainText(String text) {
        this.text = text;

    }

    public void getResult() {

    }
    public Dialog build() {
        Dialog dialog = new Dialog(this);
        return dialog;
    }
}
*/