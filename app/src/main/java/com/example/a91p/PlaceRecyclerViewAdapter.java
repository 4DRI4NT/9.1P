package com.example.a91p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> placeNameList;
    private Context context;
    private OnRowClickListener listener;

    //constructor
    public PlaceRecyclerViewAdapter(ArrayList<String> placeNameList, Context context, OnRowClickListener clickListener) {
        this.placeNameList = placeNameList;
        this.context = context;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public PlaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.place_item, parent, false);

        return new PlaceRecyclerViewAdapter.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.placeTextView.setText(placeNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return placeNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //define elements
        TextView placeTextView;
        OnRowClickListener onRowClickListener;
        public ViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);

            //link elements to id
            placeTextView = itemView.findViewById(R.id.placeTextView);
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
