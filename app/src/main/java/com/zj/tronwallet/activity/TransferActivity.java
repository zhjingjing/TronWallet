package com.zj.tronwallet.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityTransferBinding;
import com.zj.tronwallet.utils.InputFilterMinMax;
import com.zj.tronwallet.utils.TronUtil;

import org.tron.api.GrpcAPI;
import org.tron.common.utils.TransactionUtils;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

public class TransferActivity extends AppCompatActivity {

    private ActivityTransferBinding binding;
    private Wallet mWallet;

    public static void launch(Context context){
        Intent intent=new Intent(context,TransferActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_transfer);
        binding.setPresenter(this);

        mWallet = WalletManager.getSelectedWallet(this);

        if (mWallet==null){
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAmount();
    }

    public void updateAmount(){
        if(mWallet != null) {
            Protocol.Account account = TronUtil.getAccount(this, mWallet.getWalletName());

            double assetAmount = account.getBalance() / 1000000.0d;
            binding.tvAvailableAmount.setText("最大数额："+assetAmount+" TRX");

            binding.editAmount.setFilters(new InputFilter[]{new InputFilterMinMax(0, assetAmount)});
        }
    }


    //返回
    public void onBackClicked(){
        finish();
    }


    private Protocol.Transaction mTransactionUnsigned;
    private Protocol.Transaction mTransactionSigned;
    public void onSendClicked(){
        String sendAddress= binding.editAddress.getText().toString();
        String sendAmount=binding.editAmount.getText().toString();


        if (TextUtils.isEmpty(sendAddress)){
            Toast.makeText(this,"请输入转账地址",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sendAmount)){
            Toast.makeText(this,"请输入转账金额",Toast.LENGTH_SHORT).show();
            return;
        }

        if (mWallet.getAddress().equals(sendAddress)){
            Toast.makeText(this,"您不能发送到自己的地址",Toast.LENGTH_SHORT).show();
            return;
        }


        byte[] toRaw;
        try {
            toRaw = WalletManager.decodeFromBase58Check(sendAddress);
        } catch (IllegalArgumentException ignored) {
            Toast.makeText(this,"请输入一个有效地址",Toast.LENGTH_SHORT).show();
            return;
        }

        double amount=Double.parseDouble(sendAmount);
        Contract.TransferContract contract = WalletManager.createTransferContract(toRaw, WalletManager.decodeFromBase58Check(mWallet.getAddress()), (long) (amount * 1000000.0d));
        Protocol.Transaction transaction = WalletManager.createTransaction4Transfer(contract);
        byte[]  mTransactionBytes = transaction.toByteArray();
        try {
            mTransactionUnsigned = Protocol.Transaction.parseFrom(mTransactionBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        final EditText editText=new EditText(this);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("请输入密码")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (WalletManager.checkPassword(TransferActivity.this,mWallet.getWalletName(),editText.getText().toString())){
                            if(mWallet.open(editText.getText().toString())) {
                                mTransactionSigned = TransactionUtils.setTimestamp(mTransactionUnsigned);
                                mTransactionSigned = TransactionUtils.sign(mTransactionSigned, mWallet.getECKey());
                                WalletManager.broadcastTransaction(mTransactionSigned);
                                updateAccount();
                                updateAmount();

                                Toast.makeText(TransferActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(TransferActivity.this,"不能打开钱包",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(TransferActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }




                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void  updateAccount(){
        if (mWallet!=null){
            byte[] address = WalletManager.decodeFromBase58Check(mWallet.getAddress());
            Protocol.Account account = WalletManager.queryAccount(address, false);
            GrpcAPI.AccountNetMessage accountNetMessage = WalletManager.getAccountNet(address);
            GrpcAPI.AccountResourceMessage accountResMessage = WalletManager.getAccountRes(address);

            TronUtil.saveAccount(this, mWallet.getWalletName(), account);
            TronUtil.saveAccountNet(this, mWallet.getWalletName(), accountNetMessage);
            TronUtil.saveAccountRes(this, mWallet.getWalletName(), accountResMessage);
        }
    }

}
