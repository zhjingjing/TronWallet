package com.zj.tronwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityMnemonicCheckBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MnemonicCheckActivity extends AppCompatActivity {

    private ActivityMnemonicCheckBinding binding;
    private List<String> resource;
    private List<String> randomList;
    private List<String> resultList;
    public static void launch(Activity activity, List<String> list, int requestCode){

        Intent intent=new Intent(activity,MnemonicCheckActivity.class);
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("list", (ArrayList<String>) list);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_mnemonic_check);
        binding.setPresenter(this);


        Bundle bundle=getIntent().getExtras();
        if (bundle==null){
            finish();
        }
        resource=bundle.getStringArrayList("list");
        randomList=new ArrayList<>();


        if (resource==null){
            finish();
        }else{
            randomList.addAll(resource);
            int size = randomList.size();
            Random random = new Random();
            for (int i=0;i<size;i++){
                int randomPos = random.nextInt(size);

                String  temp = randomList.get(i);
                randomList.set(i, randomList.get(randomPos));
                randomList.set(randomPos, temp);
            }

        }

        resultList=new ArrayList<>();
        binding.flowLayout.setAdapter(new TagAdapter<String>(randomList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv=  (TextView) LayoutInflater.from(MnemonicCheckActivity.this).inflate(R.layout.item_textview,
                        parent, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                view.setBackgroundColor(Color.parseColor("#00ff00"));
                resultList.add(randomList.get(position));
                resetResult();
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                view.setBackgroundColor(Color.parseColor("#EAEAEA"));
                for (int i=0;i<resultList.size();i++){
                    if (resultList.get(i).equals(randomList.get(position))){
                        resultList.remove(i);
                    }
                }
                resetResult();
            }
        });

        resetResult();

    }

    public void resetResult(){
        binding.flowLayoutResult.setAdapter( new TagAdapter<String>(resultList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv=  (TextView) LayoutInflater.from(MnemonicCheckActivity.this).inflate(R.layout.item_textview,
                        parent, false);
                tv.setText(s);
                return tv;
            }

        });
    }


    public void onLeftClicked(View view){
        finish();
    }


    public void onCheckMnemonic(){
        if (resultList.size()!=resource.size()){
            Toast.makeText(this,"助记词无效",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isOk=true;
        int size=resultList.size();
        for (int i=0;i<size;i++){
            if (!resultList.get(i).equals(resource.get(i))){
                isOk=false;
            }
        }

        if (isOk){
            Intent intent=new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }else{
            Toast.makeText(this,"助记词无效",Toast.LENGTH_SHORT).show();
        }
    }


}
