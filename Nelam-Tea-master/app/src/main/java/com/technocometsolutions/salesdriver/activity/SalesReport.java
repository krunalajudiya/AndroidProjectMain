package com.technocometsolutions.salesdriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.common.StringUtil;
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
import com.technocometsolutions.salesdriver.RealPathUtil;
import com.technocometsolutions.salesdriver.activity.data.LoginDataSource;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewDailyReportAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesAdapter;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DailyReportModel;
import com.technocometsolutions.salesdriver.model.ViewDailyReportModel;
import com.technocometsolutions.salesdriver.utlity.CameraUtils;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.ImageCompress;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import cz.msebera.android.httpclient.Header;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class SalesReport extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public int IMAGE_MAX_SIZE = 1200000; // 1.2MP
    private static String imageStoragePath="";
    // Gallery directory name to store the images or videos
    public static String GALLERY_DIRECTORY_NAME = "Neelam Tea";
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    EditText date,shopname,personname,contactnumber,remark;
    public SearchableSpinner sp_city;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;
    CheckBox wall,gvt,dc,woodplank,fullbody,notinterested,paymentcollection,neworder,productdiscussion,formalmeeting,issue;
    RadioButton newclient,oldclient;
    Button visiting_card,submit;
    ImageView cardimage;
    String date_str,shopname_str,personname_str,contactnumber_str,remark_str,product_str,meeting_str,client_status,visiting_str,city_str;
    ArrayList<String> product_ar=new ArrayList<>();
    ArrayList<String> reason_ar=new ArrayList<>();
    public LoadingView loadingView;
    public ErrorView errorView;
    public SessionManager sessionManager;
    DailyReportModel dailyReportModel=new DailyReportModel();
    boolean dataeditvalue=false;
    Bundle bundle=new Bundle();
    Uri galleryimagepath;
    int position=0;

    public final int PERMISSION_CODE=1000;
    public final int PICK_FILE_REQUEST=1001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        shopname=findViewById(R.id.shop_name);
        personname=findViewById(R.id.person_name);
        contactnumber=findViewById(R.id.contact_number);
        remark=findViewById(R.id.remark);
        wall=findViewById(R.id.wall);
        gvt=findViewById(R.id.gvt);
        dc=findViewById(R.id.dc);
        newclient=findViewById(R.id.newclient);
        oldclient=findViewById(R.id.oldclient);
        woodplank=findViewById(R.id.woodplank);
        fullbody=findViewById(R.id.fullbody);
        notinterested=findViewById(R.id.notinterested);
        paymentcollection=findViewById(R.id.payment_collection);
        neworder=findViewById(R.id.newordercollection);
        productdiscussion=findViewById(R.id.productdiscuss);
        formalmeeting=findViewById(R.id.formal_meeting);
        issue=findViewById(R.id.issue);
        visiting_card=findViewById(R.id.addvisitngcard);
        cardimage=findViewById(R.id.cardimage);
        submit=findViewById(R.id.add_daily_report);
        sp_city=findViewById(R.id.d_city);
        date=findViewById(R.id.date);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        getCityApi(sessionManager.getId());


        if (getIntent().getBooleanExtra("editvalue",false)){
            int position=getIntent().getIntExtra("position",0);
            dataeditvalue=true;
            bundle=getIntent().getExtras();
            dailyReportModel.setD_id(bundle.getString("d_id"));
            dailyReportModel.setEmp_id(bundle.getString("emp_id"));
            dailyReportModel.setD_date(bundle.getString("d_date"));
            dailyReportModel.setClient_status(bundle.getString("client_status"));
            dailyReportModel.setShop_name(bundle.getString("shop_name"));
            dailyReportModel.setPerson_name(bundle.getString("person_name"));
            dailyReportModel.setContact_number(bundle.getString("contact_number"));
            dailyReportModel.setCity_name(bundle.getString("city_name"));
            dailyReportModel.setIn_product(bundle.getString("in_product"));
            dailyReportModel.setMeeting_reason(bundle.getString("meeting_reason"));
            dailyReportModel.setRemark(bundle.getString("remark"));
            dailyReportModel.setVisiting_card(bundle.getString("visiting_card"));
            setdata();


        }else {
        }



        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];
        date=findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog[0] = new DatePickerDialog(SalesReport.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog[0].show();
            }
        });

        sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                city_str=mStrings_city.get(position-1);
                Log.d("city_str",city_str);


                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        sp_city.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        visiting_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesReport.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(SalesReport.this).inflate(R.layout.image_chose, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                LinearLayout camerabtn,gallerybtn;
                camerabtn=dialogView.findViewById(R.id.camerabtn);
                gallerybtn=dialogView.findViewById(R.id.gallerybtn);

                camerabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position=0;
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            alertDialog.dismiss();

                            captureImage();
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                            alertDialog.dismiss();
                        }
                    }
                });

                gallerybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position=1;
                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                alertDialog.dismiss();
                                String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions,PERMISSION_CODE);
                            }else {
                                alertDialog.dismiss();
                                ChooseFile1();
                            }
                        }else {
                            alertDialog.dismiss();
                            ChooseFile1();
                        }
                    }
                });

                alertDialog.show();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
                if (dataeditvalue){
                    Log.d("card api",visiting_str);
                        updatedata(dailyReportModel.getD_id(),sessionManager.getId(),date_str,client_status,shopname_str,personname_str,contactnumber_str,city_str,product_str,meeting_str,remark_str,visiting_str);

                }else {
                    getApiexpense(sessionManager.getId(),date_str,client_status,shopname_str,personname_str,contactnumber_str,city_str,product_str,meeting_str,remark_str,visiting_str);
                }

            }
        });
    }
    public void getApiexpense(String emp_id, String date, String clientstatus, String shopname, String personname, String contactnumber, String city, String product, String reason, String remark,String visitingcard) {


        loadingView = new LoadingView(SalesReport.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("emp_id", emp_id);
            params.put("date", date);
            params.put("client_status", clientstatus);
            params.put("shop_name", shopname);
            params.put("person_name", personname);
            params.put("contact_number", contactnumber);
            params.put("city", city);
            params.put("in_product", product);
            params.put("meeting_reason", reason);
            params.put("remark", remark);
            //params.put("photo", new File(visitingcard));
            if (position==0){
                params.put("photo", new File(visitingcard));
            }else if (position==1){
                params.put("photo",new File(RealPathUtil.getRealPath(getApplicationContext(),galleryimagepath)));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


        String url;
        url = getString(R.string.json_add_daily_report);
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
                errorView = new ErrorView(SalesReport.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                        CameraUtils.openSettings(SalesReport.this);
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

//        Log.d("image data", String.valueOf(data.getData()));
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath);

                cardimage.setImageBitmap(bitmap);


                // successfully captured the image
                // display it in image view
                //previewCapturedImage();
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
        }
        if (resultCode==RESULT_OK && requestCode==PICK_FILE_REQUEST){

            Uri uri=data.getData();
            cardimage.setImageURI(uri);
            galleryimagepath=uri;
            Log.d("ff", String.valueOf(uri));
        }


        /*else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
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
        date_str=date.getText().toString().trim();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");


        if (newclient.isChecked()) {
            client_status ="1";
        }else if (oldclient.isChecked())
        {
            client_status="0";
        }
        shopname_str=shopname.getText().toString().trim();
        personname_str=personname.getText().toString().trim();
        contactnumber_str=contactnumber.getText().toString().trim();
        if (wall.isChecked())
        {
            product_ar.add(wall.getText().toString().trim());
        }
         if (gvt.isChecked())
        {
            product_ar.add(gvt.getText().toString().trim());
        }
         if (dc.isChecked())
        {
            product_ar.add(dc.getText().toString().trim());
        }
         if (woodplank.isChecked())
        {
            product_ar.add(woodplank.getText().toString().trim());
        }
         if (fullbody.isChecked())
        {
            product_ar.add(fullbody.getText().toString().trim());
        }
         if (notinterested.isChecked())
        {
            product_ar.add(notinterested.getText().toString().trim());
        }
         product_str=product_ar.stream().collect(Collectors.joining(","));


        if (paymentcollection.isChecked())
        {
            reason_ar.add(paymentcollection.getText().toString().trim());
        }
         if (neworder.isChecked())
        {
            reason_ar.add(neworder.getText().toString().trim());
        }
         if (productdiscussion.isChecked())
        {
            reason_ar.add(productdiscussion.getText().toString().trim());
        }
         if (formalmeeting.isChecked())
        {
            reason_ar.add(formalmeeting.getText().toString().trim());
        }
         if (issue.isChecked())
        {
            reason_ar.add(issue.getText().toString().trim());
        }
        meeting_str=reason_ar.stream().collect(Collectors.joining(","));
         remark_str=remark.getText().toString().trim();
        if (!imageStoragePath.isEmpty() || !imageStoragePath.equals(""))
        {
            String dir = null;
            dir ="" + new File(imageStoragePath);
            ImageCompress imageCompress = new ImageCompress();
            imageCompress.compressImage(imageStoragePath, dir);
            visiting_str=dir;

        }
        else{
            visiting_str="";
        }


    }
    public void getCityApi(String emp_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SalesReport.this);
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
                                    //Log.d("city",city_str);
                                    //Log.d("citycontain", String.valueOf(cityItemModelArrayList.get(2).getName()));
//                                    if (cityItemModelArrayList.contains(city_str)){
//                                        Log.d("index", String.valueOf(cityItemModelArrayList.indexOf(city_str)));
//                                        sp_city.setSelectedItem(cityItemModelArrayList.indexOf(city_str));
//                                    }


                                    for (int i=0;i<cityItemModelArrayList.size();i++)
                                    {

                                        String data = loginResponse.getItems().get(i).getName();
                                        mStrings_city.add(data);
//                                        Log.d("city",data);



//                                    mStrings_city.add(loginResponse.getItems().get(i).getName());
                                    }
                                    stateAdaptercity =  new StateAdapter(SalesReport.this, mStrings_city,dailyReportModel.getCity_name());
                                    sp_city.setAdapter(stateAdaptercity);
                                    if (dataeditvalue){
                                        for (int i=0;i<mStrings_city.size();i++){
                                            Log.d("ffcity",mStrings_city.get(i));
                                            //Log.d("comperto", String.valueOf(dailyReportModel.getCity_name().compareTo(String.valueOf(cityItemModelArrayList.get(i).getName()))));
                                            if (TextUtils.equals(city_str,mStrings_city.get(i))){
                                                //Log.d("city", String.valueOf(cityItemModelArrayList.indexOf(i)));

                                                sp_city.setSelectedItem(i+1);
                                                Log.d("ffff", "enter in if");
                                            }

                                        }
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
                                Toast.makeText(SalesReport.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void setdata(){

        date.setText(dailyReportModel.getD_date());
        shopname.setText(dailyReportModel.getShop_name());
        personname.setText(dailyReportModel.getPerson_name());
        contactnumber.setText(dailyReportModel.getContact_number());
        remark.setText(dailyReportModel.getRemark());
        city_str=dailyReportModel.getCity_name();

        Log.d("ffsize", String.valueOf(cityItemModelArrayList.size()));
        for (int i=0;i<cityItemModelArrayList.size();i++){
            Log.d("ffcity",dailyReportModel.getCity_name());
            if (dailyReportModel.getCity_name().compareTo(String.valueOf(cityItemModelArrayList.get(i).getName()))==0){
                sp_city.setSelectedItem(i);
                Log.d("ffff", "enter in if");
            }

        }
//        sp_city.setSelectedItem(dailyReportModelArrayList.get(position).get);
        Log.d("meting",dailyReportModel.getMeeting_reason());
        String metting[]=dailyReportModel.getMeeting_reason().split(",");
        String productins[]=dailyReportModel.getIn_product().split(",");

        for (String s:productins){
            if (s.compareTo(String.valueOf(wall.getText()))==0){
                wall.setChecked(true);
            }
            if (s.compareTo(String.valueOf(dc.getText()))==0){
                dc.setChecked(true);
            }
            if (s.compareTo(String.valueOf(fullbody.getText()))==0){
                fullbody.setChecked(true);
            }
            if (s.compareTo(String.valueOf(gvt.getText()))==0){
                gvt.setChecked(true);
            }
            if (s.compareTo(String.valueOf(woodplank.getText()))==0){
                woodplank.setChecked(true);
            }
            if (s.compareTo(String.valueOf(notinterested.getText()))==0){
                notinterested.setChecked(true);
            }
        }

        for (String s:metting){
            if(s.compareTo(String.valueOf(paymentcollection.getText()))==0){
                paymentcollection.setChecked(true);
            }
            if (s.compareTo(String.valueOf(productdiscussion.getText()))==0){
                productdiscussion.setChecked(true);
            }
            if (s.compareTo(String.valueOf(issue.getText()))==0){
                issue.setChecked(true);
            }
            if (s.compareTo(String.valueOf(neworder.getText()))==0){
                neworder.setChecked(true);
            }
            if (s.compareTo(String.valueOf(formalmeeting.getText()))==0){
                formalmeeting.setChecked(true);
            }
        }
        if(Integer.parseInt(dailyReportModel.getClient_status())==1){
            newclient.setChecked(true);
        }
        if (Integer.parseInt(dailyReportModel.getClient_status())==0){
            oldclient.setChecked(true);
        }
    }
    public void updatedata(String d_id,String emp_id, String date, String clientstatus, String shopname, String personname, String contactnumber, String city, String product, String reason, String remark,String visitingcard) {

        loadingView = new LoadingView(SalesReport.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        try {
            params.put("d_id", d_id);
            params.put("emp_id", emp_id);
            params.put("date", date);
            params.put("client_status", clientstatus);
            params.put("shop_name", shopname);
            params.put("person_name", personname);
            params.put("contact_number", contactnumber);
            params.put("city", city);
            params.put("in_product", product);
            params.put("meeting_reason", reason);
            params.put("remark", remark);
            if (position==0){
                params.put("photo", new File(visitingcard));
            }else if (position==1){
                params.put("photo",new File(RealPathUtil.getRealPath(getApplicationContext(),galleryimagepath)));
            }


        Log.d("shop_name",d_id+" "+emp_id+" "+date+" "+clientstatus+" "+shopname+" "+personname+" "+contactnumber+" "+
                " "+city+" "+product+" "+reason+" "+remark+" "+visitingcard);
//            new File(visitingcard)
    } catch (Exception e) {
        e.printStackTrace();
    }

        String url;
        url = getString(R.string.json_edit_daily_report);
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
                errorView = new ErrorView(SalesReport.this, new ErrorView.OnNoInternetConnectionListerner() {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_chose);

//        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//        text.setText(msg);
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }

    private void ChooseFile1() {
        Intent intent = new  Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);

    }

}