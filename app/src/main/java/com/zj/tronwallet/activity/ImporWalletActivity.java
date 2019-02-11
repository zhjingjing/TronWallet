package com.zj.tronwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityImporWalletBinding;

import org.tron.walletserver.DuplicateNameException;
import org.tron.walletserver.InvalidNameException;
import org.tron.walletserver.InvalidPasswordException;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

public class ImporWalletActivity extends AppCompatActivity {

    private ActivityImporWalletBinding binding;
    public static void launch(Activity activity,int requestCode){
        Intent intent=new Intent(activity,ImporWalletActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_impor_wallet);
        binding.setPresenter(this);
    }


    public void onLeftClicked(View view){
        finish();
    }


    //导入钱包
    public void onImportWalletClicked(){

        String name=binding.editWalletName.getText().toString();
        String pwd=binding.editWalletPwd.getText().toString();
        String pwdConfirm=binding.editWalletPwdConfirm.getText().toString();
        String privateKey=binding.editPrivateKey.getText().toString();

        if (TextUtils.isEmpty(privateKey)){
            Toast.makeText(this,"请输入私钥",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"请输入钱包名称",Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(pwdConfirm)){
            Toast.makeText(this,"请确认密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwd.equals(pwdConfirm)){
            Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }


        Wallet wallet = new Wallet(privateKey);
        wallet.setWalletName(name);
        try {
            WalletManager.store(this,wallet, pwd);
            WalletManager.selectWallet(this,name);

            Intent intent=new Intent();
            setResult(RESULT_OK,intent);
            finish();
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }



    }
}
