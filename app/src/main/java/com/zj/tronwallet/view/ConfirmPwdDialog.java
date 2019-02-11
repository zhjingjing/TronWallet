package com.zj.tronwallet.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.tronwallet.R;
import com.zj.tronwallet.inter.ConfirmPwdClickListener;

/**
 * create by zj on 2019/1/7
 */
public class ConfirmPwdDialog extends Dialog {

    private String checkPwd;
    public ConfirmPwdDialog(@NonNull Context context) {
        super(context,R.style.NoFrameDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_confirm_pwd);
        initView();


    }

    public void initView(){
        final EditText editText=findViewById(R.id.edit_pwd);
        TextView tvCancel=findViewById(R.id.tv_cancel);
        TextView tvConfirm=findViewById(R.id.tv_confirm);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd=editText.getText().toString();
                if (TextUtils.isEmpty(checkPwd)){
                    editText.setHint("请输入密码");
                }else{
                    if (listener!=null){
                        listener.doConfirm(checkPwd);
                    }
                }

            }
        });
    }

    ConfirmPwdClickListener listener;
    public void setListener(ConfirmPwdClickListener listener) {
        this.listener = listener;
    }
}
