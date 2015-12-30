package com.abdulrauf.filemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.fragments.DisplayFragment;

/**
 * Created by abdul on 30/12/15.
 */
public class OnLongPressDialog extends DialogFragment {

    public interface OnLongPressListener {

        public void onOpenButtonClicked(int position);
        public void onShareButtonClicked(int position);
        public void onDeleteButtonClicked(int position);
        public void onRenameButtonClicked(int position);
        public void onCopyButtonClicked(int position);
        public void onMoveButtonClicked(int position);
    }

    OnLongPressListener onLongPressListener;
    int position;


    public OnLongPressDialog(OnLongPressListener onLongPressListener, int position) {
        this.onLongPressListener = onLongPressListener;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_on_long_press,null);

        ((TextView) view.findViewById(R.id.openButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onOpenButtonClicked(position);
            }
        });

        ((TextView) view.findViewById(R.id.shareButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onShareButtonClicked(position);
            }
        });

        ((TextView) view.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onDeleteButtonClicked(position);
            }
        });

        ((TextView) view.findViewById(R.id.renameButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onRenameButtonClicked(position);
            }
        });

        ((TextView) view.findViewById(R.id.copyButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onCopyButtonClicked(position);
            }
        });

        ((TextView) view.findViewById(R.id.moveButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongPressListener.onMoveButtonClicked(position);
            }
        });



        builder.setView(view);

        return builder.create();
    }
}
