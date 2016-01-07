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

import com.abdulrauf.filemanager.fragments.DisplayFragment;



public class MainActivity extends AppCompatActivity {

    DisplayFragment displayFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestForPermission();
        fm = getFragmentManager();

        displayFragment = new DisplayFragment();

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

    private void promptForPermissionsDialog(String message, DialogInterface.OnClickListener onClickListener ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(message)
                .setPositiveButton("OK", onClickListener )
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

        if( count == 1)
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
