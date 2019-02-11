package com.zj.tronwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.FragmentAccountEthBinding;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import eth.org.services.WalletClient;

/**
 * create by zj on 2019/1/15
 */
public class EthAccountFragment extends BaseFragment {

    private FragmentAccountEthBinding binding;
    public static EthAccountFragment getInstance(){
        return new EthAccountFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding==null){
            binding=FragmentAccountEthBinding.inflate(inflater,container,false);
            binding.setPresenter(this);
        }

        Web3j web3j= WalletClient.build();




//        BigInteger balance = web3j.ethGetBalance(mAddress, DefaultBlockParameterName.LATEST).send().getBalance();


        return binding.getRoot();
    }


    @Override
    public String getTitle() {
        return "账户";
    }
}
