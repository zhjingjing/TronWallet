package com.zj.tronwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityWalletSelectBinding;

public class WalletSelectActivity extends AppCompatActivity {

    private ActivityWalletSelectBinding binding;

    public static void launch(Context context){
        Intent intent=new Intent(context,WalletSelectActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_wallet_select);
        binding.setPresenter(this);
    }


    public void onLeftClicked(View view){
        finish();
    }

    //创建eth钱包
    public void onCreateETH(){

    }
    //创建eos钱包
    public void onCreateEOS(){

    }
    //创建tron钱包
    public void onCreateTRON(){

    }
}
