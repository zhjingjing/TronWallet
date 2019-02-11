package com.zj.tronwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zj.tronwallet.R;
import com.zj.tronwallet.bip39.BIP39;
import com.zj.tronwallet.bip39.ValidationException;
import com.zj.tronwallet.databinding.ActivityAccountBinding;

import org.tron.common.utils.ByteArray;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

public class AccountActivity extends AppCompatActivity {
    private ActivityAccountBinding binding;
    private String walletName;
    private String pwd;
    private Wallet mWallet;
    private String mAddress;
    private String privateKey;
    private String mnemonic;

    public static void  launch(Context context,String walletName,String pwd){
        Intent intent=new Intent(context,AccountActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("name",walletName);
        bundle.putString("pwd",pwd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_account);
        binding.setPresenter(this);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            walletName=bundle.getString("name","");
            pwd=bundle.getString("pwd","");

            mWallet = WalletManager.getWallet(this,walletName, pwd);

            if(mWallet == null || !mWallet.isOpen()) {
                finish();
                return;
            }

            mAddress=mWallet.getAddress();
            binding.tvWalletAddress.setText(mAddress);

            privateKey= ByteArray.toHexString(mWallet.getECKey().getPrivKeyBytes());
            binding.tvWalletPrivate.setText(privateKey);

            try {
                mnemonic= BIP39.encode(mWallet.getECKey().getPrivKeyBytes(), "pass");
                binding.tvWalletMnemonic.setText(mnemonic);
            } catch (ValidationException e) {
                e.printStackTrace();
            }

        }
    }


    //fini
    public void onBackClicked(){
        finish();
    }


}
