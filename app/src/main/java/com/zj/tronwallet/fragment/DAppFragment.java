package com.zj.tronwallet.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.tronwallet.activity.AccountActivity;
import com.zj.tronwallet.activity.WebActivity;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.FragmentDappBinding;

import org.tron.walletserver.WalletManager;

/**
 * create by zj on 2019/1/8
 */
public class DAppFragment extends BaseFragment {


    private FragmentDappBinding binding;
    public static DAppFragment getInstance(){
        return new DAppFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding==null){
            binding=FragmentDappBinding.inflate(inflater,container,false);
            binding.setPresenter(this);
        }
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "应用";
    }

    //trongame地址
    public void onTronGameClicked(){
        final EditText editText=new EditText(getActivity());
        editText.setHint("密码");
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("请输入密码").setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (WalletManager.checkPassword(getActivity(),WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString())){
//                            WebActivity.launch(getActivity(),"http://192.168.160.236","Trongame",WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString());
                            WebActivity.launch(getActivity(),"http://h5.trongame.fun","Trongame",WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString());
                        }else{
                            Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }


    public void onTronTestClicked(){
        final EditText editText=new EditText(getActivity());
        editText.setHint("密码");
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("请输入密码").setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (WalletManager.checkPassword(getActivity(),WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString())){
                            WebActivity.launch(getActivity(),"http://192.168.160.236","TronTest",WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString());
                        }else{
                            Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }
}
