package com.abdulrauf.filemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

    public interface OnLongPressListener extends Parcelable{

        public void onOpenButtonClicked(int position);
        public void onShareButtonClicked(int position);
        public void onDeleteButtonClicked(int position);
        public void onRenameButtonClicked(int position);
        public void onCopyButtonClicked(int position);
        public void onMoveButtonClicked(int position);

    }

    private static String POSITION_KEY = "position";
    private static String LISTENER_KEY = "onLongPressListener";


    public static OnLongPressDialog newInstance(OnLongPressListener onLongPressListener,int pos) {

        Bundle args = new Bundle();

        OnLongPressDialog fragment = new OnLongPressDialog();

        args.putInt(POSITION_KEY,pos);
        args.putParcelable(LISTENER_KEY, onLongPressListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final int position = getArguments().getInt(POSITION_KEY);
        final OnLongPressListener onLongPressListener = getArguments().getParcelable(LISTENER_KEY);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_on_long_press,null);

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
