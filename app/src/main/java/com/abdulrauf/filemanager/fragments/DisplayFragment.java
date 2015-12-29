package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;

import java.io.File;

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment{

    RecyclerView recyclerView;
    String externalStorage;
    File rootSd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        //externalStorage = Environment.getExternalStorageDirectory().toString();

        rootSd = new File("/");
        File[] filesAndFolders =  rootSd.listFiles();

        Log.i("f", rootSd.toString());
        Toast.makeText(getContext(),rootSd.toString(),Toast.LENGTH_LONG).show();

        for(File file: filesAndFolders)
            System.out.println("file found                "+file);


        DisplayFragmentAdapter displayFragmentAdapter =
                new DisplayFragmentAdapter(filesAndFolders, new DisplayFragmentAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(),"working postition" + position,Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(displayFragmentAdapter);

        return view;
    }
}
