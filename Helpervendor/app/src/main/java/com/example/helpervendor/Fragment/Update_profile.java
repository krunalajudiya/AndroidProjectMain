package com.example.helpervendor.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpervendor.Activity.register_new_user;
import com.example.helpervendor.Constant.Constant;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Update_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Update_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView image_profile,image_id_proof;
    Spinner city;
    Button choose_file,update_btn,choose_id_file;
    TextInputEditText name,address,mobilenumber,pincode,visiting_rate;
    String city_name[]={"Jamnagar","Rajkot"};
    String name_str,address_str,mobilenumber_str,pincode_str,city_str,visiting_rate_str,status_str;
    File profile_file,id_proof_file;
    Uri profile_uri,id_proof_uri;

    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    private static final int PICK_FILE_REQUEST = 1001;
    private static final int PICK_FILE_REQUEST1 = 1002;
    int pos=0;
    ProgressDialog pdialog;
    UserSession userSession;
    Resultmodel.Data data;
    Switch onoffswitch;

    SharedPreferences availablity_sharedpreferences;

    public Update_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Update_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Update_profile newInstance(String param1, String param2) {
        Update_profile fragment = new Update_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_profile, container, false);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        Log.d("vendor_id",data.getVendor_id());
        name=view.findViewById(R.id.name);
        address=view.findViewById(R.id.address);
        mobilenumber=view.findViewById(R.id.mobilenumber);
        pincode=view.findViewById(R.id.pincode);
        visiting_rate=view.findViewById(R.id.visiting_rate);
        image_profile=view.findViewById(R.id.image_profile);
        image_id_proof=view.findViewById(R.id.image_id_proof);
        city=view.findViewById(R.id.city_spinner);
        choose_file=view.findViewById(R.id.chose_file);
        choose_id_file=view.findViewById(R.id.chose_id_file);
        update_btn=view.findViewById(R.id.update_btn);
        onoffswitch=view.findViewById(R.id.onoff);


        availablity_sharedpreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor=availablity_sharedpreferences.edit();

        onoffswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        status_str="1";
                        Avaliable_vendor();
                        editor.putBoolean("availableStatus",true);
                        editor.commit();

                    }else{
                        status_str="0";
                        Avaliable_vendor();
                        editor.putBoolean("availableStatus",false);
                        editor.commit();
                    }
            }
        });
        if (Integer.parseInt(data.getStatus())==1)
        {
            onoffswitch.setChecked(true);
        }else
        {
            onoffswitch.setChecked(false);
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,city_name);
        city.setAdapter(arrayAdapter);
        city.setEnabled(false);
        city.setClickable(false);
        if (data.getVendor_name()!=null && !TextUtils.isEmpty(data.getVendor_name()))
        {
            name.setText(data.getVendor_name());
        }
        if (data.getAddress()!=null && !TextUtils.isEmpty(data.getAddress()))
        {
            address.setText(data.getAddress());
        }
        if (data.getMobile()!=null && !TextUtils.isEmpty(data.getMobile()))
        {
            mobilenumber.setText(data.getMobile());
        }
        if (data.getPincode()!=null && !TextUtils.isEmpty(data.getPincode()))
        {
            pincode.setText(data.getPincode());
        }
        if (data.getVisiting_charge()!=null && !TextUtils.isEmpty(data.getVisiting_charge()))
        {
            visiting_rate.setText(data.getVisiting_charge());
        }
        if (data.getCity()!=null && !TextUtils.isEmpty(data.getCity()))
        {
            if (Arrays.asList(city_name).contains(data.getCity()))
            {
                city.setSelection(Arrays.asList(city_name).indexOf(data.getCity()));
            }
        }
        if (data.getPhoto()!=null && !TextUtils.isEmpty(data.getPincode())) {
            Log.d("ff",data.getPhoto());
            Glide.with(getActivity()).load(Constant.IMAGE_URL + data.getPhoto()).into(image_profile);
//            Picasso.get().load(Constant.IMAGE_URL + data.getPhoto()).into(image_profile);
        }
        if (data.getId_proof()!=null && !TextUtils.isEmpty(data.getId_proof())) {
            Log.d("ff",Constant.IMAGE_URL+data.getId_proof());
            Glide.with(getActivity()).load(Constant.IMAGE_URL + data.getId_proof()).into(image_id_proof);
//            Picasso.get().load(Constant.IMAGE_URL + data.getId_proof()).into(image_id_proof);
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_str=city_name[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=1;
                Log.d("ff","click");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile();
                    }
                }else {
                    ChooseFile();
                }
            }
        });
        choose_id_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=2;
                Log.d("ff","click");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile1();
                    }
                }else {
                    ChooseFile1();
                }
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                Register_user_detail();
                useravailable();


            }
        });


        return view;

    }
    public void Avaliable_vendor()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Vendor_avalibality("vendor_avalibality",status_str,data.getVendor_id());
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    data.setStatus(status_str);
                    String detail;
                    Gson gson=new Gson();
                    detail=gson.toJson(data);
                    userSession.createLoginSession(detail);
                    Toast.makeText(getActivity(),"Status Update Successfully",Toast.LENGTH_LONG).show();
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                pdialog.dismiss();

                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });

    }
    private void ChooseFile() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST);

    }
    private void ChooseFile1() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST1);

    }
    public void getvalue()
    {
        name_str=name.getText().toString().trim();
        mobilenumber_str=mobilenumber.getText().toString().trim();

        address_str=address.getText().toString().trim();
        pincode_str=pincode.getText().toString().trim();
        visiting_rate_str=visiting_rate.getText().toString().trim();
        if (profile_uri!=null)
        {
            profile_file= FileUtils.getFile(getActivity(),profile_uri);
        }
        if (id_proof_uri!=null)
        {
            id_proof_file= FileUtils.getFile(getActivity(),id_proof_uri);
        }

    }
    public  void Register_user_detail()
    {
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();


        RequestBody requesttag=RequestBody.create(MediaType.parse("text/plain"), "Vendor_update");
        RequestBody requestid=RequestBody.create(MediaType.parse("text/plain"), data.getVendor_id());
        RequestBody requestname=RequestBody.create(MediaType.parse("text/plain"), name_str);
        RequestBody requestmobile=RequestBody.create(MediaType.parse("text/plain"), mobilenumber_str);
        RequestBody requestaddress=RequestBody.create(MediaType.parse("text/plain"), address_str);
        RequestBody requestcity=RequestBody.create(MediaType.parse("text/plain"), city_str);
        RequestBody requestpincode=RequestBody.create(MediaType.parse("text/plain"), pincode_str);
        RequestBody requestvisitingrate=RequestBody.create(MediaType.parse("text/plain"), visiting_rate_str);

        MultipartBody.Part body;
        if (profile_uri!=null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_file);
            body = MultipartBody.Part.createFormData("photo", profile_file.getName(), requestBody);
        }else {
            body = MultipartBody.Part.createFormData("photo", "");
        }
        MultipartBody.Part id_body;
        if (id_proof_uri!=null) {
            RequestBody request_id_Body = RequestBody.create(MediaType.parse("multipart/form-data"), id_proof_file);
            id_body = MultipartBody.Part.createFormData("id_proof", id_proof_file.getName(), request_id_Body);
        }else {
            id_body = MultipartBody.Part.createFormData("id_proof", "");
        }
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Update_vendor_details(requesttag,requestid,requestname,requestmobile,body,requestaddress,requestcity,requestpincode,id_body,requestvisitingrate);
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
                        Toast.makeText(getActivity(),"Update Successfully",Toast.LENGTH_LONG).show();
                        data.setVendor_name(name_str);
                        data.setAddress(address_str);
                        data.setMobile(mobilenumber_str);
                        data.setCity(city_str);
                        data.setPincode(pincode_str);
                        data.setVisiting_charge(visiting_rate_str);

                        if (profile_uri!=null)
                        {
                            data.setPhoto(profile_file.getName());
                        }
                        if (id_proof_uri!=null)
                        {
                            data.setId_proof(id_proof_file.getName());
                        }
                        String detail;
                        Gson gson=new Gson();
                        detail=gson.toJson(data);
                        userSession.createLoginSession(detail);

                        loadFragment(new New_order());

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
                pdialog.dismiss();
            }
        });

    }
    public void useravailable(){
        Call<Resultmodel> call=new Retrofitclient().getMyApi().vendoravailable_status("vendor_avalibality",data.getVendor_id(),status_str);
        call.enqueue(new Callback<Resultmodel>() {
            @Override
            public void onResponse(Call<Resultmodel> call, Response<Resultmodel> response) {
                if (response.isSuccessful()){
                    Log.d("available_status","ok");
                }
            }

            @Override
            public void onFailure(Call<Resultmodel> call, Throwable t) {

            }
        });
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ChooseFile();
                }else {
                    Toast.makeText(getActivity(),"Permission denied....",Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST ){
            image_profile.setImageURI(data.getData());

            profile_uri=data.getData();
            Log.d("hello", String.valueOf(pos));
        }else if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST1 )
        {
            image_id_proof.setImageURI(data.getData());
            id_proof_uri=data.getData();
        }
    }
}