package com.xie.designpatterns.fb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xie.designpatterns.R;

import java.util.List;

/**
 * Created by marc on 2017/4/19.
 */

public class FabRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> list;

    public FabRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        String str = list.get(position);
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.tv.setText(str);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

    }
}
