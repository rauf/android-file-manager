package com.abdulrauf.filemanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abdulrauf.filemanager.fragments.DisplayFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements DisplayFragment.FragmentChange{

    DisplayFragment displayFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();

        displayFragment = new DisplayFragment();
        fm.beginTransaction()
                .add(R.id.RelativeLayoutMain, displayFragment)
                .commit();
    }

    @Override
    public void onDirectoryClicked(File path) {

        DisplayFragment displayFragment = new DisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path",path.getAbsolutePath());
        displayFragment.setArguments(bundle);

        fm.beginTransaction()
                .addToBackStack("prev")
                .replace(R.id.RelativeLayoutMain,displayFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        fm.popBackStackImmediate();
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
