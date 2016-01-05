package com.abdulrauf.filemanager.managers;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.abdulrauf.filemanager.MainActivity;
import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.fragments.DisplayFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by abdul on 31/12/15.
 */

public class FileManager {

    Context context;
    FragmentManager fm;

    public FileManager(Context context) {
        this.context = context;
        this.fm = ((MainActivity) context).getFragmentManager();
    }

    public void open(File file) {

        if(!file.canRead()) {
            Toast.makeText(context, "Do not have read access", Toast.LENGTH_SHORT).show();
            return;
        }

        if(file.isFile()) {

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            String mimeType = mime.getMimeTypeFromExtension(getExtension(file.getAbsolutePath()).substring(1));
            i.setDataAndType(Uri.fromFile(file), mimeType);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
            }

            return;
        }

        if(file.isDirectory()) {

            DisplayFragment displayFragment = new DisplayFragment();
            Bundle bundle = new Bundle();
            bundle.putString("path",file.getAbsolutePath());
            displayFragment.setArguments(bundle);

            fm.beginTransaction()
                    .addToBackStack("prev")
                    .replace(R.id.RelativeLayoutMain,displayFragment)
                    .commit();
            return;
        }
    }

    public String getExtension(String url) {


        if (url.contains("?")) {
             url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }




}
