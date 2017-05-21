package com.kleytonpascoal.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.R;

import java.sql.SQLException;

/**
 * Created by kleyton on 09/05/17.
 */

public abstract class BaseCursorRecyclerViewAdapter <T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private final String TAG = BaseCursorRecyclerViewAdapter.class.getName();

    public interface OnItemClickAtPositionListener<T> {
        void onClickAtPosition(View view, int position, T item);
    }

    public interface OnItemLongClickAtPositionListener<T> {
        void onLongClickAtPosition(View view, int position, T item);
    }


    protected Context mContext;
    private Cursor mCursor;
    protected PreparedQuery<T> mPreparedQuery;

    protected OnItemClickAtPositionListener<T> mOnItemClickAtPositionListener;
    protected OnItemLongClickAtPositionListener<T> mOnItemLongClickAtPositionListener;

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getViewHolderLayoutResId(), parent, false);
        final VH viewHolder = onCreateCustomViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mOnItemClickAtPositionListener != null) {
                    mOnItemClickAtPositionListener.onClickAtPosition(v, adapterPosition, getItemFromCursorAt(adapterPosition));
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mOnItemLongClickAtPositionListener != null) {
                    mOnItemLongClickAtPositionListener.onLongClickAtPosition(v, adapterPosition, getItemFromCursorAt(adapterPosition));
                }
                return true;
            }
        });

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    protected T getItemFromCursorAt(int position) {
        if (mCursor != null && mPreparedQuery != null) {
            try {
                mCursor.moveToPosition(position);
                return mPreparedQuery.mapRow(new AndroidDatabaseResults(mCursor, null, false));
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    public T getItemAtPosition(int position) {
        return getItemFromCursorAt(position);
    }

    public Cursor swapCursor(@Nullable Cursor cursor, @Nullable PreparedQuery<T> preparedQuery) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor oldCursor = mCursor;

        mPreparedQuery = preparedQuery;
        mCursor = cursor;

        if (mCursor != null) {
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public void setItemClickAtPositionListener(OnItemClickAtPositionListener<T> onItemClickAtPositionListener) {
        mOnItemClickAtPositionListener = onItemClickAtPositionListener;
    }

    public void setItemLongClickAtPositionListener(OnItemLongClickAtPositionListener<T> onItemLongClickAtPositionListener) {
        mOnItemLongClickAtPositionListener = onItemLongClickAtPositionListener;
    }

    protected abstract VH onCreateCustomViewHolder(View view);

    @LayoutRes
    protected abstract int getViewHolderLayoutResId();
}
