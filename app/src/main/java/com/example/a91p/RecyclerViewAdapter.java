package com.example.a91p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a91p.data.Advert;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<Advert> advertList;
    private Context context;
    private OnRowClickListener listener;

    //constructor
    public RecyclerViewAdapter(List<Advert> advertList, Context context, OnRowClickListener clickListener) {
        this.advertList = advertList;
        this.context = context;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        //sets database elements into recycler layout
        holder.recyclerPostTypeText.setText(advertList.get(position).getPostType());
        holder.recyclerDescriptionText.setText(advertList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //define elements
        TextView recyclerPostTypeText, recyclerDescriptionText;
        OnRowClickListener onRowClickListener;
        public ViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);

            //link elements to id
            recyclerPostTypeText = itemView.findViewById(R.id.recyclerPostTypeTextView);
            recyclerDescriptionText = itemView.findViewById(R.id.recyclerDescriptionTextView);
            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRowClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnRowClickListener {
        void onItemClick(int position);
    }
}
