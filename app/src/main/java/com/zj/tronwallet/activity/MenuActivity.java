package com.zj.tronwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.zj.tronwallet.R;
import com.zj.tronwallet.adapter.NormalFragmentAdapter;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.ActivityMenuBinding;
import com.zj.tronwallet.fragment.AccountFragment;
import com.zj.tronwallet.fragment.DAppFragment;
import com.zj.tronwallet.fragment.EthAccountFragment;
import com.zj.tronwallet.fragment.SettingFragment;

import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

public class MenuActivity extends Activity {
    private ActivityMenuBinding binding;
    private Wallet wallet;
    private String type="tron";
    public static void launch(Context context){
        Intent intent=new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    private BaseFragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_menu);
        binding.setPresenter(this);
//        SharedPreferences sharedPreferences = getSharedPreferences(getString(org.tron.R.string.preference_file_key_type), Context.MODE_PRIVATE);
//        type = sharedPreferences.getString("current_wallet_type","");
        wallet= WalletManager.getSelectedWallet(this);
        WalletManager.initGRPC(this);
//        if (type.equals("tron")){
//            wallet= WalletManager.getSelectedWallet(this);
//            WalletManager.initGRPC(this);
//        }else if (type.equals("eth")){
//
//        }else if (type.equals("eos")){
//
//        }

        init();
    }

    public void init(){
        fragments=new BaseFragment[3];
        if (type.equals("eth")){
            fragments[0]= EthAccountFragment.getInstance();
        }else if (type.equals("eos")){
            fragments[0]= AccountFragment.getInstance();
        }else {
            fragments[0]= AccountFragment.getInstance();
        }
        fragments[1]= SettingFragment.getInstance();
        fragments[2]= DAppFragment.getInstance();
        binding.mainTab.setupWithViewPager(binding.mainViewpager);
        NormalFragmentAdapter adapter = new NormalFragmentAdapter(getFragmentManager(), fragments);
        binding.mainViewpager.setAdapter(adapter);
        binding.mainViewpager.setOffscreenPageLimit(5);
    }

}
