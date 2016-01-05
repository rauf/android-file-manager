package com.abdulrauf.filemanager.managers;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by abdul on 31/12/15.
 */

public class FileManager {



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


    public boolean copyToDirectory(File old, File newDir) throws IOException {


        int temp;
        File copiedFile = new File(newDir + "/" + old.getName());

        if( !old.exists() || !newDir.exists())
            return false;

        if( old.isFile() && old.canRead() && newDir.isDirectory() && newDir.canWrite()) {

            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            try {

                in = new BufferedInputStream(new FileInputStream(old));
                out = new BufferedOutputStream(new FileOutputStream(copiedFile));

                while ( (temp = in.read()) != -1) {
                    out.write(temp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
            }
        }

        else if (old.isDirectory() && old.canRead() && newDir.isDirectory() && newDir.canExecute()) {

            File[] files = old.listFiles();

            if(!copiedFile.mkdirs())
                return false;

            for (File singleFile : files) {
                System.out.println(singleFile + "     copiedFile is " + copiedFile);
                copyToDirectory(singleFile, copiedFile);
            }
        }

        return true;
    }




}
