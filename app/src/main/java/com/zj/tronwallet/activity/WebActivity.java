package com.zj.tronwallet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import com.zj.tronwallet.R;
import com.zj.tronwallet.databinding.ActivityWebBinding;
import com.zj.tronwallet.utils.WebVieWidget;

import org.tron.common.crypto.ECKey;
import org.tron.common.utils.ByteArray;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.io.IOException;
import java.io.InputStream;

public class WebActivity extends AppCompatActivity {

    private ActivityWebBinding binding;
    private String mUrl;
    private String mTitle;
    private Wallet mWallet;

    public static void launch(Context context,String url,String title,String walletName,String pwd){
        Intent intent=new Intent(context,WebActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("name",walletName);
        bundle.putString("pwd",pwd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_web);
        binding.setPresenter(this);

        Bundle bundle=getIntent().getExtras();
        if (bundle==null){
            finish();
        }

        mUrl=  bundle.getString("url","http://h5.trongame.fun/#/");
        mTitle=bundle.getString("title","应用");

       String  walletName=bundle.getString("name","");
        String pwd=bundle.getString("pwd","");

        mWallet = WalletManager.getWallet(this,walletName, pwd);

        ECKey ecKey=mWallet.getECKey();

        final String   privateKey= ByteArray.toHexString(ecKey.getPrivKeyBytes());
        binding.webWidget.setWebLoadListener(new WebVieWidget.SimpleLoadListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//                mBinding.titleBar.setTitleText(title);
            }

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageFinished(WebView view, String url) {
                mUrl = url;
                injectScriptFile(view,"TronWeb.js");

//                view.loadUrl("javascript:  try{function myFunction()" +
//                    "{" +
//                     "if(!window.tronWeb){"+
//                    " var HttpProvider = TronWeb.providers.HttpProvider;\n" +
//                    " var fullNode = new HttpProvider('https://api.trongrid.io');\n" +
//                    " var solidityNode = new HttpProvider('https://api.trongrid.io');\n" +
//                    " var eventServer  = new HttpProvider('https://api.trongrid.io');\n" +
//                     "console.log(TronWeb.address.fromPrivateKey(\""+privateKey+"\"));"+
//                    " var tronWeb = new TronWeb(" +
//                    " fullNode," +
//                    " solidityNode," +
//                    " eventServer" +
//                    " );" +
//                    "tronWeb.setAddress(TronWeb.address.fromPrivateKey(\""+privateKey+"\"));"+
//                     "console.log(tronWeb.trx.getContract('TMNL2oxE9Qz7FctDuQcnaCbKoEaQfdgRnA')+'----'+tronWeb.ready);"+
//                    "tronWeb.trx.sign.bind(TronWeb);"+
//                    "tronWeb.setDefaultBlock('latest');"+
//                    "tronWeb.ready=true;"+
//                    "window.tronWeb = tronWeb;" +
//                    "console.log(window.tronWeb.defaultAddress.base58+'----'+tronWeb.ready);"+
//                    "console.log(window.tronWeb.defaultPrivateKey+'----'+tronWeb.ready);"+
//                    "console.log(window.tronWeb.trx.getBalance(window.tronWeb.defaultAddress.base58)+'----'+tronWeb.ready);"+
//                     "};"+
//                    "};" +
//                    "myFunction();}catch(err){" +
//                    "console.log('插入方法报错啦+'+err);" +
//                     "};"
//            );
                view.loadUrl("javascript:  try{function myFunction()" +
                    "{" +
                     "if(!window.tronWeb){"+
                    " var HttpProvider = TronWeb.providers.HttpProvider;" +
                    " var fullNode = new HttpProvider('https://api.trongrid.io');" +
                    " var solidityNode = new HttpProvider('https://api.trongrid.io');" +
                     "var eventServer = 'https://api.trongrid.io/';"+
                    " var tronWeb = new TronWeb(" +
                    " fullNode," +
                    " solidityNode," +
                    " eventServer," +
                     "'"+privateKey+"'"+
                    " );" +
//                    "tronWeb.setAddress(TronWeb.address.fromPrivateKey(\""+privateKey+"\"));"+
//                    "tronWeb.setPrivateKey('"+privateKey+"');"+
//                    "tronWeb.defaultPrivateKey=false;"+
//                    "tronWeb.setDefaultBlock('latest');"+
//                    "tronWeb.contract().address='TX9WvWkhCEF3J35HnvcenK3C5XrABCipfF';"+
//                    "tronWeb.isConnected();"+
                    "tronWeb.ready=true;"+
                    "window.tronWeb = tronWeb;" +
                    "console.log(window.tronWeb.defaultAddress.base58+'----'+tronWeb.ready);"+
                    "console.log(window.tronWeb.defaultPrivateKey+'----'+tronWeb.ready);"+
                    "console.log(window.tronWeb.trx.getBalance(window.tronWeb.defaultAddress.base58)+'----'+tronWeb.ready);"+
                     "};"+
                    "};" +
                    "myFunction();" +
                     "}catch(err){" +
                    "console.log('插入方法报错啦+'+err);" +
                     "};"
            );
            }
        });
        binding.titleBar.setTitleText(mTitle);
        binding.webWidget.loadUrl(mUrl,this);
        binding.titleBar.setLeftBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
