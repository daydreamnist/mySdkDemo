package com.broadlink.mysdkdemo.commonUtils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder> {


    private List<T> datas ;
    private int itemViewId;
    private int contentViewId;
    private OnItemClickListener onItemClickListener;

    public SimpleRecyclerAdapter() {
    }

    public SimpleRecyclerAdapter(List<T> datas, int itemViewId, int contentViewId) {
        this.datas = datas;
        this.itemViewId = itemViewId;
        this.contentViewId = contentViewId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SimpleRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(itemViewId,viewGroup,false);
        if (onItemClickListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("setOnClickListener","click");

                    onItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("setOnClickListener","long click");
                    onItemClickListener.onItemLongClick(v,(Integer) v.getTag());
                    return true;
                }
            });
        }

        return new ViewHolder(itemView);
    }

    public List<T> getDatas(){
        return this.datas;
    }


    @Override
    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.ViewHolder viewHolder, int i) {
        Object data = datas.get(i);
        if (data instanceof String){
            viewHolder.mTv_content.setText((String)data);
        }else {
            viewHolder.mTv_content.setText(JSON.toJSONString(data,true));
        }
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addItem(T o){
        datas.add(o);
        notifyItemInserted(datas.size());
    }

    public void removeItem(int position){
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public void resetList(){

        for (int i = 0;i<datas.size();i++) {
            notifyItemRemoved(i);
        }
        datas.clear();
    }




    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView mTv_content;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv_content = itemView.findViewById(contentViewId);
        }
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void setItemViewId(int itemViewId) {
        this.itemViewId = itemViewId;
    }

    public void setContentViewId(int contentViewId) {
        this.contentViewId = contentViewId;
    }
}
