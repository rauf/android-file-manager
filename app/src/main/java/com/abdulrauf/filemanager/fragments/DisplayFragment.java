package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrauf.filemanager.MainActivity;
import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;

import java.io.File;

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment{

    public interface FragmentChange {
        public void onDirectoryClicked(File path);
    }

    RecyclerView recyclerView;
    String externalStorage;
    File path;
    FragmentChange fragmentChange;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentChange = (MainActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String temp = "/";

        try {
            temp = getArguments().getString("path");
        }catch (Exception e) {
            Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
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

        final File[] filesAndFolders =  path.listFiles();

        Toast.makeText(getContext(),"new fragment",Toast.LENGTH_LONG).show();

        DisplayFragmentAdapter displayFragmentAdapter =
                new DisplayFragmentAdapter(filesAndFolders, new DisplayFragmentAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        File singleItem = filesAndFolders[position];

                        if(singleItem.isFile()) {
                            Toast.makeText(getContext(), "Target is a file", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!singleItem.canRead()) {
                            Toast.makeText(getContext(), "Do not have read access", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        fragmentChange.onDirectoryClicked(singleItem);
                    }
                });


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(displayFragmentAdapter);

        return view;
    }

}
