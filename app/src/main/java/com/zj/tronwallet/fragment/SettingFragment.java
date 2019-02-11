package com.zj.tronwallet.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.tronwallet.CreateActivity;
import com.zj.tronwallet.R;
import com.zj.tronwallet.activity.AccountActivity;
import com.zj.tronwallet.activity.WalletSelectActivity;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.FragmentSettingBinding;

import org.tron.walletserver.WalletManager;

import java.util.HashSet;
import java.util.Set;

/**
 * create by zj on 2019/1/7
 */
public class SettingFragment extends BaseFragment {

    private FragmentSettingBinding binding;
    public static SettingFragment getInstance(){
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       if (binding==null){
           binding=FragmentSettingBinding.inflate(inflater,container,false);
           binding.setPresenter(this);
       }
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "设置";
    }

    //切换节点
    public void onChangeNodeClicked(){

    }


    //私密账户信息
    public void onPrivateInfoClicked(){
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
                            AccountActivity.launch(getActivity(),WalletManager.getSelectedWallet(getActivity()).getWalletName(),editText.getText().toString());
                        }else{
                            Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();

    }

    //删除当前钱包
    public void onDelWalletClicked(){

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("确认删除当前钱包吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reset();
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


    public void reset(){
        String selectedWallet = WalletManager.getSelectedWallet(getActivity()).getWalletName();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(selectedWallet, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();
        Set<String> wallets = new HashSet<>(preferences.getStringSet(getString(R.string.wallets_key), new HashSet<String>()));
        wallets.remove(selectedWallet);
        editor.putStringSet(getString(R.string.wallets_key), wallets);


        if(!wallets.isEmpty())
            editor.putString(getString(R.string.selected_wallet_key), wallets.iterator().next());
        editor.commit();

        Intent intent = new Intent(getActivity(), CreateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
    }


    public void onAddWalletClicked(){
        WalletSelectActivity.launch(getActivity());

    }
}
