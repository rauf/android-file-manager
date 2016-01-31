package com.abdulrauf.filemanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.managers.FileManager;

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
    private FileManager fileManager;
    private Context context;

    public DisplayFragmentAdapter(ArrayList<File> filesAndFolders, OnItemClickListener onItemClickListener, Context context) {
        this.filesAndFolders = filesAndFolders;
        this.onItemClickListener = onItemClickListener;
        selectedItems = new SparseBooleanArray();
        this.context = context;
        fileManager = new FileManager();
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
        holder.lastModified.setText(new Date(singleItem.lastModified()).toString());
        setIcon(singleItem, holder);


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
                onItemClickListener.onIconClick(holder.cardView, position);
            }
        });

        if(selectedItems.get(position,false)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#6666FF"));
        }
        else holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));


    }

    public void setIcon(File file, ListItemViewHolder holder) {

        String extension;
        Drawable drawable = null;

        try {

            extension = fileManager.getExtension(file.getAbsolutePath());

            if (file.isFile()) {

                switch (extension) {

                    case ".c":
                    case ".cpp":
                    case ".doc":
                    case ".docx":
                    case ".exe":
                    case ".h":
                    case ".html":
                    case ".java":
                    case ".log":
                    case ".txt":
                    case ".pdf":
                    case ".ppt":
                    case ".xls":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_file);
                        break;

                    case ".3ga":
                    case ".aac":
                    case ".mp3":
                    case ".m4a":
                    case ".ogg":
                    case ".wav":
                    case ".wma":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_audio);
                        break;

                    case ".3gp":
                    case ".avi":
                    case ".mpg":
                    case ".mpeg":
                    case ".mp4":
                    case ".mkv":
                    case ".webm":
                    case ".wmv":
                    case ".vob":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_video);
                        break;

                    case ".ai":
                    case ".bmp":
                    case ".exif":
                    case ".gif":
                    case ".jpg":
                    case ".jpeg":
                    case ".png":
                    case ".svg":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_image);
                        break;

                    case ".rar":
                    case ".zip":
                    case ".ZIP":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_compressed);
                        break;

                    default:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);
                        break;
                }

            }  else if (file.isDirectory()) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_folder);
            }

            else drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);

        }   catch (Exception e) {
            drawable = ContextCompat.getDrawable(context,R.drawable.ic_error);
        }

        drawable = DrawableCompat.wrap(drawable);
        holder.icon.setImageDrawable(drawable);

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

    public void deleteSelectedItemsFromList(){

        for (int i = 0; i < selectedItems.size(); i++) {
            filesAndFolders.remove(selectedItems.keyAt(i));
        }

        notifyDataSetChanged();
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }


}
