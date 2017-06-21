package com.imceits.android.imagerecycler;

//Created by AUNGTUNTUN on 21/06/2017.


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<RecycleData> imageList;
    private Context mContext;

    public MyAdapter(Context context, ArrayList<RecycleData> dataList){
        this.mContext = context;
        this.imageList = dataList;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textHeader.setText(imageList.get(position).getLabel());
        Picasso.with(mContext).load(imageList.get(position).getImageUrl()).noFade().resize(200,100).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textHeader;
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textHeader = (TextView) itemView.findViewById(R.id.txtHeader);
            imageView = (ImageView) itemView.findViewById(R.id.imgView1);
        }
    }  // inner class

}
