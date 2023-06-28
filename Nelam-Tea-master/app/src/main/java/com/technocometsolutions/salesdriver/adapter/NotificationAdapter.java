package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.ViewCatalouge;
import com.technocometsolutions.salesdriver.model.GetDelalerListModel;
import com.technocometsolutions.salesdriver.model.NotificationItemModel;
import com.technocometsolutions.salesdriver.model.NotificationModel;
import com.technocometsolutions.salesdriver.utlity.CheckForSDCard;
import com.technocometsolutions.salesdriver.utlity.DownloadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    public List<NotificationItemModel> notificationItemModelList;
    public Activity mContext;



    public NotificationAdapter(Activity mContext, List<NotificationItemModel> notificationModelList) {
        this.mContext = mContext;
        this.notificationItemModelList= notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        final NotificationItemModel notificationModel= notificationItemModelList.get(position);

        holder.tvNotificationMessage.setText(notificationModel.getMessage());
        holder.tvNotificationDate.setText(notificationModel.getDate());
        holder.layoutnotification.removeAllViews();
        for (int i = 0; i < notificationItemModelList.get(position).getAttachments().size(); i++) {
            AppCompatImageView ivImageView;
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_notification_item, null);
            ivImageView=itemView.findViewById(R.id.ivAttachmnet);
            Log.d("imagessssss","dixit ffff"+notificationItemModelList.get(position).getAttachments().get(i).getAttachments());
            String last4 = takeLast(notificationItemModelList.get(position).getAttachments().get(i).getAttachments(), 4); //Output: ring
            if (last4.equalsIgnoreCase("xlsx"))
            {
                //ic_excel
                ivImageView.setImageResource(R.drawable.ic_excel);
            }
            else if (last4.equalsIgnoreCase(".pdf"))
            {
                    //ic_pdf
                ivImageView.setImageResource(R.drawable.ic_pdf);
            }
            else
            {
                Glide.with(mContext).load(""+notificationItemModelList.get(position).getAttachments().get(i).getAttachments()).into(ivImageView);
            }
            String newUrl=notificationItemModelList.get(position).getAttachments().get(i).getAttachments();
            ivImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DownloadTask(mContext, newUrl);
                    String downloadFileName = newUrl.substring(newUrl.lastIndexOf('/'), newUrl.length());
                    downloadFileName=downloadFileName.replace("/","");
                    downloadFileName=downloadFileName.trim();
                    Log.d("file_name",downloadFileName);
                    String format;
                    if (last4.equalsIgnoreCase("xlsx"))
                    {
                        format="xlsx";
                    }
                    else if (last4.equalsIgnoreCase(".pdf"))
                    {
                        format="pdf";
                    }else {
                        format="";
                    }
                    //new DownloadingFile(downloadFileName,newUrl,format);
                }
            });

            holder.layoutnotification.addView(itemView);
        }

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

    @Override
    public int getItemCount() {
        return notificationItemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout layoutnotification;
    TextView tvNotificationMessage,tvNotificationDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutnotification=itemView.findViewById(R.id.layoutnotification);
            tvNotificationMessage=itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationDate=itemView.findViewById(R.id.tvNotificationDate);
        }
    }

    private class DownloadingFile extends AsyncTask<Void, Void, Void> {


        ProgressDialog progressDialog;
        File apkStorage = null;
        File outputFile = null;
        String downloadFileName;
        String downloadUrl;
        String format;

        public DownloadingFile(String downloadFileName,String downloadUrl,String format) {
            this.downloadFileName=downloadFileName;
            this.downloadUrl=downloadUrl;
            this.format=format;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    ContextThemeWrapper ctw = new ContextThemeWrapper( mContext, R.style.AlertDialogCustom);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                    alertDialogBuilder.setTitle("Document  ");
                    alertDialogBuilder.setMessage("Document Downloaded Successfully ");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    Toast.makeText(mContext,""+ Environment.getExternalStorageDirectory() + "/Download/" + downloadFileName,Toast.LENGTH_LONG).show();
                    alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/" + downloadFileName);  // -> filename = maven.pdf
                            if (pdfFile.exists()){
                                Log.d("ff","pdf");
                            }
                            Uri path = Uri.fromFile(pdfFile);
                            Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", pdfFile);
                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            if (format.equalsIgnoreCase("xlsx")){
                                pdfIntent.setDataAndType(photoURI, "application/xlsx");

                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            }
                            else if (format.equalsIgnoreCase("pdf")){
                                pdfIntent.setDataAndType(photoURI, "application/pdf");

                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Log.d("path", String.valueOf(path));
                            }



//                            try{
                            mContext.startActivity(pdfIntent);
//                            }catch(ActivityNotFoundException e){
//                                Toast.makeText(ViewCatalouge.this, "No Application available to view", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    });
                    alertDialogBuilder.show();
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

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Download");
                } else
                    Toast.makeText(mContext, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

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
