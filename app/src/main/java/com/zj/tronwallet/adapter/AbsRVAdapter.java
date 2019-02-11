package com.zj.tronwallet.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView}适配器基类
 *
 * @author Created by jiangyujiang on 2017/7/31.
 */

public abstract class AbsRVAdapter<E, T extends AbsRVAdapter.BindingViewHolder> extends RecyclerView.Adapter<T> {
    private List<E> mDataList;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public AbsRVAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? getHeaderItemCount() : getHeaderItemCount() + mDataList.size();
    }

    public int getHeaderItemCount() {
        // 在列表顶部不包含在数据列表中的Item数量，并非ListView的Header数
        // 头部Item数量+dataList数量等于总的Item数量，子类必须返回正确的头部Item数量来保证映射数据列表正确
        return 0;
    }

    public Object getItem(int position) {
        if (position < getHeaderItemCount()) {
            return getHeaderItem(position);
        } else {
            return mDataList == null ? null : mDataList.get(position - getHeaderItemCount());
        }
    }

    public Object getHeaderItem(int position) {
        return null;
    }

    /**
     * 列表Item的位置获取对应数据列表的索引，如果找不到将返回-1
     *
     * @param position item位置
     * @return item对应的数据项位置
     */
    public int getDataIndex(int position) {
        if (position < getHeaderItemCount()) {
            return -1;
        }
        return position - getHeaderItemCount();
    }

    public int getDataCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addData(List<E> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        int addCount = data.size();
        if (mDataList == null) {
            mDataList = new ArrayList<>(addCount);
        }
        int startPos = getItemCount();
        mDataList.addAll(data);
        notifyItemRangeInserted(startPos, addCount);
    }

    public void addDataToStart(E data) {
        if (data == null) {
            return;
        }
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(0, data);
        notifyItemInserted(0);
    }

    public void addDataToTail(E data) {
        if (data == null) {
            return;
        }
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        int pos = getItemCount();
        mDataList.add(data);
        notifyItemInserted(pos);
    }

    public void clearData() {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    public void setData(List<E> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    public List<E> getDataList() {
        return mDataList;
    }

    public void addData(int index, E data) {
        if (data == null) {
            return;
        }
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(index, data);
        notifyItemInserted(getHeaderItemCount() + index);
    }

    public void setDataIndex(int index,E data){
        if (data==null){
            return;
        }
        if (mDataList==null){
            mDataList = new ArrayList<>();
        }
        mDataList.set(index,data);
        notifyItemChanged(getHeaderItemCount() + index);
    }

    public void removeData(int index) {
        if (mDataList == null) {
            return;
        }
        mDataList.remove(index);
        notifyItemRemoved(getHeaderItemCount() + index);
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder {
        public ViewDataBinding mBinding;

        public BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
