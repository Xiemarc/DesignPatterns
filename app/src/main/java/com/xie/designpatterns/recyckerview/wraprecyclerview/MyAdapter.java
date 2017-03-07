package com.xie.designpatterns.recyckerview.wraprecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xie.designpatterns.R;
import com.xie.designpatterns.recyckerview.decoration.MyRecyclerAdapter;

import java.util.List;

/**
 * des:
 * author: marc
 * date:  2017/2/13 22:16
 * email：aliali_ha@yeah.net
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> list;

    public MyAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv.setText(list.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public ViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            tv = (TextView) view.findViewById(R.id.tv);
        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private MyRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyRecyclerAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
