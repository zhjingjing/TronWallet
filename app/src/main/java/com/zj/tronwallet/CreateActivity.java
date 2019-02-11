package com.zj.tronwallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.tronwallet.activity.CreateMnemonicActivity;
import com.zj.tronwallet.activity.ImporWalletActivity;
import com.zj.tronwallet.activity.MenuActivity;
import com.zj.tronwallet.databinding.ActivityMainBinding;
import org.tron.walletserver.DuplicateNameException;
import org.tron.walletserver.InvalidNameException;
import org.tron.walletserver.InvalidPasswordException;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import eth.org.services.WalletClient;
import eth.org.utils.WalletUtil;

public class CreateActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int type=0;//0：tron 1：eth 2：eos
    public static void launch(Context context,int type){
        Intent intent=new Intent(context,CreateActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_main);
        binding.setPresenter(this);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            type=bundle.getInt("type",0);
        }
//        SharedPreferences sharedPreferences = getSharedPreferences(getString(org.tron.R.string.preference_file_key_type), Context.MODE_PRIVATE);
//       String type = sharedPreferences.getString("current_wallet_type","");
           if(WalletManager.existAnyWallet(this))
           {
               MenuActivity.launch(this);
               finish();
               return;
           }

    }

    //创建钱包
    public void onCreateWallet(){
        final String name=binding.editCreateName.getText().toString();
        final String pwd=binding.editCreatePwd.getText().toString();

        if (TextUtils.isEmpty(name)){
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("无效的用户名")
                    .setMessage("请输入一个有效的用户名")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

            return;

        }

        if (TextUtils.isEmpty(pwd)||pwd.length()<6){
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("无效的密码")
                    .setMessage("请输入一个至少6位密码")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();


            return;
        }


        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("创建钱包").setMessage("您即将使用输入的密码创建钱包")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final EditText confirmPwd=new EditText(CreateActivity.this);
                        AlertDialog.Builder confirmDialog=new AlertDialog.Builder(CreateActivity.this);
                        confirmDialog.setMessage("确认密码").setView(confirmPwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface pwdDialog, int which) {
                                pwdDialog.dismiss();
                                if (pwd.equals(confirmPwd.getText().toString())){
                                    createWallet(name,pwd);
                                }else{
                                    Toast.makeText(CreateActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface pwdDialog, int which) {
                                pwdDialog.dismiss();
                            }
                        }).show();


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //导入钱包
    public void onImportWallet(){

        ImporWalletActivity.launch(this,1101);
    }

    public void createWallet(String name,String pwd){

        if (type==0){
//            SharedPreferences sharedPreferences = getSharedPreferences(getString(org.tron.R.string.preference_file_key_type), Context.MODE_PRIVATE);
//            SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
//            sharedEditor.putString("current_wallet_type","tron");
//            sharedEditor.commit();

            Wallet wallet = new Wallet(true);
            wallet.setWalletName(name);
            try {
                WalletManager.store(this,wallet, pwd);
                WalletManager.selectWallet(this,name);
                MenuActivity.launch(this);
                finish();
            } catch (DuplicateNameException e) {
                e.printStackTrace();
            } catch (InvalidPasswordException e) {
                e.printStackTrace();
            } catch (InvalidNameException e) {
                e.printStackTrace();
            }
        }else if (type==1){

            CreateMnemonicActivity.launch(this,1201);

        }else if (type==2){
//            SharedPreferences sharedPreferences = getSharedPreferences(getString(org.tron.R.string.preference_file_key_type), Context.MODE_PRIVATE);
//            SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
//            sharedEditor.putString("current_wallet_type","eos");
//            sharedEditor.commit();
              Toast.makeText(this,"EOS开发中。。。",Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1101){


                MenuActivity.launch(this);
                finish();
            }else if (requestCode==1201){
                String s=data.getStringExtra("mnemonic");

                String pwd=binding.editCreatePwd.getText().toString();
                byte[] seed= WalletUtil.getSeed(s);
                ECKeyPair ecKeyPair=WalletUtil.getECKeyPair(seed);
                WalletFile walletFile=null;
                try {
                    WalletUtils.generateWalletFile(pwd,ecKeyPair,getFilesDir(),false);
                    walletFile=WalletUtil.createLight(pwd,ecKeyPair);
                } catch (CipherException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (walletFile!=null){
//                        SharedPreferences sharedPreferences = getSharedPreferences(getString(org.tron.R.string.preference_file_key_type), Context.MODE_PRIVATE);
//                        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
//                        sharedEditor.putString("current_wallet_type","eth");
//                        sharedEditor.commit();
//
//
//                        SharedPreferences walletPreferences = getSharedPreferences("eth_"+binding.editCreateName.getText().toString(), Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = walletPreferences.edit();
//                        editor.putString("pwd",pwd);
//                        editor.putString("address",walletFile.getAddress());
//                        editor.commit();
//
//                        MenuActivity.launch(this);
//                        finish();
                    }

                }

            }
        }
    }
}
