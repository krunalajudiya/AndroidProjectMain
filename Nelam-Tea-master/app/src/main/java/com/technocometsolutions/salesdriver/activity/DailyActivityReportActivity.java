package com.technocometsolutions.salesdriver.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.utlity.CameraUtils;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.ImageCompress;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class DailyActivityReportActivity extends AppCompatActivity {
    public SearchableSpinner sp_dealers;
    public LoadingView loadingView;
    public ErrorView errorView;
    public String dealer_id="",st_message="";
    public SessionManager sessionManager;
    public StateAdapter stateAdapter;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public EditText et_message;
    public Button btn_submit;
    public LinearLayout linearImage;
    public ImageView iv_complaint;
    private static String imageStoragePath="";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 500;
    public static String GALLERY_DIRECTORY_NAME = "Neelam Tea";
    public int IMAGE_MAX_SIZE = 1200000; // 1.2MP
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GALLERY_DIRECTORY_NAME=getResources().getResourceName(R.string.app_name);
        sessionManager=new SessionManager(this);
        sp_dealers=findViewById(R.id.sp_dealers);
        et_message=findViewById(R.id.et_message);
        btn_submit=findViewById(R.id.btn_submit);
        linearImage=findViewById(R.id.linearImage);
        iv_complaint=findViewById(R.id.iv_complaint);
        getDealerApi(sessionManager.getId());
        GpsProvider.onGPS(this);

        linearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });
        stateAdapter = new StateAdapter(DailyActivityReportActivity.this, mStrings,"Select Dealers");
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    //  lv_select_dealer_or_not.setVisibility(View.GONE);
                    dealer_id="";
                    //  lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                    //   lv_select_dealer_or_not.setVisibility(View.VISIBLE);
                    //dealer_id=dealerListItemModels.get(position-1).getId();

                    String dealerSplit= stateAdapter.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];



                    for (int i=0;i<dealerListItemModels.size();i++)
                    {
                        if (dealerListItemModels.get(i).getId().equals(splitDelarId))
                        {
                            dealer_id=dealerListItemModels.get(i).getId();
                            break;
                        }
                    }
                   /* lv_select_dealer.setVisibility(View.VISIBLE);
                    getCategorisApi(""+dealerListItemModels.get(position-1).getId());*/
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st_message=et_message.getText().toString();
                if (dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(DailyActivityReportActivity.this, "Please Select Dealer!", Toast.LENGTH_SHORT).show();
                }
                else if (st_message.equalsIgnoreCase(""))
                {
                    Toast.makeText(DailyActivityReportActivity.this, "Please Enter Message!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String images1="";
                    if (!imageStoragePath.isEmpty() || !imageStoragePath.equals(""))
                    {

                        String dir = null;
                        dir = String.valueOf(new File(imageStoragePath));
                        ImageCompress imageCompress = new ImageCompress();
                        imageCompress.compressImage(imageStoragePath, dir);
                        images1=dir;

                    }
                    getApiComplaint(sessionManager.getId(),dealer_id,st_message,images1);
                }

            }
        });





    }

    public void getSubmitApi(String id, String dealer_id, String st_message) {
        dealerListItemModels.clear();
        loadingView = new LoadingView(DailyActivityReportActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DailyActivityReportActivity.this);
        String url;
        url = getString(R.string.json_complaint);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        finish();

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DailyActivityReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dealer_id",""+dealer_id);
                params.put("emp_id",""+id);
                params.put("message",""+st_message);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void getDealerApi(String emp_id) {
        //spinnar
        dealerListItemModels.clear();
        loadingView = new LoadingView(DailyActivityReportActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DailyActivityReportActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<dealerListItemModels.size();i++)
                                {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

//                                    mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                }

                                stateAdapter = new StateAdapter(DailyActivityReportActivity.this, mStrings,"Select Dealers");
                                sp_dealers.setAdapter(stateAdapter);

                                for (int j=1;j<dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= stateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            sp_dealers.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(DailyActivityReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DailyActivityReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id",emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath().toString();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    /**
     * Alert dialog to navigate to app settings
     * to enable necessary permissions
     */
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(DailyActivityReportActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath);

                iv_complaint.setImageBitmap(bitmap);


                // successfully captured the image
                // display it in image view

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void getApiComplaint(String emp_id,String dealer_id,String st_message,String ta_attachment) {



        loadingView = new LoadingView(DailyActivityReportActivity.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("dealer_id",""+dealer_id);
            params.put("emp_id",""+emp_id);
            params.put("message",""+st_message);

            if (!ta_attachment.equalsIgnoreCase(""))
            {
                params.put("image", new File(ta_attachment));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String url;
        url = getString(R.string.json_complaint);
        client.post(""+url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                //  Log.d("respons",""+response.toString());
                Log.d("dd", "onResponse: "+response);
                loadingView.hideLoadingView();
                finish();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("error","dixit: "+res);

                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+res);
                errorView = new ErrorView(DailyActivityReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        });




    }

}
