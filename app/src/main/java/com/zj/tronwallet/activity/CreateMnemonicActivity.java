package com.zj.tronwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityCreateMnemonicBinding;

import java.util.ArrayList;
import java.util.List;

import eth.org.utils.WalletUtil;

public class CreateMnemonicActivity extends AppCompatActivity {

    private ActivityCreateMnemonicBinding binding;
    private List<String> list;
    private String mnemonic;
    public static void launch(Activity context, int requestCode){
        Intent intent=new Intent(context,CreateMnemonicActivity.class);
        context.startActivityForResult(intent,requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_create_mnemonic);
        binding.setPresenter(this);

        mnemonic =   WalletUtil.generateMnemonics();

        if (mnemonic!=null){
            String[] strings=mnemonic.split(" ");
            list=new ArrayList<>();

            for (int i=0;i<strings.length;i++){
                list.add(strings[i]);
            }

            binding.flowLayout.setAdapter(new TagAdapter<String>(list) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv=  (TextView) LayoutInflater.from(CreateMnemonicActivity.this).inflate(R.layout.item_textview,
                            parent, false);
                    tv.setText(s);
                    return tv;
                }
            });
        }
        Log.e("xxx",mnemonic);
    }


    public void onLeftClicked(View view){
        finish();
    }


    public void onNextClicked(){
        if (list!=null){
            MnemonicCheckActivity.launch(this,list,1101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 1101:
                    Intent intent=new Intent();
                    intent.putExtra("mnemonic",mnemonic);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
            }
        }
    }
}
