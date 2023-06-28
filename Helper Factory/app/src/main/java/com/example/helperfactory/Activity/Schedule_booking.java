package com.example.helperfactory.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Couponmodel;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.Other.MySingleton;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Schedule_booking extends AppCompatActivity {

    LinearLayout tomorrow,today,pickdate,eighttonine,ninetoten,tentoeleven,eleventotweleve,twelevetoone,onetotwo,twotothree,threetofour,fourtofive,fivetosix,sixtoseven,seventoeight,eighttoninepm,ninetotenpm,coupanlayout;
    Toolbar toolbar;
    Button continue_btn;
    TextView tomorrow_txt,today_txt,pickdate_txt;
    Calendar myCalendar;
    UserSession userSession;
    Resultmodel.Data userdata;
    String tomorrow_str,today_str,date_str,time_str,client_id,category_id,sub_cat_id,sub_cat_name;
    ProgressDialog pdialog;
    TextView price;
    ImageView issue_img,close;
    Button choose_photo,book_now;
    Uri issue_uri;
    File issue_file;
    RadioGroup couponuse;
    RadioButton yes,no;
    String couponcode_str,Couponcode_str_temp;
    private static final int REQUEST_PERMISSION = 1000;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABMS_FlA:APA91bG-WRSzwo6soXltBYD8902ttAlAf_NrkhGekidnzqcp5AtKqO3BhOiZzGoArEY4rI2PBAvn5q3O6yH1YOkNBq10HKOgRZOyagokEwh1WFMyjEgr35zP8ZczvHOxQWf5LufpFH-0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_booking);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.app_name));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    onBackPressed();
                }
            });
        }
        userSession=new UserSession(getApplicationContext());
        Gson gson=new Gson();
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        client_id=userdata.getClient_id();
        eighttonine=(LinearLayout)findViewById(R.id.eighttonine);
        ninetoten=(LinearLayout)findViewById(R.id.ninetoten);
        tentoeleven=(LinearLayout)findViewById(R.id.tentoeleven);
        eleventotweleve=(LinearLayout)findViewById(R.id.eleventotweleve);
        twelevetoone=(LinearLayout)findViewById(R.id.twelevetoone);
        onetotwo=(LinearLayout)findViewById(R.id.onetotwo);
        twotothree=(LinearLayout)findViewById(R.id.twotothree);
        threetofour=(LinearLayout)findViewById(R.id.threetofour);
        fourtofive=(LinearLayout)findViewById(R.id.fourtofive);
        fivetosix=(LinearLayout)findViewById(R.id.fivetosix);
        sixtoseven=(LinearLayout)findViewById(R.id.sixtoseven);
        seventoeight=(LinearLayout)findViewById(R.id.seventoeight);
        eighttoninepm=(LinearLayout)findViewById(R.id.eighttoninepm);
        ninetotenpm=(LinearLayout)findViewById(R.id.ninetotenpm);
        tomorrow=(LinearLayout)findViewById(R.id.tomorrow);
        today=(LinearLayout)findViewById(R.id.today);
        pickdate=(LinearLayout)findViewById(R.id.pickdate);
        continue_btn=(Button)findViewById(R.id.continue_btn);
        today_txt=(TextView)findViewById(R.id.today_txt);
        tomorrow_txt=(TextView)findViewById(R.id.tomorrow_txt);
        pickdate_txt=(TextView)findViewById(R.id.pickdate_txt);
        price=(TextView)findViewById(R.id.pricing);



        Bundle b=getIntent().getExtras();
        sub_cat_id=b.getString("sub_cat_id");
        sub_cat_name=b.getString("sub_cat_name");
        Log.d("ff",sub_cat_id);
        Log.d("sub_cat_name",sub_cat_name);
        category_id=b.getString("cat_id");
        price.setText(b.getString("pricing"));
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        tomorrow_str= new SimpleDateFormat("dd/MM/yy").format(cal.getTime());
        tomorrow_txt.setText(tomorrow_str.split("/")[0]);
        Calendar cal1 = new GregorianCalendar();
        cal1.add(Calendar.DATE, 0);
        today_str= new SimpleDateFormat("dd/MM/yy").format(cal1.getTime());
        today_txt.setText(today_str.split("/")[0]);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomorrow.setSelected(true);
                if (today.isSelected())
                {
                    today.setSelected(false);
                }
                if (pickdate.isSelected())
                {
                    pickdate.setSelected(false);
                }


            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today.setSelected(true);
                if (tomorrow.isSelected())
                {
                    tomorrow.setSelected(false);
                }
                if (pickdate.isSelected())
                {
                    pickdate.setSelected(false);
                }


            }
        });
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate.setSelected(true);
                if (today.isSelected())
                {
                    today.setSelected(false);
                }
                if (tomorrow.isSelected())
                {
                    tomorrow.setSelected(false);
                }
                DatePickerDialog datePickerDialog=new DatePickerDialog(Schedule_booking.this,R.style.MyAlertDateDialogStyle,datelistener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });
        layout_listeners();
        AlertDialog.Builder builder = new AlertDialog.Builder(Schedule_booking.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(Schedule_booking.this).inflate(R.layout.issue_photo_dialog, viewGroup, false);
        issue_img=dialogView.findViewById(R.id.issue_img);
        choose_photo=dialogView.findViewById(R.id.choosephoto);
        book_now=dialogView.findViewById(R.id.book_now);
        close=dialogView.findViewById(R.id.close);
        couponuse=dialogView.findViewById(R.id.couponuse);
        coupanlayout=dialogView.findViewById(R.id.cuponlayout);
        yes=dialogView.findViewById(R.id.yes);
        no=dialogView.findViewById(R.id.no);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        coupondata();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (issue_uri!=null)
                {
                    issue_file=FileUtils.getFile(Schedule_booking.this,issue_uri);
                }
                switch (couponuse.getCheckedRadioButtonId()){

                    case R.id.yes:
                        couponcode_str=Couponcode_str_temp;
                        break;
                    case R.id.no:
                        couponcode_str="";
                        break;

                }
                Booking();

                alertDialog.dismiss();
                Log.d("final details", time_str + "\n" + date_str);
            }
        });
        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check Permission
                if(ActivityCompat.checkSelfPermission(Schedule_booking.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Schedule_booking.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PERMISSION);
                }else{
                    selectImage(Schedule_booking.this);
                }
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (eighttonine.isSelected())
                {
                    time_str="8 AM to 9 AM";

                }
                if (ninetoten.isSelected())
                {
                    time_str="9 AM to 10 AM";

                }
                if (tentoeleven.isSelected())
                {
                    time_str="10 AM to 11 AM";

                }
                else if (eleventotweleve.isSelected())
                {
                    time_str="11 AM to 12 PM";

                }
                else if (twelevetoone.isSelected())
                {
                    time_str="12 PM to 1 PM";
                }
                else if (onetotwo.isSelected())
                {
                    time_str="1 PM to 2 PM";
                }
                else if (twotothree.isSelected())
                {
                    time_str="2 PM to 3 PM";
                }
                else if (threetofour.isSelected())
                {
                    time_str="3 PM to 4 PM";
                }
                else if (fourtofive.isSelected())
                {
                    time_str="4 PM to 5 PM";
                }
                else if (fivetosix.isSelected())
                {
                    time_str="5 PM to 6 PM";
                }
                else if (sixtoseven.isSelected())
                {
                    time_str="6 PM to 7 PM";
                }
                else if (seventoeight.isSelected())
                {
                    time_str="7 PM to 8 PM";
                }
                else if (eighttoninepm.isSelected())
                {
                    time_str="8 PM to 9 PM";
                }
                else if (ninetotenpm.isSelected())
                {
                    time_str="9 PM to 10 PM";
                }

                if (today.isSelected())
                {
                    date_str=today_str;
                    Log.d("date2",date_str);

                }else if (tomorrow.isSelected())
                {
                    date_str=tomorrow_str;
                    Log.d("date3",date_str);

                }else if (pickdate.isSelected()) {
                    date_str = pickdate_txt.getText().toString().trim();
                    Log.d("date4",date_str);
                }
                if (!eighttonine.isSelected() && !ninetoten.isSelected() && !tentoeleven.isSelected() && !eleventotweleve.isSelected() && !twelevetoone.isSelected() &&! onetotwo.isSelected() && !twotothree.isSelected() && !threetofour.isSelected() && !fourtofive.isSelected() && !fivetosix.isSelected() && !sixtoseven.isSelected() && !seventoeight.isSelected() && !eighttoninepm.isSelected() && !ninetotenpm.isSelected())
                {
                    Toast.makeText(getApplicationContext(),"Please Choice Any Time",Toast.LENGTH_LONG).show();
                }else if (!today.isSelected() && !tomorrow.isSelected() && !pickdate.isSelected())
                {
                    Toast.makeText(getApplicationContext(),"Please Choice Any Date",Toast.LENGTH_LONG).show();
                }else {
                    alertDialog.show();

                }
            }

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(Schedule_booking.this);
            }
        }
    }
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = Intent.createChooser(
                            FileUtils.createGetContentIntent("image/*"), "Select a file");
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        issue_img.setImageBitmap(selectedImage);
                        Long tsLong = System.currentTimeMillis()/1000;
                        issue_file=persistImage(selectedImage,tsLong.toString());
                        issue_uri=Uri.fromFile(issue_file);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        issue_uri = data.getData();
                        issue_img.setImageURI(issue_uri);
                    }
                    break;
            }
        }
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        pickdate_txt.setText(sdf.format(myCalendar.getTime()));
    }
    public void layout_listeners()
    {
        eighttonine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eighttonine.setSelected(true);
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }

            }
        });
        ninetoten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ninetoten.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }

            }
        });
        seventoeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seventoeight.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }

            }
        });
        eighttoninepm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eighttoninepm.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }

            }
        });
        ninetotenpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ninetotenpm.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }

            }
        });
        tentoeleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentoeleven.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        eleventotweleve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleventotweleve.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        twelevetoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twelevetoone.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        onetotwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onetotwo.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        twotothree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twotothree.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        threetofour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threetofour.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        fourtofive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourtofive.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        fivetosix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fivetosix.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
                if (sixtoseven.isSelected())
                {
                    sixtoseven.setSelected(false);
                }
            }
        });
        sixtoseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixtoseven.setSelected(true);
                if (eighttonine.isSelected())
                {
                    eighttonine.setSelected(false);
                }
                if (ninetoten.isSelected())
                {
                    ninetoten.setSelected(false);
                }
                if (seventoeight.isSelected())
                {
                    seventoeight.setSelected(false);
                }
                if (eighttoninepm.isSelected())
                {
                    eighttoninepm.setSelected(false);
                }
                if (ninetotenpm.isSelected())
                {
                    ninetotenpm.setSelected(false);
                }
                if (eleventotweleve.isSelected())
                {
                    eleventotweleve.setSelected(false);
                }
                if (twelevetoone.isSelected())
                {
                    twelevetoone.setSelected(false);
                }
                if (onetotwo.isSelected())
                {
                    onetotwo.setSelected(false);
                }
                if (twotothree.isSelected())
                {
                    twotothree.setSelected(false);
                }
                if (threetofour.isSelected())
                {
                    threetofour.setSelected(false);
                }
                if (fourtofive.isSelected())
                {
                    fourtofive.setSelected(false);
                }
                if (fivetosix.isSelected())
                {
                    fivetosix.setSelected(false);
                }
                if (tentoeleven.isSelected())
                {
                    tentoeleven.setSelected(false);
                }
            }
        });

    }
    public  void Booking()
    {
        pdialog = new ProgressDialog(Schedule_booking.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        RequestBody requesttag=RequestBody.create(MediaType.parse("text/plain"), "Book_appointment");
        RequestBody requestclient_id=RequestBody.create(MediaType.parse("text/plain"), client_id);
        RequestBody requestcategoryid=RequestBody.create(MediaType.parse("text/plain"), category_id);
        RequestBody requestsubcatid=RequestBody.create(MediaType.parse("text/plain"), sub_cat_id);
        RequestBody requestdate=RequestBody.create(MediaType.parse("text/plain"), date_str);
        RequestBody requesttime=RequestBody.create(MediaType.parse("text/plain"), time_str);
        RequestBody requestcoupon=RequestBody.create(MediaType.parse("text/plain"),couponcode_str);
        MultipartBody.Part body;
        if (issue_uri!=null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), issue_file);
            body = MultipartBody.Part.createFormData("issue_photo", issue_file.getName(), requestBody);
        }else {
            body = MultipartBody.Part.createFormData("issue_photo", "");
        }
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Servicebooking(requesttag,requestclient_id,requestcategoryid,requestsubcatid,requestdate,requesttime,body,requestcoupon);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Insertresultmodel insertresultmodel = response.body();
                    Log.d("fffff111", insertresultmodel.getError());
                    if (Integer.parseInt(insertresultmodel.getError()) == 200) {
                        Toast.makeText(getApplicationContext(),"Booking SuccessFully",Toast.LENGTH_LONG).show();
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        home.putExtra("success",1);
                        JSONObject notification=new JSONObject();
                        JSONObject notificationbody=new JSONObject();
                        String msg="New Order availble";
                        try {
                            notificationbody.put("title",sub_cat_name);
                            notificationbody.put("message",msg);
                            notificationbody.put("vendor","true");
                            notificationbody.put("sub_cat_id",sub_cat_id);
                            notificationbody.put("client_id",null);
                            notificationbody.put("user_id",userdata.getClient_id());
                            notificationbody.put("city",userdata.getCity());
                            notification.put("to",TOPIC);
                            notification.put("data",notificationbody);
                            Log.d("fff", String.valueOf(notification));
                            sendNotification(notification);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(home);
                        finish();
                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fff",t.getMessage());
                pdialog.dismiss();
            }
        });
    }

    public void sendNotification(JSONObject notification){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>()   {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void coupondata(){
        Call<Couponmodel> call= Retrofitclient.getInstance().getMyApi().Coupon("fetch_coupon",userdata.getClient_id());
        call.enqueue(new Callback<Couponmodel>() {
            @Override
            public void onResponse(Call<Couponmodel> call, Response<Couponmodel> response) {

                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Couponmodel couponmodel = response.body();
                    Couponmodel.coupon data=couponmodel.getData();
                    Log.d("fffff111", couponmodel.getError_msg());
                    if (couponmodel.getError() == 200) {
                        if (couponmodel.getData().getCoupon_code()!=null){
                            coupanlayout.setVisibility(View.VISIBLE);
                            Couponcode_str_temp=couponmodel.getData().getCoupon_code();
                        }

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Couponmodel> call, Throwable t) {
                //handle error or failure cases here
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());

            }
        });


    }

}