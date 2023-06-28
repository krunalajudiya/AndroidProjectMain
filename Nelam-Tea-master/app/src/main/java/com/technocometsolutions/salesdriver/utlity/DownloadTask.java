package com.technocometsolutions.salesdriver.utlity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.ViewCatalouge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Activity context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;



    public DownloadTask(Activity context, String downloadUrl) {
        this.context = context;

        this.downloadUrl = downloadUrl;


        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    public static String takeLast(String value, int count) {
        if (value == null || value.trim().length() == 0 || count < 1) {
            return "";
        }

        if (value.length() > count) {
            return value.substring(value.length() - count);
        } else {
            return value;
        }
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
//                    ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.AlertDialogCustom);
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                    alertDialogBuilder.setTitle("Document  ");
//                    alertDialogBuilder.setMessage("Document Downloaded Successfully ");
//                    alertDialogBuilder.setCancelable(false);
//                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    });
                    Toast.makeText(context,""+Environment.getExternalStorageDirectory() + "/Letina/" + downloadFileName,Toast.LENGTH_LONG).show();

                    String format=takeLast(downloadFileName, 4);
                    Log.d("format",format);
                    File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Letina" + downloadFileName);  // -> filename = maven.pdf
                    if (pdfFile.exists()){
                        Log.d("ff","pdf");
                    }
                    Uri path = Uri.fromFile(pdfFile);
                    Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    if (format.equalsIgnoreCase("xlsx")){
//                        pdfIntent.setDataAndType(photoURI, "application/xlsx");
//                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        Log.d("path", String.valueOf(path));
//                        context.startActivity(pdfIntent);
//                    }
//                    else if (format.equalsIgnoreCase("pdf")){
                        pdfIntent.setDataAndType(photoURI, "application/pdf");
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Log.d("path", String.valueOf(path));
                        context.startActivity(pdfIntent);
//                    }



//                    alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Neelam Sales Force/" + downloadFileName);  // -> filename = maven.pdf
//                            Uri path = Uri.fromFile(pdfFile);
//                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                            pdfIntent.setDataAndType(path, "*/*");
//                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            try{
//                                context.startActivity(pdfIntent);
//                            }catch(ActivityNotFoundException e){
//                                Toast.makeText(context, "No Application available to view", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    //alertDialogBuilder.show();
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Letina");
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
}
