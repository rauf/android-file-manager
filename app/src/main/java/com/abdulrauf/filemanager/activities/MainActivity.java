package com.abdulrauf.filemanager.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.fragments.DisplayFragment;
import com.abdulrauf.filemanager.managers.FileManager;

import java.io.File;


public class MainActivity extends AppCompatActivity {


    final String DISPLAY_FRAGMENT_TAG = "displayFragment";

    RelativeLayout relativeLayout;
    FragmentManager fm;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayoutMain);

        requestForPermission();
        fm = getFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().detectAll()
                        .penaltyLog().build());

        if(fm.findFragmentById(R.id.RelativeLayoutMain) == null) {
            DisplayFragment displayFragment = new DisplayFragment();
            setSupportActionBar(toolbar);

            fm.beginTransaction()
                    .add(R.id.RelativeLayoutMain, displayFragment,DISPLAY_FRAGMENT_TAG)
                    .addToBackStack(DISPLAY_FRAGMENT_TAG)
                    .commit();
        }
    }



    private void requestForPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                promptForPermissionsDialog(getString(R.string.error_request_permission), new DialogInterface.OnClickListener() {
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
                .setPositiveButton(getString(R.string.ok), onClickListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();

    }

    @Override
    public void onBackPressed() {

        DisplayFragment displayFragment = (DisplayFragment) fm.findFragmentByTag(DISPLAY_FRAGMENT_TAG);

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

            case R.id.action_settings:
                Intent intent = new Intent(this,PrefsActivity.class);
                // bypassing the single header
                intent.putExtra( PrefsActivity.EXTRA_SHOW_FRAGMENT, PrefsActivity.Header1.class.getName() );
                intent.putExtra( PrefsActivity.EXTRA_NO_HEADERS, true );
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private void createNewFolderInCurrDirectory() {

        final DisplayFragment displayFragment = (DisplayFragment) fm.findFragmentByTag(DISPLAY_FRAGMENT_TAG);

        final FileManager fileManager = displayFragment
                                    .getEventManager()
                                    .getFileManager();

        final File dir = fileManager.getCurrentDirectory();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);

        builder.setMessage(getString(R.string.prompt_create_folder))
                .setView(editText)
                .setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(fileManager.newFolder(dir,editText.getText().toString())) {
                            Toast.makeText(MainActivity.this, getString(R.string.success_create_folder), Toast.LENGTH_SHORT).show();
                            displayFragment.getEventManager().populateList(fileManager.getCurrentDirectory());
                        }
                        else Toast.makeText(MainActivity.this,getString(R.string.error_create_folder),Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel),null)
                .create()
                .show();

    }


}