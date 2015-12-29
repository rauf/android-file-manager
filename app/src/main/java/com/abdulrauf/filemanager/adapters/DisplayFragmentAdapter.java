package com.abdulrauf.filemanager.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulrauf.filemanager.R;

import java.util.ArrayList;


/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragmentAdapter extends RecyclerView.Adapter<DisplayFragmentAdapter.ListItemViewHolder>{


    ArrayList<DisplayFragmentListItem> listItems;

    public DisplayFragmentAdapter(ArrayList<DisplayFragmentListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.list_item_fragment_display,parent,false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        DisplayFragmentListItem singleItem = listItems.get(position);

        holder.title.setText(singleItem.getTitle());
        holder.cardView.setCardBackgroundColor(3566);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }

}
