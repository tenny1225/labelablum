package com.lenovo.album.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-15.
 */

public class LoadingDialogUtil {
    private static MagicDialog dialog;

    public static void showDialog(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        dialog = new MagicDialog.Builder(context).setContent(v).build().show();
    }

    public static void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;

        }
    }
}
