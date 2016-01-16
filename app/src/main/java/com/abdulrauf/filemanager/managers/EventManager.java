package com.abdulrauf.filemanager.managers;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.fragments.DisplayFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.String.*;


/**
 * Created by abdul on 5/1/16.
 */
public class EventManager {


    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";

    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_COPY = "COPY";
    public static final String OPERATION_MOVE = "MOVE";
    public static final String OPERATION_POPULATE_LIST = "POPULATE_LIST";



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


    public void refreshCurrentDirectory() {

        populateList(fileManager.getCurrentDirectory());
    }

    public void populateList(File file) {

        new BackgroundWork(OPERATION_POPULATE_LIST,null,file).execute();
    }


    public void share(ArrayList<File> files) {


        Uri contentUri = FileProvider.getUriForFile(context, "com.abdulrauf.filemanager.fileprovider", files.get(0));
        ClipData clipData = ClipData.newRawUri(null, contentUri);

        for (int i = 1; i < files.size() ; i++) {
            Uri otherUri = FileProvider.getUriForFile(context, "com.abdulrauf.filemanager.fileprovider", files.get(i));
            clipData.addItem(new ClipData.Item(otherUri));
        }

        Intent intent = ShareCompat.IntentBuilder.from((Activity) context).setType("*/*").setStream(contentUri).getIntent();
        intent.setClipData(clipData);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(intent);

    }

    public void copy(ArrayList<File> source, File destination){

        new BackgroundWork(OPERATION_COPY,source,destination)
                .execute();

    }


    public void move(ArrayList<File> sources,File destination) {

        new BackgroundWork(OPERATION_MOVE,sources,destination)
                .execute();

    }


    public void delete (ArrayList<File> files ) {

        new BackgroundWork(OPERATION_DELETE,files,null)
                .execute();
    }




    private class BackgroundWork extends AsyncTask< Void, String, Boolean> {

        private String operation;
        private ProgressDialog progressDialog;
        private ArrayList<File> sources;
        private File destination;

        public BackgroundWork(String operation, ArrayList<File> sources, File destination) {
            this.operation = operation;
            this.sources = sources;
            this.destination = destination;
        }

        @Override
        protected void onPreExecute() {


            switch (operation) {

                case OPERATION_COPY:
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Copying...");
                    progressDialog.show();
                    break;

                case OPERATION_MOVE:
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Moving...");
                    progressDialog.show();
                    break;

                case OPERATION_DELETE:
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Deleting...");
                    progressDialog.show();
                    break;

                case OPERATION_POPULATE_LIST:
                    break;

            }



        }

        @Override
        protected Boolean doInBackground(Void... params) {


            switch (operation) {

                case OPERATION_COPY:

                    for (File source : sources) {
                        try {
                            publishProgress(source.getName());
                            fileManager.copyToDirectory(source,destination);

                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;

                case OPERATION_MOVE:
                    for(File source: sources) {
                        try{
                            publishProgress(source.getName());
                            fileManager.moveToDirectory(source,destination);

                        } catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;


                case OPERATION_DELETE:
                    for(File source: sources) {

                        try {
                            publishProgress(source.getName());
                            fileManager.deleteFile(source);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;


                case OPERATION_POPULATE_LIST:

                    filesAndFolders.clear();
                    ArrayList<File> list = new ArrayList<>(Arrays.asList(destination.listFiles()));

                    filesAndFolders.addAll(
                            fileManager.sort(
                                    fileManager.isFileHidden() ? list : fileManager.removeHiddenFiles(list)
                            ));
                    break;

            }



            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            switch (operation) {

                case OPERATION_COPY :
                    progressDialog.setMessage("Copying  " + values[0]);
                    break;

                case OPERATION_MOVE :
                    progressDialog.setMessage("Moving  " + values[0]);
                    break;

                case OPERATION_DELETE :
                    progressDialog.setMessage("Deleting  " + values[0]);
                    break;


            }

        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            switch (operation) {

                case OPERATION_COPY :
                    progressDialog.dismiss();
                    Toast.makeText(context,"Files successfully copied",Toast.LENGTH_SHORT).show();
                    refreshCurrentDirectory();
                    break;

                case OPERATION_MOVE :
                    progressDialog.dismiss();
                    Toast.makeText(context,"Files successfully moved",Toast.LENGTH_SHORT).show();
                    refreshCurrentDirectory();
                    break;

                case OPERATION_DELETE :
                    progressDialog.dismiss();
                    Toast.makeText(context,"Files successfully deleted",Toast.LENGTH_SHORT).show();
                    refreshCurrentDirectory();
                    break;

                case OPERATION_POPULATE_LIST:
                    adapter.notifyDataSetChanged();
                    break;

            }

        }

    }


    //getters


    public FileManager getFileManager() {
        return fileManager;
    }


}
