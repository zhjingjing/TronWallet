package com.zj.tronwallet.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zj.tronwallet.R;


/**
 * 加载数据不确定进度对话框
 *
 * @author Created by jiangyujiang on 2017/5/4.
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
    }

    public LoadingDialog(@NonNull Context context, boolean cancelable) {
        super(context, R.style.LoadingDialog);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }

    public static LoadingDialog show(Context context, boolean cancelable) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }
}
