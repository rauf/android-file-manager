package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment implements
        DisplayFragmentAdapter.OnItemClickListener,
        OnLongPressDialog.OnLongPressListener {

    RecyclerView recyclerView;
    String externalStorage;
    File path;
    File[] filesAndFolders;
    FileManager fileManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String temp = "/";

        try {
            temp = getArguments().getString("path");
        }catch (Exception e) {
            Toast.makeText(getContext(),"Exception",Toast.LENGTH_LONG).show();
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
        filesAndFolders =  path.listFiles();

        fileManager = new FileManager(getContext());

        DisplayFragmentAdapter displayFragmentAdapter = new DisplayFragmentAdapter(filesAndFolders, this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(displayFragmentAdapter);

        return view;
    }


    @Override
    public void onItemClick(View view, int position) {
        File singleItem = filesAndFolders[position];
        fileManager.open(singleItem);
    }


    @Override
    public void onItemLongClick(View view, int position) {
            new OnLongPressDialog(this,position).show(getFragmentManager(),"onLongPressDialog");
    }


    @Override
    public void onOpenButtonClicked(int position) {
        Toast.makeText(getContext(),"open Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareButtonClicked(int position) {
        Toast.makeText(getContext(),"share Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteButtonClicked(int position) {
        Toast.makeText(getContext(),"delete Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRenameButtonClicked(int position) {
        Toast.makeText(getContext(),"rename Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCopyButtonClicked(int position) {
        Toast.makeText(getContext(),"copy Button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoveButtonClicked(int position) {
        Toast.makeText(getContext(),"move Button clicked",Toast.LENGTH_SHORT).show();
    }


}
