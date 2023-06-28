package com.technocometsolutions.salesdriver.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.ExpensesTypeItemModel;
import com.technocometsolutions.salesdriver.model.ExpensesTypeModel;
import com.technocometsolutions.salesdriver.model.RouteItemModel;
import com.technocometsolutions.salesdriver.model.RouteModel;
import com.technocometsolutions.salesdriver.model.StateItemModel;
import com.technocometsolutions.salesdriver.model.StateModel;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class SaveexActivity extends AppCompatActivity {
    EditText /*et_public,et_food_bil,et_night_halt,et_other,et_date,*/
            et_date, etTravelKm, etTaamount, etdaamount, etotheramount, etRemark;

    TextView tv_public, tv_food_bil, tv_night_halt, tv_other, ettotalamount, tvtaamount, tvdaamount, tvotheramount;
    Button btn_Save;
    SearchableSpinner sp_expense, sp_transport, sp_state, sp_city, sp_transport_public, sp_dealer, sp_route;

    ImageView iv_taamount, ivdaamout, ivotheramount;

    int a = 0;
    int b = 0;
    int c = 0;
    int travel_km = 0;
    int travel_km_final = 0;
    Double r = 0.0;


    private final ArrayList<String> mStrings_public_transport = new ArrayList<>();
    private final ArrayList<String> mStrings_expense = new ArrayList<>();
    int day, month, year;
    int dayFinal, monthFinal, yearFinal;
    public StateAdapter stateAdapter;
    public StateAdapter stateAdapter_public_transport;
    public StateAdapter stateAdapter_expense;
    public String transport_id = "";
    public String transport_public_id = "";
    public String expense_id = "", state_id = "";
    public LinearLayout lv_da_updown, lv_public_transport;
    public SessionManager sessionManager;
    public ArrayList<ExpensesTypeItemModel> expensesTypeItemModels = new ArrayList<>();
    public ArrayList<SubTransportItemModel> subTransportItemModels = new ArrayList<>();
    public LoadingView loadingView;
    public ErrorView errorView;
    public int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public String userChoosenTask;

    public static final int MEDIA_TYPE_IMAGE = 1;
    // Activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 = 300;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE3 = 400;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4 = 500;


    //images call
    public ImageView iv_publictrasport,iv_foodbill,iv_nighthalt,iv_otharimages;

    public int IMAGE_MAX_SIZE = 1200000; // 1.2MP

    private static String imageStoragePath="";
    private static String imageStoragePath2="";
    private static String imageStoragePath3="";
    private static String imageStoragePath4="";
    public String city_id = "";
    // Gallery directory name to store the images or videos
    public static String GALLERY_DIRECTORY_NAME = "Neelam Tea";
    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public StateAdapter stateAdapterstate;
    public StateAdapter stateAdaptercity;
    private final ArrayList<String> mStrings_state = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public ArrayList<StateItemModel> stateItemModelArrayList = new ArrayList<>();
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();


    public String dealer_id = "";
    public String route_id = "";
    public SearchableSpinner sp_dealers;


    public StateAdapter routestateAdapter;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private final ArrayList<String> routeStringList = new ArrayList<>();
    public SearchableSpinner spRoute;
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    private ArrayList<RouteItemModel> routeItemModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveex);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GALLERY_DIRECTORY_NAME = getResources().getResourceName(R.string.app_name);
        sessionManager = new SessionManager(this);
        /*iv_publictrasport= findViewById(R.id.iv_publictrasport);
        iv_foodbill= findViewById(R.id.iv_foodbill);
        iv_nighthalt= findViewById(R.id.iv_nighthalt);
        iv_otharimages= findViewById(R.id.iv_otharimages);*/


        GpsProvider.onGPS(this);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }*/
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


      /*  lv_da_updown=findViewById(R.id.lv_da_updown);
        lv_public_transport=findViewById(R.id.lv_public_transport);
*/


      /*  et_public=findViewById(R.id.et_public);
        et_food_bil=findViewById(R.id.et_food_bil);
        et_night_halt=findViewById(R.id.et_night_halt);
        et_other=findViewById(R.id.et_other);

        tv_public=findViewById(R.id.tv_public);
        tv_food_bil=findViewById(R.id.tv_food_bil);
        tv_night_halt=findViewById(R.id.tv_night_halt);
        tv_other=findViewById(R.id.tv_other);*/
        // et_document=findViewById(R.id.et_document);

        et_date = findViewById(R.id.et_date);
        etdaamount = findViewById(R.id.et_daamount);
        tvdaamount = findViewById(R.id.tv_daamount);
        ettotalamount = findViewById(R.id.tv_totalamount);
        etTaamount = findViewById(R.id.et_taamount);
        tvtaamount = findViewById(R.id.tv_taamount);
        etTravelKm = findViewById(R.id.et_travelkm);
        etRemark = findViewById(R.id.et_remarks_if_any);
        etotheramount = findViewById(R.id.et_otheramount);
        tvotheramount = findViewById(R.id.tv_otheramount);


        ivdaamout = findViewById(R.id.iv_daamount);
        iv_taamount = findViewById(R.id.iv_taamount);
        iv_otharimages = findViewById(R.id.iv_otheramount);


        sp_dealers = findViewById(R.id.sp_dealers);
        getDealerApi();

        spRoute = findViewById(R.id.sp_route);
        stateAdapter = new StateAdapter(SaveexActivity.this, mStrings, "Select Dealers");
        sp_dealers.setAdapter(stateAdapter);


        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    dealer_id = "";
                    //lv_select_dealer.setVisibility(View.GONE);
                    //             spRetailer.setVisibility(View.GONE);
                    spRoute.setVisibility(View.GONE);
                } else {
                    //dealer_id=dealerListItemModels.get(position-1).getId();
                    //lv_select_dealer.setVisibility(View.VISIBLE);
                    //getCategorisApi(""+dealer_id);
                    String dealerSplit = stateAdapter.getItem(position).toString();
                    String[] lastFinalId = dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length - 1];

                    for (int i = 0; i < dealerListItemModels.size(); i++) {
                        if (dealerListItemModels.get(i).getId().equals(splitDelarId)) {
                            dealer_id = dealerListItemModels.get(i).getId();
                            Log.d("abc_id", splitDelarId);
                            Log.d("abc_id", dealer_id);
                            break;
                        }
                    }
                    spRoute.setVisibility(View.VISIBLE);
                    getRouteList(sessionManager.getId(), dealer_id);

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        routestateAdapter = new StateAdapter(SaveexActivity.this, routeStringList, "Select Route");
        spRoute.setAdapter(routestateAdapter);
        spRoute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    route_id = "";
                    //spRetailer.setVisibility(View.GONE);
                    //lv_select_dealer.setVisibility(View.GONE);
                } else {
                    //spRetailer.setVisibility(View.VISIBLE);
//                    route_id=routeItemModelArrayList.get(position-1).getId();
                    String dealerSplit = routestateAdapter.getItem(position).toString();
                    String[] lastFinalId = dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length - 1];


                    for (int i = 0; i < routeItemModelArrayList.size(); i++) {
                        if (routeItemModelArrayList.get(i).getId().equals(splitDelarId)) {
                            route_id = routeItemModelArrayList.get(i).getId();
                            break;
                        }
                    }
                    Log.d("RouteID", "" + route_id + " Delaer14:" + sessionManager.getId() + " Delaer:" + dealer_id);
                    //lv_select_dealer.setVisibility(View.VISIBLE);
                    //getRetailerList(sessionManager.getId(),dealer_id,route_id);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        tvtaamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });

        tvdaamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage2();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });

        tvotheramount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage3();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });

        /*tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage4();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });*/


        btn_Save = findViewById(R.id.btn_save);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    /*if (etTaamount.getEditableText().toString().trim().length()==0)
                    {*/
                    if (etTaamount.getEditableText().toString().trim().length() == 0) {
                        a = 0;
                    } else {
                        a = Integer.parseInt(etTaamount.getText().toString());
                    }

                    if (etTravelKm.getEditableText().toString().trim().length() == 0) {
                        travel_km = 0;
                        travel_km_final=0;
                    } else {
                        travel_km = Integer.parseInt(etTravelKm.getText().toString());
                        int km=Integer.parseInt(sessionManager.getKeyChargePerKm());
                        travel_km_final=travel_km*km;

                    }

                    if (etdaamount.getEditableText().toString().trim().length() == 0) {
                        b = 0;
                    } else {
                        b = Integer.parseInt(etdaamount.getText().toString());
                    }
                    if (etotheramount.getEditableText().toString().trim().length() == 0) {
                        c = 0;
                    } else {
                        c = Integer.parseInt(etotheramount.getText().toString());
                    }


                    Log.e("val1", "" + a + b + c);
                    r = Double.parseDouble("" + (a + b + c + travel_km_final));
                    ettotalamount.setText(String.valueOf(r));


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etTaamount.addTextChangedListener(textWatcher);
        etdaamount.addTextChangedListener(textWatcher);
        etotheramount.addTextChangedListener(textWatcher);
        etTravelKm.addTextChangedListener(textWatcher);


        //getExpenseApi(sessionManager.getId());
        ///sp_expense=findViewById(R.id.sp_expense);


        //sp_state=findViewById(R.id.sp_state);
        //sp_city=findViewById(R.id.sp_city);
        //getStateApi();
        /*stateAdapterstate = new StateAdapter(SaveexActivity.this, mStrings_state,"Select State");
        sp_state.setAdapter(stateAdapterstate);

        //stateAdaptercity = new StateAdapter(SaveexActivity.this, mStrings_city,"Select City");
        //sp_city.setAdapter(stateAdaptercity);


        sp_city.setVisibility(View.GONE);
        sp_state.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {

                //sp_city.setVisibility(View.VISIBLE);

                if (position==0)
                {
                    city_id="";
                    state_id="";
                    sp_city.setVisibility(View.GONE);
                    // Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
                *//*else if (position==stateItemModelArrayList.size())
                {
                    Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position-1).getId(), Toast.LENGTH_SHORT).show();
                }*//*
                else
                {
                    sp_city.setVisibility(View.VISIBLE);
                    String dealerSplit=stateAdapterstate.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];

                    for (int i=0;i<stateItemModelArrayList.size();i++)
                    {
                        if (stateItemModelArrayList.get(i).getId().equals(splitDelarId))
                        {
                            state_id=stateItemModelArrayList.get(i).getId();
                            break;
                        }
                    }
                    Log.d("CityID",":cc"+state_id);
                    getCityApi(state_id);
                    stateAdaptercity =  new StateAdapter(SaveexActivity.this, mStrings_city,"Select City");
                    sp_city.setAdapter(stateAdaptercity);
                    //Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position-1).getId(), Toast.LENGTH_SHORT).show();
                }
                //getCityApi(stateItemModelArrayList.get(position-1).getId());
                // stateAdaptercity =  new StateAdapter(DealerServeReportActivity.this, mStrings_city);
                // sp_city.setAdapter(stateAdaptercity);
            }

            @Override
            public void onNothingSelected() {
                sp_city.setVisibility(View.GONE);
            }
        });
        sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    city_id="";
                }
                else
                {
                    //city_id=cityItemModelArrayList.get(position-1).getId();
                    String dealerSplit= stateAdaptercity.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];

                    for (int i=0;i<cityItemModelArrayList.size();i++)
                    {
                        if (cityItemModelArrayList.get(i).getId().equals(splitDelarId))
                        {
                            city_id=cityItemModelArrayList.get(i).getId();
                            break;
                        }
                    }
                }

                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        sp_state.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        sp_city.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });*/


        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SaveexActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearFinal = year;
                        monthFinal = month + 1;
                        dayFinal = dayOfMonth;
                        et_date.setText("" + dayFinal + "-" + monthFinal + "-" + yearFinal);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        //sp_transport=findViewById(R.id.sp_transport);
        //sp_transport_public=findViewById(R.id.sp_transport_public);
       /* stateAdapter_expense = new StateAdapter(SaveexActivity.this, mStrings_expense,"Select Expense");
        sp_expense.setAdapter(stateAdapter_expense);
        sp_expense.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
             if (position==0)
             {
                 expense_id="";
                 lv_da_updown.setVisibility(View.GONE);
                 sp_transport.setVisibility(View.GONE);
             }
             else if (position==1){
                 expense_id=expensesTypeItemModels.get(position-1).getId();
                 lv_da_updown.setVisibility(View.GONE);
                 sp_transport.setVisibility(View.GONE);
             }
             else if (position==3){
                 expense_id=expensesTypeItemModels.get(position-1).getId();
                 lv_da_updown.setVisibility(View.GONE);
                 sp_transport.setVisibility(View.GONE);
             }

             else {
                 expense_id=expensesTypeItemModels.get(position-1).getId();
                 lv_da_updown.setVisibility(View.VISIBLE);
                 sp_transport.setVisibility(View.VISIBLE);
             }
            }
            @Override
            public void onNothingSelected() {

            }
        });

        lv_da_updown.setVisibility(View.GONE);
        lv_public_transport.setVisibility(View.GONE);
        sp_transport.setVisibility(View.GONE);
        getStaticTransport();

        stateAdapter_public_transport = new StateAdapter(SaveexActivity.this, mStrings_public_transport,"Select Transport");
        sp_transport_public.setAdapter(stateAdapter_public_transport);
        sp_transport_public.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {


                    transport_public_id="";

                }
                else
                {

                    transport_public_id=subTransportItemModels.get(position-1).getId();

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


        sp_transport_public.setVisibility(View.GONE);
        //stateAdapter = new StateAdapter(SaveexActivity.this, mStrings,"Select Transport");
        sp_transport.setAdapter(stateAdapter);
        sp_transport.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    transport_id="";
                    lv_public_transport.setVisibility(View.GONE);
                    sp_transport_public.setVisibility(View.GONE);
                }
                else if (position==1)
                {
                    transport_id="1";
                    lv_public_transport.setVisibility(View.VISIBLE);
                    sp_transport_public.setVisibility(View.VISIBLE);
                    getTransportPublic(transport_id);

                }

                else
                {
                    transport_id="2";
                    lv_public_transport.setVisibility(View.GONE);
                    sp_transport_public.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });*/
        /*et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SaveexActivity.this, SaveexActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });*/


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dealer_id.equalsIgnoreCase("")) {
                    Toast.makeText(SaveexActivity.this, "Please Select Dealer", Toast.LENGTH_SHORT).show();
                } else if (route_id.equalsIgnoreCase("")) {
                    Toast.makeText(SaveexActivity.this, "Please Select Route", Toast.LENGTH_SHORT).show();
                } else if (et_date.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SaveexActivity.this, "Please Select Date !", Toast.LENGTH_SHORT).show();
                }
                /*else if (expense_id.equalsIgnoreCase("1"))
                {
                    getApiexpense1(sessionManager.getId(),et_date.getText().toString(),expense_id);
                }*/
                else
                {
                    String images1="",images2="",images3="",images4="";

                   /* String dir = null;
                    dir ="" + new File(imageStoragePath);
                    ImageCompress imageCompress = new ImageCompress();
                    imageCompress.compressImage(imageStoragePath, dir);
                    images1=dir;*/

                    if (!imageStoragePath.isEmpty() || !imageStoragePath.equals(""))
                    {
                        String dir = null;
                        dir ="" + new File(imageStoragePath);
                        ImageCompress imageCompress = new ImageCompress();
                        imageCompress.compressImage(imageStoragePath, dir);
                        images1=dir;

                    }
                    if (!imageStoragePath2.isEmpty() || !imageStoragePath2.equals(""))
                    {
                        String dir2 = null;
                        dir2 ="" + new File(imageStoragePath2);
                        ImageCompress imageCompress2 = new ImageCompress();
                        imageCompress2.compressImage(imageStoragePath2, dir2);
                        images2=dir2;

                    }
                    if (!imageStoragePath3.isEmpty() || !imageStoragePath3.equals(""))
                    {
                        String dir3 = null;
                        dir3 ="" + new File(imageStoragePath2);
                        ImageCompress imageCompress3 = new ImageCompress();
                        imageCompress3.compressImage(imageStoragePath3, dir3);

                        images3=dir3;

                    }
                    /*if (!imageStoragePath4.isEmpty() || !imageStoragePath4.equals(""))
                    {
                        String dir4 = null;
                        dir4 ="" + new File(imageStoragePath4);
                        ImageCompress imageCompress4 = new ImageCompress();
                        imageCompress4.compressImage(imageStoragePath4, dir4);
                        images4=dir4;

                    }
*/


                    String date = et_date.getText().toString();
                    String remark = etRemark.getText().toString();
                    String travelkm = etTravelKm.getText().toString();
                    String taamount = etTaamount.getText().toString();
                    String daamount = etdaamount.getText().toString();
                    String otheramount = etotheramount.getText().toString();
                    String totalamount = ettotalamount.getText().toString();

                    getApiexpense(sessionManager.getId(), date, dealer_id, route_id, travelkm, taamount, images1, daamount, images2, otheramount, images3, remark);
                }


            }
        });
    }

    public void getTransportPublic(String transport_id) {
        subTransportItemModels.clear();
        mStrings_public_transport.clear();
        loadingView = new LoadingView(SaveexActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_transport);
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

                            }
                        }
                        else
                        {
                            Toast.makeText(SaveexActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("transport_type", transport_id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getApiexpense(String emp_id, String date, String dealer_id, String route_id, String km, String ta_amount, String ta_attachment, String da_amount, String da_attachment, String other_amount, String other_attachment, String remark) {


        loadingView = new LoadingView(SaveexActivity.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("emp_id", emp_id);
            params.put("date", date);
            params.put("dealer_id", dealer_id);
            params.put("route_id", route_id);
            params.put("km", km);

           /* if (transport_type.equalsIgnoreCase("1"))
            {

            }*/
            if (!ta_amount.equalsIgnoreCase("")) {
                params.put("ta", ta_amount);
            }
            if (!ta_attachment.equalsIgnoreCase("")) {
                params.put("ta_attachment", new File(ta_attachment));
            }
            if (!da_amount.equalsIgnoreCase("")) {
                params.put("da", da_amount);
            }
            if (!da_attachment.equalsIgnoreCase("")) {
                params.put("da_attachment", new File(da_attachment));
            }
            if (!other_amount.equalsIgnoreCase("")) {
                params.put("other", other_amount);
            }
            if (!other_attachment.equalsIgnoreCase("")) {
                params.put("other_attachment", new File(other_attachment));
            }
            if (!remark.equalsIgnoreCase("")) {
                params.put("remark", remark);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String url;
        url = getString(R.string.json_employe_expenses);
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
                errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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

    public void getApiexpense1(String emp_id, String date, String expenses_type) {


        loadingView = new LoadingView(SaveexActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_employe_expenses);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("emp_id", emp_id);
                params.put("date", date);
                params.put("expenses_type", expenses_type);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }

    public void getExpenseApi(String emp_id) {
        expensesTypeItemModels.clear();
        mStrings_expense.clear();
        loadingView = new LoadingView(SaveexActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_expenses_type);
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
                        ExpensesTypeModel loginResponse = gson.fromJson(response, ExpensesTypeModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                expensesTypeItemModels = loginResponse.getItems();
                                for (int i=0;i<expensesTypeItemModels.size();i++)
                                {
                                    mStrings_expense.add(loginResponse.getItems().get(i).getName());
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
                            Toast.makeText(SaveexActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("emp_id", emp_id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getStaticTransport() {
        mStrings.add("Public Transport");
        mStrings.add("Private Transport");

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


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
                        CameraUtils.openSettings(SaveexActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
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
    private void captureImage2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath2 = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);
    }

    private void captureImage3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath3 = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE3);
    }
    private void captureImage4() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath4 = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);
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

                iv_taamount.setImageBitmap(bitmap);


                // successfully captured the image
                // display it in image view
                previewCapturedImage();
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
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE2) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath2);

                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath2);

                ivdaamout.setImageBitmap(bitmap);



                // successfully captured the image
                // display it in image view
              //  previewCapturedImage2();
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
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE3) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath3);


                Bitmap bitmap = CameraUtils.optimizeBitmap(IMAGE_MAX_SIZE, imageStoragePath3);

                iv_otharimages.setImageBitmap(bitmap);


                // successfully captured the image
                // display it in image view
               // previewCapturedImage3();
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


    /**
     * Display image from gallery
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //txtDescription.setVisibility(View.GONE);
            //videoPreview.setVisibility(View.GONE);

            //iv_car_photo.setVisibility(View.VISIBLE);

          //  Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

           // iv_car_photo.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void getStateApi() {
        stateItemModelArrayList.clear();
        mStrings.clear();
      //  loadingView = new LoadingView(SaveexActivity.this);
      //  loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_state);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                      //  loadingView.hideLoadingView();

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
                        StateModel loginResponse = gson.fromJson(response, StateModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                stateItemModelArrayList = loginResponse.getItems();
                                for (int i=0;i<stateItemModelArrayList.size();i++) {
                                    String data = loginResponse.getItems().get(i).getName();
                                    String data1 = "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);
                                    mStrings_state.add(data + " " + data1);

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
                            Toast.makeText(SaveexActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
               /* errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });*/
                //errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }


    public void getCityApi(String state_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        loadingView = new LoadingView(SaveexActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_city);
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
                        try {
                            Gson gson = new Gson();
                            CityModel loginResponse = gson.fromJson(response, CityModel.class);
                            if (loginResponse.getSuccess())
                            {
                                if (loginResponse.getItems() != null) {
                                    cityItemModelArrayList = loginResponse.getItems();
                                    for (int i=0;i<cityItemModelArrayList.size();i++) {
                                        String data = loginResponse.getItems().get(i).getName();
                                        String data1 = "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                        mStrings_city.add(data + " " + data1);

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
                                Toast.makeText(SaveexActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("state_id", state_id);
                //params.put("password", password);
                return params;
            }

            ;
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

    public void getDealerApi() {
        //spinnar
        dealerListItemModels.clear();
        // loadingView = new LoadingView(RetailerActivity.this);
        //    loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: " + response);
                        // loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);
                        if (loginResponse.getSuccess()) {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + dealerListItemModels.size());
                                for (int i = 0; i < dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1 = "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

//                                    mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                }

                                stateAdapter = new StateAdapter(SaveexActivity.this, mStrings,"Select Dealers");
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
                        } else {
                            Toast.makeText(SaveexActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: " + error.getMessage());
                /*errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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

    public void getRouteList(String emp_id, String dealer_id) {
        //spinnar
        routeItemModelArrayList.clear();
        routeStringList.clear();
        // loadingView = new LoadingView(RetailerActivity.this);
        //  loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(SaveexActivity.this);
        String url;
        url = getString(R.string.json_route_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: " + response);
                        // loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        RouteModel loginResponse = gson.fromJson(response, RouteModel.class);
                        if (loginResponse.getSuccess()) {

                            if (loginResponse.getItems() != null) {
                                routeItemModelArrayList = loginResponse.getItems();
                                Log.d("dixittttt1", "onCreate: " + dealerListItemModels.size());
                                for (int i = 0; i < routeItemModelArrayList.size(); i++) {
                                    String data = loginResponse.getItems().get(i).getRoute();
                                    String data1 = "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    routeStringList.add(data + " " + data1);
                                }

                            }
                        } else {
                            Toast.makeText(SaveexActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: " + error.getMessage());
                /*errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id);
                params.put("dealer_id", dealer_id);
                return params;
            }

            ;
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

}
