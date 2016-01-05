package com.abdulrauf.filemanager.managers;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.abdulrauf.filemanager.MainActivity;
import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.fragments.DisplayFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by abdul on 5/1/16.
 */
public class EventManager {


    private final int DELETE = 1;
    private final int COPY = 2;
    private final int MOVE = 3;

    Context context;
    FragmentManager fm;
    FileManager fileManager;

    public EventManager(Context context) {
        this.context = context;
        this.fm = ((MainActivity) context).getFragmentManager();
        fileManager = new FileManager();
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
            String mimeType = mime.getMimeTypeFromExtension(fileManager.getExtension(file.getAbsolutePath()).substring(1));
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



    public void copy(ArrayList<File> source, File destination){

        new BackgroundWork(COPY,source,destination)
                .execute();

    }






    private class BackgroundWork extends AsyncTask< Void, Integer, Boolean> {


        int operation;
        private ProgressDialog progressDialog;
        ArrayList<File> sources;
        File destination;

        public BackgroundWork(int operation, ArrayList<File> sources, File destination) {
            this.operation = operation;
            this.sources = sources;
            this.destination = destination;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            switch (operation) {

                case COPY:
                    progressDialog.setTitle("Copying...");
                    progressDialog.setMessage("Files are being copied");
                    progressDialog.show();

            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            switch (operation) {

                case COPY:

                    for (File source : sources) {

                        try {
                            fileManager.copyToDirectory(source,destination);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }



            }



            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            progressDialog.dismiss();
        }

    }



}
