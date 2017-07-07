package com.xie.designpatterns.miui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xie.designpatterns.R;
import com.xie.designpatterns.miui.activity.Item;
import com.xie.designpatterns.utils.CircleImageView;

import java.util.List;

/**
 * Created by marc on 2017/7/7.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<Item> mList;

    public ItemAdapter(List<Item> mList) {
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_girl, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = mList.get(position);
        holder.iv.setImageResource(item.img);
        holder.tv.setText(item.text);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        CircleImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text_item);
            iv = (CircleImageView) itemView.findViewById(R.id.img_item);
        }
    }
}
