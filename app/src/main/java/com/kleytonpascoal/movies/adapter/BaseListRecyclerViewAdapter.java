package com.kleytonpascoal.movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleyton on 12/05/17.
 */

public abstract class BaseListRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected Context mContext;

    public interface OnItemClickAtPositionListener<T> {
        void onClickAtPosition(View view, int position, T item);
    }

    public BaseListRecyclerViewAdapter() {
        mValues = new ArrayList<>(0);
    }

    protected final ArrayList<T> mValues;
    protected OnItemClickAtPositionListener<T> mOnItemClickAtPositionListener;

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(getViewHolderLayoutResId(), parent, false);

        final VH viewHolder = onCreateCustomViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mOnItemClickAtPositionListener != null) {
                    mOnItemClickAtPositionListener.onClickAtPosition(v, adapterPosition, mValues.get(adapterPosition));
                }
            }
        });

        return viewHolder;
    }

    protected abstract VH onCreateCustomViewHolder(View view);

    protected abstract int getViewHolderLayoutResId();

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAll(List<T> items) {
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public void restore() {
        mValues.clear();
        notifyDataSetChanged();
    }

    public void setItemClickAtPositionListener(OnItemClickAtPositionListener<T> onItemClickAtPositionListener) {
        mOnItemClickAtPositionListener = onItemClickAtPositionListener;
    }
}
