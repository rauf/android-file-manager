package com.abdulrauf.filemanager.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abdulrauf.filemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


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
    private SparseBooleanArray selectedItems;

    public DisplayFragmentAdapter(ArrayList<File> filesAndFolders, OnItemClickListener onItemClickListener) {
        this.filesAndFolders = filesAndFolders;
        this.onItemClickListener = onItemClickListener;
        selectedItems = new SparseBooleanArray();
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
        holder.lastModified.setText( new Date(singleItem.lastModified()).toString());

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
                onItemClickListener.onIconClick(holder.cardView,position);
            }
        });

        if(selectedItems.get(position,false)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#6666FF"));
        }
        else holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

    }

    @Override
    public int getItemCount() {
            return filesAndFolders.size();
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView lastModified;
        ImageView icon;
        LinearLayout linearLayout;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            lastModified = (TextView) itemView.findViewById(R.id.lastModified);
        }
    }

    public void select(int position) {

        selectedItems.put(position, true);
        notifyItemChanged(position);
    }

    public void unselect(int position) {

        if(selectedItems.get(position,false)) {
            selectedItems.delete(position);
        }
        notifyItemChanged(position);
    }

    public void toggleSelection(int position) {

        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        }
        else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }


    public void clearSelection() {

        selectedItems.clear();
        notifyDataSetChanged();
    }

    public ArrayList<File> getSelectedItems() {

        ArrayList<File> list = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            list.add(filesAndFolders.get(selectedItems.keyAt(i)));
        }

        return list;
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }


}
