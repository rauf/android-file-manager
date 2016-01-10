package com.abdulrauf.filemanager.managers;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.fragments.DisplayFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


/**
 * Created by abdul on 5/1/16.
 */
public class EventManager {


    public enum OPERATION {
         DELETE(1),COPY(2) ,MOVE(3);

        private int val;

        OPERATION(int val){
            this.val = val;
        }

        public int getValue(){
            return val;
        }
    }

    public enum SORT{
        ASC(1),DESC(2);

        private int val;

        SORT(int val) {
            this.val = val;
        }

        public int getValue(){
            return val;
        }
    }


    private Context context;
    private FileManager fileManager;
    private ArrayList<File> filesAndFolders;
    private DisplayFragmentAdapter adapter;
    private DisplayFragment displayFragment;


    public EventManager(Context context, DisplayFragment displayFragment, ArrayList<File> filesAndFolders, DisplayFragmentAdapter adapter){

        this.context = context;
        this.displayFragment = displayFragment;
        this.filesAndFolders = filesAndFolders;
        this.adapter = adapter;
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

        }

        else if(file.isDirectory()) {

            populateList(file);
            displayFragment.getToolbar().setTitle(file.getName());
            fileManager.pushToPathStack(file);

        }
    }


    public void moveUpDirectory(){

        File file;
        file = fileManager.popFromPathStack().getParentFile();
        populateList(file);
        displayFragment.getToolbar().setTitle(file.getName());
    }


    public void populateList(File file) {

        filesAndFolders.clear();

        ArrayList<File> list = new ArrayList<>(Arrays.asList(file.listFiles()));

        filesAndFolders.addAll(
                fileManager.sort(
                        fileManager.isFileHidden() ? list : fileManager.removeHiddenFiles(list)
                ));

        adapter.notifyDataSetChanged();
    }


    public void copy(ArrayList<File> source, File destination){

        new BackgroundWork(OPERATION.COPY,source,destination)
                .execute();

    }

    public void rename(File file,String name) {

        if(fileManager.renameFileTo(file,name))
            Toast.makeText(context,"Rename successful",Toast.LENGTH_SHORT).show();

        else Toast.makeText(context,"Cannot rename",Toast.LENGTH_SHORT).show();

    }


    
    public void move(ArrayList<File> sources,File destination) {

        new BackgroundWork(OPERATION.MOVE,sources,destination)
                .execute();

    }


    public void delete (ArrayList<File> files ) {

        new BackgroundWork(OPERATION.DELETE,files)
                .execute();
    }


    private class BackgroundWork extends AsyncTask< Void, String, Boolean> {

        OPERATION operation;
        private ProgressDialog progressDialog;
        ArrayList<File> sources;
        File destination;

        public BackgroundWork(OPERATION operation, ArrayList<File> sources, File destination) {
            this.operation = operation;
            this.sources = sources;
            this.destination = destination;
        }

        public BackgroundWork(OPERATION operation, ArrayList<File> sources) {
            this.operation = operation;
            this.sources = sources;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            switch (operation) {

                case COPY:
                    progressDialog.setTitle("Copying...");
                    break;

                case MOVE:
                    progressDialog.setTitle("Moving...");
                    break;

                case DELETE:
                    progressDialog.setTitle("Deleting...");
                    break;

            }

            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {


            switch (operation) {

                case COPY:

                    for (File source : sources) {
                        try {
                            fileManager.copyToDirectory(source,destination);
                            publishProgress(source.getName());

                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;

                case MOVE:
                    for(File source: sources) {
                        try{
                            fileManager.moveToDirectory(source,destination);
                            publishProgress(source.getName());

                        } catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;


                case DELETE:
                    for(File source: sources) {

                        try {
                            fileManager.deleteFile(source);
                            publishProgress(source.getName());

                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;

            }



            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            switch (operation) {

                case COPY :
                    progressDialog.setMessage("Copying  " + values[0]);
                    break;

                case MOVE :
                    progressDialog.setMessage("Moving  " + values[0]);
                    break;

                case DELETE :
                    progressDialog.setMessage("Deleting  " + values[0]);
                    break;
            }


        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            progressDialog.dismiss();
        }

    }


    //getters


    public FileManager getFileManager() {
        return fileManager;
    }


}
