package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.adapters.DisplayFragmentListItem;

import java.util.ArrayList;

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment{


    RecyclerView recyclerView;
    ArrayList<DisplayFragmentListItem> listItems;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        listItems = new ArrayList<>();

        listItems.add(new DisplayFragmentListItem("asdf"));
        listItems.add(new DisplayFragmentListItem("zxasdasdf"));
        listItems.add(new DisplayFragmentListItem("ttasdf"));

        DisplayFragmentAdapter displayFragmentAdapter = new DisplayFragmentAdapter(listItems);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(displayFragmentAdapter);

        return view;
    }
}
