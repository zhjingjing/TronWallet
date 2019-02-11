package com.zj.tronwallet.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zj.tronwallet.activity.TransferActivity;
import com.zj.tronwallet.activity.TransferRecordActivity;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.FragmentAccountBinding;
import com.zj.tronwallet.utils.TronUtil;

import org.tron.api.GrpcAPI;
import org.tron.common.utils.Utils;
import org.tron.protos.Protocol;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * create by zj on 2019/1/7
 */
public class AccountFragment extends BaseFragment {

    private FragmentAccountBinding binding;
    private Wallet wallet;
    private Protocol.Account mAccount;
    private boolean isLoad;

    //获取数据
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                if (wallet!=null){
                    mAccount= TronUtil.getAccount(getActivity(),wallet.getWalletName());
                    initView();
                    Log.e("xxx","xx+update+1");
                }

            }

        }
    };
    public static AccountFragment getInstance(){
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding==null){
            binding=FragmentAccountBinding.inflate(inflater,container,false);
            binding.setPresenter(this);
        }

        wallet= WalletManager.getSelectedWallet(getActivity());
        if (wallet!=null){
            binding.tvWalletName.setText(wallet.getWalletName());
            binding.tvWalletAddress.setText(wallet.getAddress());
            mAccount=TronUtil.getAccount(getActivity(),wallet.getWalletName());
            initView();
            initTimer();
        }
        return binding.getRoot();
    }


    public void initView(){
        if (mAccount!=null){
            double balance=mAccount.getBalance()/1000000.0d;
            binding.tvWalletAccount.setText(balance+"");

            long frozenTotal = 0;
            for(Protocol.Account.Frozen frozen : mAccount.getFrozenList()) {
                frozenTotal += frozen.getFrozenBalance();
            }
            binding.tvWalletTronWeight.setText((frozenTotal/1000000)+"");

            GrpcAPI.AccountNetMessage accountNetMessage = TronUtil.getAccountNet(getActivity(), wallet.getWalletName());

            long bandwidth = accountNetMessage.getNetLimit() + accountNetMessage.getFreeNetLimit();
            long bandwidthUsed = accountNetMessage.getNetUsed()+accountNetMessage.getFreeNetUsed();
            long bandwidthLeft = bandwidth - bandwidthUsed;

            binding.tvWalletBandwidth.setText(bandwidthLeft+"");

        }
    }

    public void initTimer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isLoad){
                    try {
                        Thread.sleep(1000);
                        updateAccount();
                        Log.e("xxx","update+1");
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }



    @Override
    public String getTitle() {
        return "账户";
    }

    //转账
    public void onSendTRX(){
        TransferActivity.launch(getActivity());
    }


    //转账记录
    public void onTransferRecord(){
        TransferRecordActivity.launch(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        isLoad=false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isLoad=true;
            initTimer();
        }else{
            isLoad=false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isLoad=true;
        initTimer();
    }

    public void  updateAccount(){
        if (wallet!=null){
            byte[] address = WalletManager.decodeFromBase58Check(wallet.getAddress());
            if (address!=null&&address.length>0){
                Protocol.Account account = WalletManager.queryAccount(address, false);
                GrpcAPI.AccountNetMessage accountNetMessage = WalletManager.getAccountNet(address);
                GrpcAPI.AccountResourceMessage accountResMessage = WalletManager.getAccountRes(address);

                TronUtil.saveAccount(getActivity(), wallet.getWalletName(), account);
                TronUtil.saveAccountNet(getActivity(), wallet.getWalletName(), accountNetMessage);
                TronUtil.saveAccountRes(getActivity(), wallet.getWalletName(), accountResMessage);
            }
        }
    }
}
