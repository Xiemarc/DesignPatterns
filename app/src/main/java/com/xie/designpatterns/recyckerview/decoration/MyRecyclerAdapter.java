package com.xie.designpatterns.recyckerview.decoration;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * des:
 * author: marc
 * date:  2017/2/12 21:13
 * email：aliali_ha@yeah.net
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<String> list;
    private OnItemClickListener mOnItemClickListener;

    public MyRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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

    public void addData(int position) {
        list.add(position, "additem" + position);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
