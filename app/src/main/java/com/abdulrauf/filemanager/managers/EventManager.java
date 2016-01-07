package com.abdulrauf.filemanager.managers;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.abdulrauf.filemanager.MainActivity;
import com.abdulrauf.filemanager.R;
import com.abdulrauf.filemanager.adapters.DisplayFragmentAdapter;
import com.abdulrauf.filemanager.dialogs.OnLongPressDialog;
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



    public EventManager(Context context) {
        this.context = context;
        fileManager = new FileManager();
    }

    public EventManager(Context context, ArrayList<File> filesAndFolders, DisplayFragmentAdapter adapter){
        this(context);
        this.filesAndFolders = filesAndFolders;
        this.adapter = adapter;
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

            filesAndFolders.clear();
            filesAndFolders.addAll(Arrays.asList(file.listFiles()));

            adapter.notifyDataSetChanged();
            fileManager.pushToPathStack(file);
        }
    }


    public void moveUpDirectory(){

        File file;

        file = fileManager.popFromPathStack().getParentFile();
        System.out.println("else  " + file);

        filesAndFolders.clear();
        filesAndFolders.addAll(Arrays.asList(file.listFiles()));
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

    public ArrayList<File> sort(SORT type,ArrayList<File> files, boolean caseSensitive) {

        ArrayList<File> sortedFiles = new ArrayList<>();

        switch (type) {

            case ASC :
                sortedFiles = fileManager.sortAscending(files,caseSensitive);
                break;

            case DESC:
                sortedFiles = fileManager.sortDescending(files,caseSensitive);
                break;
        }

        return sortedFiles;
    }





    private class BackgroundWork extends AsyncTask< Void, Integer, Boolean> {

        OPERATION operation;
        private ProgressDialog progressDialog;
        ArrayList<File> sources;
        File destination;

        public BackgroundWork(OPERATION operation, ArrayList<File> sources, File destination) {
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
                    break;


                case MOVE:
                    progressDialog.setTitle("Moving...");
                    progressDialog.setMessage("Files are being moves");
                    progressDialog.show();
                    break;
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
                    break;

                case MOVE:
                    for(File source: sources) {
                        try{
                            fileManager.moveToDirectory(source,destination);
                        } catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;

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


    //getters


    public FileManager getFileManager() {
        return fileManager;
    }
}
