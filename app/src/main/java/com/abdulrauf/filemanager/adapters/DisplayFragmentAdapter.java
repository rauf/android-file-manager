package com.abdulrauf.filemanager.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrauf.filemanager.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by abdul on 29/12/15.
 */

public class DisplayFragmentAdapter extends RecyclerView.Adapter<DisplayFragmentAdapter.ListItemViewHolder>{


    public interface OnItemClickListener  {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view,int position);
        public void onIconClick(View view,int position);
    }

    private ArrayList<File> filesAndFolders;
    private OnItemClickListener onItemClickListener;

    public DisplayFragmentAdapter(ArrayList<File> filesAndFolders, OnItemClickListener onItemClickListener) {
        this.filesAndFolders = filesAndFolders;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_fragment_display, parent, false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        final File singleItem = filesAndFolders.get(position);

        holder.title.setText(singleItem.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(v, position);
                return true;
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#6666ff"));
                onItemClickListener.onIconClick(holder.cardView,position);
            }
        });

    }

    @Override
    public int getItemCount() {
            return filesAndFolders.size();
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        ImageView icon;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

}
