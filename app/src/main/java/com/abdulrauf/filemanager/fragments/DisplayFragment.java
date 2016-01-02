package com.abdulrauf.filemanager.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.dialogs.OnLongPressDialog;
import com.abdulrauf.filemanager.managers.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment implements
        DisplayFragmentAdapter.OnItemClickListener,
        OnLongPressDialog.OnLongPressListener{

    RecyclerView recyclerView;
    String externalStorage;
    File path;
    ArrayList<File> filesAndFolders;
    FileManager fileManager;
    DisplayFragmentAdapter adapter;
    ActionMode actionMode;
    Toolbar toolbar;

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

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(path.getName());

        //externalStorage = Environment.getExternalStorageDirectory().toString();
        filesAndFolders = new ArrayList<>(Arrays.asList(path.listFiles()));
        fileManager = new FileManager(getContext());

        adapter = new DisplayFragmentAdapter(filesAndFolders, this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        File singleItem = filesAndFolders.get(position);
        fileManager.open(singleItem);
    }


    @Override
    public void onItemLongClick(View view, int position) {
            new OnLongPressDialog(this,position).show(getFragmentManager(), "onLongPressDialog");
    }


    @Override
    public void onIconClick(View view, int position) {

        if(actionMode != null) {

            adapter.toggleSelection(position);
            actionMode.setTitle(adapter.getSelectedItemsCount() + " items selected");

            if ( adapter.getSelectedItemsCount() <= 0 )
                actionMode.finish();

            return;
        }

        actionMode = getActivity().startActionMode(actionModeCallback);
        adapter.toggleSelection(position);
        actionMode.setTitle(adapter.getSelectedItemsCount() + " items selected");
    }


    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_long_press, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.shareButton1 :
                    Toast.makeText(getContext(), "Share Button CLicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                case R.id.deleteButton1 :
                    Toast.makeText(getContext(), "Delete Button CLicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                case R.id.moveButton1 :
                    Toast.makeText(getContext(), "Move Button CLicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                case R.id.copyButton1 :
                    Toast.makeText(getContext(), "Copy Button CLicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelection();
        }

    };




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
