package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.CatalougeAdapter;
import com.technocometsolutions.salesdriver.adapter.GetOrderListAdapter;
import com.technocometsolutions.salesdriver.model.CatalougeDetailsModel;
import com.technocometsolutions.salesdriver.model.CatalougeModel;
import com.technocometsolutions.salesdriver.model.GetOrderListModel;
import com.technocometsolutions.salesdriver.utlity.CheckForSDCard;
import com.technocometsolutions.salesdriver.utlity.DownloadCatalouge;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCatalouge extends AppCompatActivity {

    private static final int PERMISSION_CODE_WRITE = 1000;
    private static final int PERMISSION_CODE_READ = 1001;
    RecyclerView catalougerecyclerview;
    LoadingView loadingView;
    ErrorView errorView;
    List<CatalougeDetailsModel> catalougeDetailsModels=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    private static final String TAG = "Download Task";
    private Activity context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_catalouge);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catalougerecyclerview=findViewById(R.id.catalougerecyclerview);


        getcatalougedetails();
    }

    void getcatalougedetails() {
        loadingView = new LoadingView(ViewCatalouge.this);
        loadingView.showLoadingView();

        RequestQueue queue = Volley.newRequestQueue(ViewCatalouge.this);
        String url;
        url = getString(R.string.json_get_all_catalouge);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: " + response);
                        //  loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        CatalougeModel loginResponse = gson.fromJson(response, CatalougeModel.class);
                        if (loginResponse.getSuccess()) {
                            if (loginResponse.getItems() != null) {
                                loadingView.hideLoadingView();
                                catalougeDetailsModels=loginResponse.getItems();
                                CatalougeAdapter catalougeAdapter=new CatalougeAdapter(ViewCatalouge.this,catalougeDetailsModels);
                                linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                                gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
                                catalougerecyclerview.setLayoutManager(gridLayoutManager);
                                catalougerecyclerview.setAdapter(catalougeAdapter);
                                catalougeAdapter.setOnItemClickListener(new CatalougeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int pos, View v) {
                                        String catalougeurl=getString(R.string.catalouge_url)+catalougeDetailsModels.get(pos).getCatalogue_pdf();
                                        Log.d("urldownload",catalougeurl);
//                                        DownloadCatalouge downloadCatalouge=new DownloadCatalouge(ViewCatalouge.this,catalougeurl);
                                        downloadUrl=catalougeurl;
                                        downloadFileName=catalougeDetailsModels.get(pos).getCatalogue_pdf();
                                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                                String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                                                requestPermissions(permissions,PERMISSION_CODE_WRITE);
                                            }
                                            else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                                String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                requestPermissions(permissions,PERMISSION_CODE_READ);
                                            }else {
                                                //new DownloadingCatalouge().execute();
                                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                                pdfIntent.setDataAndType(Uri.parse(catalougeurl), "application/pdf");
                                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                startActivity(pdfIntent);
                                            }
                                        }else {

                                            //new DownloadingCatalouge().execute();

                                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                            pdfIntent.setDataAndType(Uri.parse(catalougeurl), "application/pdf");
                                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            startActivity(pdfIntent);
                                        }
                                    }
                                });


                            }
                        } else {
                               Toast.makeText(ViewCatalouge.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: " + error.getMessage());
                errorView = new ErrorView(ViewCatalouge.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            ;
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    private class DownloadingCatalouge extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewCatalouge.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    ContextThemeWrapper ctw = new ContextThemeWrapper( ViewCatalouge.this, R.style.AlertDialogCustom);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                    alertDialogBuilder.setTitle("Document  ");
                    alertDialogBuilder.setMessage("Document Downloaded Successfully ");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    Toast.makeText(ViewCatalouge.this,""+ Environment.getExternalStorageDirectory() + "/Download/" + downloadFileName,Toast.LENGTH_LONG).show();
                    alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/" + downloadFileName);  // -> filename = maven.pdf
                            if (pdfFile.exists()){
                                Log.d("ff","pdf");
                            }
                            Uri path = Uri.fromFile(pdfFile);
                            Uri photoURI = FileProvider.getUriForFile(ViewCatalouge.this, ViewCatalouge.this.getPackageName() + ".provider", pdfFile);
                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            pdfIntent.setDataAndType(photoURI, "application/pdf");
                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            Log.d("path", String.valueOf(path));


//                            try{
                                startActivity(pdfIntent);
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
                    Toast.makeText(ViewCatalouge.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}