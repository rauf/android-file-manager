package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.dialogs.OnLongPressDialog;
import com.abdulrauf.filemanager.managers.FileManager;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment implements
        DisplayFragmentAdapter.OnItemClickListener,
        OnLongPressDialog.OnLongPressListener {

    RecyclerView recyclerView;
    String externalStorage;
    File path;
    ArrayList<File> filesAndFolders;
    FileManager fileManager;
    ArrayList<File> selectedFiles;
    DisplayFragmentAdapter displayFragmentAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String temp = "/";

        try {
            temp = getArguments().getString("path");
        }catch (Exception e) {
            Toast.makeText(getActivity(),"Exception",Toast.LENGTH_LONG).show();
        } finally {
            path = new File(temp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        //externalStorage = Environment.getExternalStorageDirectory().toString();
        filesAndFolders = new ArrayList<>(Arrays.asList(path.listFiles()));
        selectedFiles = new ArrayList<>();
        fileManager = new FileManager(getContext());

        displayFragmentAdapter = new DisplayFragmentAdapter(filesAndFolders, this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(displayFragmentAdapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        File singleItem = filesAndFolders.get(position);
        fileManager.open(singleItem);
    }


    @Override
    public void onItemLongClick(View view, int position) {
            new OnLongPressDialog(this,position).show(getFragmentManager(),"onLongPressDialog");
    }


    @Override
    public void onIconClick(View view, int position) {

        File singleFile = filesAndFolders.get(position);

        if(!selectedFiles.contains(singleFile) )
            selectedFiles.add(singleFile);

        else {
            selectedFiles.remove(singleFile);
            Toast.makeText(getActivity(),"file removed from selected",Toast.LENGTH_SHORT).show();
            ((CardView)view).setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public void onOpenButtonClicked(int position) {
        Toast.makeText(getActivity(),"open Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareButtonClicked(int position) {
        Toast.makeText(getActivity(),"share Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteButtonClicked(int position) {
        Toast.makeText(getActivity(),"delete Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRenameButtonClicked(int position) {
        Toast.makeText(getActivity(),"rename Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCopyButtonClicked(int position) {
        Toast.makeText(getActivity(),"copy Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoveButtonClicked(int position) {
        Toast.makeText(getActivity(),"move Button clicked",Toast.LENGTH_SHORT).show();
    }


}
