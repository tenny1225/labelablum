package com.lenovo.album.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-14.
 */

public class MagicDialog extends Dialog {
    public MagicDialog(@NonNull Context context) {
        super(context);
    }

    public MagicDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private View content;
        private  MagicDialog dialog;
        public Builder(Context context) {
            this.context =context;

        }
        public Builder setContent(View content){
            this.content = content;
            return this;
        }

        public Builder  build(){
            dialog = new MagicDialog(context, R.style.Translucent_NoTitle);
            dialog.setContentView(content);
            dialog.setCanceledOnTouchOutside(false);
            return this;
        }
        public MagicDialog show(){
            dialog.show();
            return dialog;
        }
    }
}
