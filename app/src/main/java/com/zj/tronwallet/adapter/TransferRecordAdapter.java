package com.zj.tronwallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.zj.tronwallet.databinding.ItemTransferRecordBinding;

import org.tron.api.GrpcAPI;

/**
 * create by zj on 2019/1/10
 */
public class TransferRecordAdapter extends AbsRVAdapter<GrpcAPI.TransactionExtention, AbsRVAdapter.BindingViewHolder> {

    private Context context;
    private int type;
    public TransferRecordAdapter(Context context,int type) {
        super(context);
        this.type=type;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemTransferRecordBinding binding=ItemTransferRecordBinding.inflate(mInflater,viewGroup,false);

        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder bindingViewHolder, int i) {
        ItemTransferRecordBinding binding= (ItemTransferRecordBinding) bindingViewHolder.mBinding;
        GrpcAPI.TransactionExtention bean= (GrpcAPI.TransactionExtention) getItem(i);
        binding.setData(bean);
        binding.setPos(i);

        if (bean!=null){

        }



    }
}
