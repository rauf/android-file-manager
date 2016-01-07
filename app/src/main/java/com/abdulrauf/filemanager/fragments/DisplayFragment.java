package com.abdulrauf.filemanager.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.dialogs.OnLongPressDialog;
import com.abdulrauf.filemanager.managers.EventManager;


import java.io.File;
import java.util.ArrayList;


/**
 * Created by abdul on 29/12/15.
 */
public class DisplayFragment extends Fragment  {


    private RecyclerView recyclerView;
    private File path;
    private ArrayList<File> filesAndFolders;
    private Toolbar toolbar;
    private EventManager eventManager;
    private DisplayFragmentAdapter adapter;
    private ActionMode actionMode;


      @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String temp;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

             temp = "/";

        else temp = Environment.getExternalStorageDirectory().toString();

        path = new File(temp);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(path.getName());

        filesAndFolders = new ArrayList<>();

        adapter = new DisplayFragmentAdapter(filesAndFolders,onItemClickListenerCallback);
        eventManager = new EventManager(getActivity(),filesAndFolders,adapter);

        recyclerView.setLayoutManager(gridLayoutManager);
        eventManager.open(path);
        recyclerView.setAdapter(adapter);

        return view;
    }




    private DisplayFragmentAdapter.OnItemClickListener onItemClickListenerCallback = new DisplayFragmentAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            File singleItem = filesAndFolders.get(position);
            eventManager.open(singleItem);

        }

        @Override
        public void onItemLongClick(View view, int position) {
            new OnLongPressDialog(onLongPressListenerCallback, position).show(getFragmentManager(), "onLongPressDialog");
        }

        @Override
        public void onIconClick(View view, int position) {

            if (actionMode != null) {

                adapter.toggleSelection(position);
                actionMode.setTitle(adapter.getSelectedItemsCount() + " items selected");

                if (adapter.getSelectedItemsCount() <= 0)
                    actionMode.finish();

                return;
            }

            actionMode = getActivity().startActionMode(actionModeCallback);
            adapter.toggleSelection(position);
            actionMode.setTitle(adapter.getSelectedItemsCount() + " items selected");
        }
    };



    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_action_mode, menu);
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
                    Toast.makeText(getActivity(), "Share Button Clicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                case R.id.deleteButton1 :
                    //fileManager.delete(adapter.getSelectedItems());
                    adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                    Toast.makeText(getActivity(), "Delete Button Clicked", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;

                case R.id.moveButton1 :
                    Toast.makeText(getActivity(), "Move Button Clicked", Toast.LENGTH_SHORT).show();
                    eventManager.move(adapter.getSelectedItems(),new File("/sdcard/"));
                    mode.finish();
                    return true;

                case R.id.copyButton1 :
                    Toast.makeText(getActivity(), "Copy Button Clicked", Toast.LENGTH_SHORT).show();
                    eventManager.copy(adapter.getSelectedItems(), new File("/sdcard/"));
                    Toast.makeText(getActivity(), "Copy Finished", Toast.LENGTH_SHORT).show();
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


    private OnLongPressDialog.OnLongPressListener  onLongPressListenerCallback = new  OnLongPressDialog.OnLongPressListener() {

        @Override
        public void onOpenButtonClicked(int position) {
            Toast.makeText(getActivity(), "open Button clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShareButtonClicked(int position) {
            Toast.makeText(getActivity(), "share Button clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDeleteButtonClicked(int position) {
            Toast.makeText(getActivity(), "delete Button clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRenameButtonClicked(int position) {
            Toast.makeText(getActivity(), "rename Button clicked", Toast.LENGTH_SHORT).show();
            promptUserForRenameInput(filesAndFolders.get(position));
        }

        @Override
        public void onCopyButtonClicked(int position) {
            Toast.makeText(getActivity(), "copy Button clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMoveButtonClicked(int position) {
            Toast.makeText(getActivity(), "move Button clicked", Toast.LENGTH_SHORT).show();
        }

    };

    private void promptUserForRenameInput(final File file) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText editText = new EditText(getActivity());
        editText.setText(file.getName());

        builder.setMessage("Type the new name ")
                .setView(editText)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventManager.rename(file,
                                editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
