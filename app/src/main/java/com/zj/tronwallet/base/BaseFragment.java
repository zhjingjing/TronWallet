package com.zj.tronwallet.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;
import java.lang.reflect.Field;

/**
 * fragment基类
 *
 * @author Created by jiang on 2017/5/12.
 */

public class BaseFragment extends Fragment {
    private Handler mHandler;
    private boolean mIsActivityCreated;
    private boolean mIsFirstVisible = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsActivityCreated = true;

        if (getUserVisibleHint() && mIsFirstVisible) {
            mIsFirstVisible = false;
            onFirstVisibleToUser();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mIsActivityCreated && isVisibleToUser && mIsFirstVisible) {
            mIsFirstVisible = false;
            onFirstVisibleToUser();
        }
    }

    protected void onFirstVisibleToUser() {

    }

    protected void showToast(String msg) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }

    protected void showToast(int resId) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
        }
    }


    public String getTitle() {
        return "";
    }


//    模拟器运行时出现java.lang.IllegalStateException: No activity
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
