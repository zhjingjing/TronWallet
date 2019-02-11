package com.zj.tronwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zj.tronwallet.R;
import com.zj.tronwallet.adapter.NormalFragmentAdapter;
import com.zj.tronwallet.base.BaseFragment;
import com.zj.tronwallet.databinding.ActivityTransferRecordBinding;
import com.zj.tronwallet.fragment.TransferRecordFragment;

public class TransferRecordActivity extends AppCompatActivity {

    private ActivityTransferRecordBinding binding;
    private BaseFragment[] fragments;
    public static void launch(Context context){
        Intent intent=new Intent(context,TransferRecordActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_transfer_record);
        binding.setPresenter(this);

        initView();



    }

    public void initView(){
        fragments=new BaseFragment[2];
        fragments[0]= TransferRecordFragment.getInstance(0);
        fragments[1]=TransferRecordFragment.getInstance(1);

        binding.tabRecord.setupWithViewPager(binding.recordViewpager);
        NormalFragmentAdapter adapter = new NormalFragmentAdapter(getFragmentManager(), fragments);
        binding.recordViewpager.setAdapter(adapter);
        binding.recordViewpager.setOffscreenPageLimit(5);
    }

    //close
    public void onLeftClicked(View view){
        finish();
    }
}
