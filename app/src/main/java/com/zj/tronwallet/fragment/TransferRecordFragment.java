package com.zj.tronwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zj.tronwallet.adapter.TransferRecordAdapter;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.FragmentTransferRecordBinding;

import org.tron.api.GrpcAPI;
import org.tron.protos.Protocol;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zj on 2019/1/10
 */
public class TransferRecordFragment extends BaseFragment {

    private FragmentTransferRecordBinding binding;

    private int type;//0: 发送 1：接收
    private List<GrpcAPI.TransactionExtention> mTransactions;
    private Wallet mWallet;

    private TransferRecordAdapter adapter;
    public static TransferRecordFragment getInstance(int type){
        TransferRecordFragment fragment=new TransferRecordFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding==null){
            binding=FragmentTransferRecordBinding.inflate(inflater,container,false);
            binding.setPresenter(this);
        }
        Bundle bundle=getArguments();
        if (bundle!=null){
            type=bundle.getInt("type",0);

            mWallet = WalletManager.getSelectedWallet(getActivity());
            mTransactions = new ArrayList<>();
            adapter=new TransferRecordAdapter(getActivity(),type);
            binding.recycleRecord.setHasFixedSize(true);
            binding.recycleRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recycleRecord.setAdapter(adapter);
            getList();
        }
        return binding.getRoot();
    }


    @Override
    public String getTitle() {
        type=getArguments().getInt("type",0);
        if (type==0){
            return "发送";
        }else{
            return "接收";
        }
    }


    public void getList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GrpcAPI.TransactionListExtention transactionsExtention = null;
                try {
                    if (type==1) {
                        transactionsExtention = WalletManager.getTransactionsFromThis2(WalletManager.decodeFromBase58Check(mWallet.getAddress()), 0, 1000);
                    } else {
                        transactionsExtention = WalletManager.getTransactionsToThis2(WalletManager.decodeFromBase58Check(mWallet.getAddress()), 0, 1000);
                    }
                    mTransactions.clear();
                    List<GrpcAPI.TransactionExtention> t = transactionsExtention.getTransactionList();
                    mTransactions.addAll(t);
                    Log.e("xxxx",mTransactions.size()+"");
                    adapter.setData(mTransactions);

                } catch (Exception ignore) {
                    Log.e("xxxx",ignore.getMessage());

                }


            }
        }).start();


    }
}
