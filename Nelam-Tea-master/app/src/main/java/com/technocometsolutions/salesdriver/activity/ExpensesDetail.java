package com.technocometsolutions.salesdriver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.technocometsolutions.salesdriver.adapter.ViewExpensesAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesModel;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.ExpensesModel;
import com.technocometsolutions.salesdriver.model.SubTransportItemModel;
import com.technocometsolutions.salesdriver.model.SubTransportModel;
import com.technocometsolutions.salesdriver.utlity.CameraUtils;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.ImageCompress;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class ExpensesDetail extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    public int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public int IMAGE_MAX_SIZE = 1200000; // 1.2MP
    private static String imageStoragePath="";
    // Gallery directory name to store the images or videos
    public static String GALLERY_DIRECTORY_NAME = "Neelam Tea";
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    EditText dateofexpense,distance,tolltax,breakfastcharge,lunch_charge,dinner_charge,da,mis_charge,remark,hotelstay,fromcitytxt,tocitytxt,modeoftransfertxt;
    ImageView bill_image;
    Button add_expense;
    String date_str,distance_str,toll_str,transport_str,breakfast_str,lunch_str,dinner_str,da_str,mis_str,remark_str,bill_str,fromcity_str,tocity_str,hotelstay_str;
    final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR); // current year
    int mMonth = c.get(Calendar.MONTH); // current month
    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
    SearchableSpinner modeoftransfer;
    ArrayList<String> transfermodelist=new ArrayList<>(Arrays.asList("Bike","Care","Other"));
    public ArrayList<SubTransportItemModel> subTransportItemModels = new ArrayList<>();
    private final ArrayList<String> mStrings_public_transport = new ArrayList<>();
    public LoadingView loadingView;
    public ErrorView errorView;
    StateAdapter transportadapter;
    public SessionManager sessionManager;
    ExpensesModel expensesModel=new ExpensesModel();
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ExpensesDetail.this);
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;
    public SearchableSpinner fromcity,tocity;
    Boolean editvalueornot=false;
    Bundle bundle=new Bundle();
    LinearLayout layoutaddmore;
    TextView addmoreimage;
    ImageView imageView;
    int idconst=1000;
    int currentclickedid;
    List<File> fileList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GpsProvider.onGPS(this);
        sessionManager = new SessionManager(this);
        dateofexpense=findViewById(R.id.dateofexpenses);
        modeoftransfer=findViewById(R.id.modeoftransfer);
        distance=findViewById(R.id.distance);
        tolltax=findViewById(R.id.tollcharge);
        breakfastcharge=findViewById(R.id.breakfastcharge);
        lunch_charge=findViewById(R.id.lunchcharge);
        dinner_charge=findViewById(R.id.dinnercharges);
        da=findViewById(R.id.da);
        mis_charge=findViewById(R.id.mis_charge);
        remark=findViewById(R.id.remark);
        bill_image=findViewById(R.id.bill_btn);
        add_expense=findViewById(R.id.add_expense);
        fromcity=findViewById(R.id.fromcity);
        tocity=findViewById(R.id.tocity);
        hotelstay=findViewById(R.id.hotelstaycharge);
        layoutaddmore=findViewById(R.id.layoutaddmore);
        addmoreimage=findViewById(R.id.addmoreimage);
        fromcitytxt=findViewById(R.id.fromcity_txt);
        tocitytxt=findViewById(R.id.tocity_txt);
        modeoftransfertxt=findViewById(R.id.modeoftransfer_txt);



        /*addmoreimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resourceid=getResources().getIdentifier("ic_photo_upload","drawable",getPackageName());
//                if (==R.drawable.ic_photo_upload){
//
//                }else{
//
//                }

                addmoreimagelayout();
                Log.d("resourceid", String.valueOf(bill_image.getDrawable()));
                Log.d("ff",String.valueOf(imageView.getDrawable()));


            }
        });*/
        bill_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });
        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
//                if (editvalueornot){
//                    updatedata(expensesModel.getE_id(),sessionManager.getId(),date_str,transport_str,distance_str,toll_str,breakfast_str,lunch_str,dinner_str,da_str,mis_str,remark_str,bill_str);
//                }else {
                    getApiexpense(sessionManager.getId(),date_str,transport_str,distance_str,toll_str,breakfast_str,lunch_str,dinner_str,da_str,mis_str,remark_str);
//                }


            }
        });
        getCityApi(sessionManager.getId());
        stateAdaptercity =  new StateAdapter(ExpensesDetail.this, mStrings_city,"Select City");
        fromcity.setAdapter(stateAdaptercity);
        fromcity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                fromcity_str=mStrings_city.get(position-1);
                Log.d("from_city",fromcity_str);


                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        fromcity.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        getCityApi(sessionManager.getId());
        stateAdaptercity =  new StateAdapter(ExpensesDetail.this, mStrings_city,"Select City");
        tocity.setAdapter(stateAdaptercity);
        tocity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                tocity_str=mStrings_city.get(position-1);


                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        tocity.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        getTransportPublic();
        transportadapter =  new StateAdapter(ExpensesDetail.this, mStrings_public_transport,"Select Transport");
        modeoftransfer.setAdapter(transportadapter);


        modeoftransfer.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position!=0){
                    transport_str=subTransportItemModels.get(position-1).getId();
                    Log.d("ssss1", String.valueOf(subTransportItemModels.get(position-1).getId()));
                }


            }

            @Override
            public void onNothingSelected() {

            }
        });
        modeoftransfer.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        dateofexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // date picker dialog
                datePickerDialog[0] = new DatePickerDialog(ExpensesDetail.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateofexpense.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog[0].show();
            }
        });

        if (getIntent().getBooleanExtra("edit_expense",false)){
            int position=getIntent().getIntExtra("position",0);
            editvalueornot=true;
            add_expense.setVisibility(View.GONE);
            modeoftransfer.setVisibility(View.GONE);
            fromcity.setVisibility(View.GONE);
            tocity.setVisibility(View.GONE);
            addmoreimage.setVisibility(View.GONE);
            bill_image.setVisibility(View.GONE);

            fromcitytxt.setVisibility(View.VISIBLE);
            tocitytxt.setVisibility(View.VISIBLE);
            modeoftransfertxt.setVisibility(View.VISIBLE);

            dateofexpense.setEnabled(false);
            distance.setEnabled(false);
            hotelstay.setEnabled(false);
            tolltax.setEnabled(false);
            breakfastcharge.setEnabled(false);
            lunch_charge.setEnabled(false);
            dinner_charge.setEnabled(false);
            da.setEnabled(false);
            mis_charge.setEnabled(false);
            remark.setEnabled(false);
            fromcitytxt.setEnabled(false);
            tocitytxt.setEnabled(false);
            modeoftransfertxt.setEnabled(false);


            bundle=getIntent().getExtras();
            Log.d("datatat",bundle.getString("e_id"));
            expensesModel.setE_id(bundle.getString("e_id"));
            expensesModel.setEmp_id(bundle.getString("emp_id"));
            expensesModel.setE_date(bundle.getString("e_date"));
            expensesModel.setE_from(bundle.getString("e_from"));
            expensesModel.setE_to(bundle.getString("e_to"));
            Log.d("tocity",expensesModel.getE_from());
            expensesModel.setHotel_stay(bundle.getString("hotel_stay"));
            expensesModel.setT_id(bundle.getString("t_id"));
            expensesModel.setDistance(bundle.getString("distance"));
            expensesModel.setToll_charge(bundle.getString("toll_charge"));
            expensesModel.setBreakfast_charge(bundle.getString("breakfast_charge"));
            expensesModel.setLunch_charge(bundle.getString("lunch_charge"));
            expensesModel.setDinner_charge(bundle.getString("dinner_charge"));
            expensesModel.setDa(bundle.getString("da"));
            expensesModel.setMis_charge(bundle.getString("mis_charge"));
            expensesModel.setRemarks(bundle.getString("remarks"));
            expensesModel.setBill(bundle.getString("bill"));
            setdataforedit();
            Log.d("position", "");

        }else {
            dateofexpense.setText(""+mDay+"/"+(mMonth+1)+"/"+mYear);
        }


    }
    public void addmoreimagelayout(){
//        LinearLayout linearLayout=new LinearLayout(getApplicationContext());
//        LinearLayout.LayoutParams linearlayoutparams=
//                new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        linearLayout.setLayoutParams(linearlayoutparams);
        imageView=new ImageView(getApplicationContext());
        LinearLayout.LayoutParams imageviewparams=
                new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(100, 100));
        imageView.setId(idconst++);
        imageView.setLayoutParams(imageviewparams);
        imageView.setImageResource(R.drawable.ic_photo_upload);
        layoutaddmore.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ff", String.valueOf(imageView.getId()));
                captureImage();

            }
        });
        Log.d("imageid", String.valueOf(imageView.getId()));
    }
    public void getApiexpense(String emp_id, String date, String transfer, String distance, String toll, String breakfast, String lunch, String dinner, String da, String mis,String remark) {


        loadingView = new LoadingView(ExpensesDetail.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 60 * 1000;
        client.setTimeout(DEFAULT_TIMEOUT);
        RequestParams params = new RequestParams();

        try {
            params.put("emp_id", emp_id);
            params.put("date", date);
            params.put("start_time", "");
            params.put("end_time", "");
            params.put("from",fromcity_str);
            params.put("to",tocity_str);
            params.put("hotel_stay",hotelstay_str);
            params.put("transport_mode", transfer);
            params.put("distance", distance);
            params.put("toll_charge", toll);
            params.put("breakfast_charge", breakfast);
            params.put("lunch_charge", lunch);
            params.put("dinner_charge", dinner);
            params.put("da", da);
            params.put("mis_charge", mis);
            params.put("remark", remark);
            //params.put("photo", new File(bill_str));


            if (fileList.size()==0){
                Log.d("file_size", String.valueOf(fileList.size()));
                //File file_ar = null;
                //params.put("photo[]",file_ar);
                //params.put("photo[]",new File[0]);
            }else {
                File[] file_ar=new File[fileList.size()];
                file_ar=fileList.toArray(file_ar);
                Log.d("file_ar", String.valueOf(file_ar.length));
                params.put("photo[]",file_ar);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        String url;
        url = getString(R.string.json_add_daily_expense);
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
                errorView = new ErrorView(ExpensesDetail.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                Log.d("errorshow","error");
                errorView.showLoadingView();

            }
        });




    }
    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    /**
     * Requesting permissions using Dexter library
     */
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
                        CameraUtils.openSettings(ExpensesDetail.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath);
                if (!imageStoragePath.isEmpty() || !imageStoragePath.equals(""))
                {
                    String dir = null;
                    dir ="" + new File(imageStoragePath);
                    ImageCompress imageCompress = new ImageCompress();
                    imageCompress.compressImage(imageStoragePath, dir);
                    bill_str=dir;
                    fileList.add(new File(bill_str));
                    addmoreimage.setText(fileList.size()+" Bill Uploaded");
                    Log.d("count", String.valueOf(fileList.get(0).getPath()));

                }
                    //bill_image.setImageBitmap(bitmap);





                // successfully captured the image
                // display it in image view
                //previewCapturedImage();
                CAMERA_CAPTURE_IMAGE_REQUEST_CODE++;
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
                imageStoragePath="";
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
                imageStoragePath="";
            }
        } /*else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
                // previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }*/

        /*else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath4);

                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath4);

                ivotheramount.setImageBitmap(bitmap);


                // successfully captured the image
                // display it in image view
               // previewCapturedImage4();
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
            }*/

    }
    public void getvalue()
    {
        date_str=dateofexpense.getText().toString().trim();
        distance_str=distance.getText().toString().trim();
        toll_str=tolltax.getText().toString().trim();
        breakfast_str=breakfastcharge.getText().toString().trim();
        lunch_str=lunch_charge.getText().toString().trim();
        dinner_str=dinner_charge.getText().toString().trim();
        da_str=da.getText().toString().trim();
        mis_str=mis_charge.getText().toString().trim();
        remark_str=remark.getText().toString().trim();
        hotelstay_str=hotelstay.getText().toString().trim();
       /* if (!imageStoragePath.isEmpty() || !imageStoragePath.equals(""))
        {
            String dir = null;
            dir ="" + new File(imageStoragePath);
            ImageCompress imageCompress = new ImageCompress();
            imageCompress.compressImage(imageStoragePath, dir);
            bill_str=dir;

        }
        else{
            bill_str="";
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getTransportPublic() {
        subTransportItemModels.clear();
        mStrings_public_transport.clear();
        loadingView = new LoadingView(ExpensesDetail.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(ExpensesDetail.this);
        String url;
        url = getString(R.string.json_all_transport);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();

                        /*try {
                            JSONArray jsonElements=response.getJSONArray("items");
                            for (int i=0;i<jsonElements.length();i++)
                            {
                                //baki
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // JsonObject jsonObject=r
                        Gson gson = new Gson();
                        SubTransportModel loginResponse = gson.fromJson(response, SubTransportModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {

                                subTransportItemModels = loginResponse.getItems();
                                for (int i=0;i<subTransportItemModels.size();i++)
                                {
                                    mStrings_public_transport.add(loginResponse.getItems().get(i).getName());

                                }
                                Log.d("ff", String.valueOf(mStrings_public_transport));
                                if (getIntent().getBooleanExtra("edit_expense",false)){
                                    modeoftransfertxt.setText(subTransportItemModels.get(Integer.parseInt(expensesModel.getT_id())).getName());
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(ExpensesDetail.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(ExpensesDetail.this, new ErrorView.OnNoInternetConnectionListerner() {
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

                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCityApi(String emp_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(ExpensesDetail.this);
        String url;
        url = getString(R.string.json_all_city);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
//                        loadingView.hideLoadingView();

                        /*try {
                            JSONArray jsonElements=response.getJSONArray("items");
                            for (int i=0;i<jsonElements.length();i++)
                            {
                                //baki
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // JsonObject jsonObject=r

                        try {
                            Gson gson = new Gson();
                            CityModel loginResponse = gson.fromJson(response, CityModel.class);
                            if (loginResponse.getSuccess())
                            {
                                if (loginResponse.getItems() != null) {
                                    cityItemModelArrayList = loginResponse.getItems();
                                    for (int i=0;i<cityItemModelArrayList.size();i++)
                                    {

                                        String data = loginResponse.getItems().get(i).getName();
                                        mStrings_city.add(data);

//                                    mStrings_city.add(loginResponse.getItems().get(i).getName());
                                    }

                               /* dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);*/


                                }
                            }
                            else
                            {
                                Toast.makeText(ExpensesDetail.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {

                        }

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("state_id", state_id);
                //params.put("password", password);
                params.put("emp_id",emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    void updatedata(String e_id,String emp_id, String date, String transfer, String distance, String toll, String breakfast, String lunch, String dinner, String da, String mis,String remark,String bill)
    {

        loadingView = new LoadingView(ExpensesDetail.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("e_id",e_id);
            params.put("emp_id", emp_id);
            params.put("date", date);
            params.put("start_time", "");
            params.put("end_time", "");
            params.put("from",fromcity_str);
            params.put("to",tocity_str);
            params.put("hotel_stay",hotelstay_str);
            params.put("transport_mode", transfer);
            params.put("distance", distance);
            params.put("toll_charge", toll);
            params.put("breakfast_charge", breakfast);
            params.put("lunch_charge", lunch);
            params.put("dinner_charge", dinner);
            params.put("da", da);
            params.put("mis_charge", mis);
            params.put("remark", remark);
            params.put("photo", new File(bill_str));






        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String url;
        url = getString(R.string.json_edit_expense);
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
                errorView = new ErrorView(ExpensesDetail.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    void setdataforedit(){
        dateofexpense.setText(expensesModel.getE_date());
        modeoftransfer.setSelectedItem(expensesModel.getT_id());
        distance.setText(expensesModel.getDistance());
        hotelstay.setText(expensesModel.getHotel_stay());
        tolltax.setText(expensesModel.getToll_charge());
        breakfastcharge.setText(expensesModel.getBreakfast_charge());
        lunch_charge.setText(expensesModel.getLunch_charge());
        dinner_charge.setText(expensesModel.getDinner_charge());
        da.setText(expensesModel.getDa());
        mis_charge.setText(expensesModel.getMis_charge());
        remark.setText(expensesModel.getRemarks());
        fromcitytxt.setText(expensesModel.getE_from());
        tocitytxt.setText(expensesModel.getE_to());
        //mode of transport set in getTransportPublic()




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}