package com.abdulrauf.filemanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.abdulrauf.filemanager.fragments.DisplayFragment;
import com.abdulrauf.filemanager.managers.FileManager;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    DisplayFragment displayFragment;
    FragmentManager fm;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestForPermission();
        fm = getFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        displayFragment = new DisplayFragment();
        setSupportActionBar(toolbar);

        fm.beginTransaction()
                .add(R.id.RelativeLayoutMain, displayFragment)
                .addToBackStack("displayFragment")
                .commit();

    }


    private void requestForPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                promptForPermissionsDialog("You need to give access to External Storage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                100);
                    }
                });

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }

    private void promptForPermissionsDialog(String message, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    public void onBackPressed() {

        int count = displayFragment
                .getEventManager()
                .getFileManager()
                .getPathStackItemsCount();

        if (count == 1)
            super.onBackPressed();

        else displayFragment
                .getEventManager()
                .moveUpDirectory();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.newFolder:
                createNewFolderInCurrDirectory();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }


    private void createNewFolderInCurrDirectory() {

        final FileManager fileManager = displayFragment
                                    .getEventManager()
                                    .getFileManager();

        final File dir = fileManager.getCurrentDirectory();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);

        builder.setMessage("Enter name of New Folder ")
                .setView(editText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(fileManager.newFolder(dir,editText.getText().toString())) {
                            Toast.makeText(MainActivity.this, "Folder created successfully", Toast.LENGTH_SHORT).show();
                            displayFragment.getEventManager().populateList(fileManager.getCurrentDirectory());
                        }
                        else Toast.makeText(MainActivity.this,"Cannot create New Folder",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();

    }


}