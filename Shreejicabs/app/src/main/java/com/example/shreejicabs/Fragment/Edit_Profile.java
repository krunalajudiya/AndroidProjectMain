package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.shreejicabs.Activity.LoginActivity;
import com.example.shreejicabs.Activity.RegisterActivity;
import com.example.shreejicabs.Adapter.Allavaliabilityadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Remote.IUploadAPI;
import com.example.shreejicabs.Remote.RetrofitClient;
import com.example.shreejicabs.Session.UserSession;
import com.example.shreejicabs.Utils.ProgressRequestBody;
import com.example.shreejicabs.Utils.UploadCallBacks;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_Profile extends Fragment implements UploadCallBacks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText name,mobile_number,email,company_name,address,city,website;
    String name_str,mobile_number_str,email_str,company_name_str,address_str,city_str,website_str,user_id,licence_str;
    Button update_info,choose_licence;
    ImageView licence_img;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private static final String TAG_MESSAGE = "error_msg";
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    JSONArray products = null;
    int success;
    User user;
    UserSession userSession;
    Uri selectlicence;
    private static final int PICK_FILE_REQUEST = 1001;
    //Retrofit
    public static final String BASE_URL = Constants.BASE_URL;
    IUploadAPI mService;
    private ProgressDialog progressDialog;
    LottieAnimationView loading_screen;
    LinearLayout layout_main;

    public Edit_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_Profile newInstance(String param1, String param2) {
        Edit_Profile fragment = new Edit_Profile();
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
        View view= inflater.inflate(R.layout.fragment_edit__profile, container, false);
        name=(EditText)view.findViewById(R.id.name);
        mService = getAPIUpload();
        mobile_number=(EditText)view.findViewById(R.id.number);
        email=(EditText)view.findViewById(R.id.email);
        company_name=(EditText)view.findViewById(R.id.companyname);
        address=(EditText)view.findViewById(R.id.address);
        city=(EditText)view.findViewById(R.id.city);
        website=(EditText)view.findViewById(R.id.website);
        update_info=(Button)view.findViewById(R.id.updateinfo);
        choose_licence=(Button)view. findViewById(R.id.chooselicence);
        licence_img=(ImageView) view.findViewById(R.id.driving_license_img);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        layout_main=(LinearLayout)view.findViewById(R.id.layout_main);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        new LoadUserDetails().execute();
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
                uploadFile(selectlicence,licence_str);
                new Update_info().execute();

            }
        });
        choose_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChooseFile();
            }
        });
        return view;
    }
    private IUploadAPI getAPIUpload()
    {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }
    public void getvalue()
    {
        name_str=name.getText().toString().trim();
        mobile_number_str=mobile_number.getText().toString().trim();
        email_str=email.getText().toString().trim();
        company_name_str=company_name.getText().toString().trim();
        address_str=address.getText().toString().trim();
        city_str=city.getText().toString().trim();
        website_str=website.getText().toString().trim();
        if(selectlicence != null){
            File file = FileUtils.getFile(getActivity(), selectlicence);
            licence_str = getSaltString() + file.getName();
        }
    }
    protected String getSaltString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    private void ChooseFile() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }
    private void uploadFile(Uri selectUri, String name) {
        if(selectUri != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading.....");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
//            progressDialog.show();

            File file = FileUtils.getFile(getActivity(), selectUri);
            ProgressRequestBody requestFile = new ProgressRequestBody(file,this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "uploaded_file", name, requestFile);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    mService.uploadFile(body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
//                                    progressDialog.dismiss();

                                    //Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
//                                    progressDialog.dismiss();
                                    //Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }
    }
    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILE_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectlicence = data.getData();
                    licence_img.setImageURI(selectlicence);
                }
                break;

        }
    }

    private class Update_info extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params .add(new BasicNameValuePair("tag", "update_profile"));
                params .add(new BasicNameValuePair("Reg_id", user_id));
                params .add(new BasicNameValuePair("Name", name_str));
                params .add(new BasicNameValuePair("Mobile", mobile_number_str));
                params .add(new BasicNameValuePair("Email", email_str));
                params .add(new BasicNameValuePair("Company_name", company_name_str));
                params .add(new BasicNameValuePair("Address", address_str));
                params .add(new BasicNameValuePair("City", city_str));
                params .add(new BasicNameValuePair("Website", website_str));
                params .add(new BasicNameValuePair("Experience", ""));
                params .add(new BasicNameValuePair("Policy", ""));
                params .add(new BasicNameValuePair("Licence", ""));
                params .add(new BasicNameValuePair("Proof_id", licence_str));
                params .add(new BasicNameValuePair("User_type", ""));

                JSONObject jsonObject =jParser.makeHttpRequest(Constants.url, "POST", params);
                // check your log for json response
                Log.d("Register Details", jsonObject.toString());
                success = jsonObject.getInt(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(success == 200){
                //successfully created the product
                Toast.makeText(getActivity(), "Update Information successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Update Information Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class LoadUserDetails extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "fetch_profile"));
                params.add(new BasicNameValuePair("Reg_id", user_id));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("fetchp");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable

                        name_str = c.getString("Name");
                        mobile_number_str = c.getString("Mobile_no");
                        email_str = c.getString("Email");
                        company_name_str = c.getString("Company_name");
                        address_str = c.getString("Address");
                        city_str = c.getString("City");
                        website_str = c.getString("Website");
                        licence_str=c.getString("proof");


                    }



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            layout_main.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);
            if (success==200) {
                if (!TextUtils.isEmpty(name_str))
                {
                    name.setText(name_str);
                }
                if (!TextUtils.isEmpty(mobile_number_str))
                {
                    mobile_number.setText(mobile_number_str);
                }
                if (!TextUtils.isEmpty(email_str))
                {
                    email.setText(email_str);
                }
                if (!TextUtils.isEmpty(company_name_str))
                {
                    company_name.setText(company_name_str);
                }
                if (!TextUtils.isEmpty(address_str))
                {
                    address.setText(address_str);
                }
                if (!TextUtils.isEmpty(city_str))
                {
                    city.setText(city_str);
                }
                if (!TextUtils.isEmpty(website_str))
                {
                    website.setText(website_str);
                }
                if (!TextUtils.isEmpty(licence_str))
                {
                    Picasso.get().load(Constants.IMAGE_URL+licence_str).into(licence_img);
//                    Glide.with(getActivity()).load(Constants.IMAGE_URL+licence_str).into(licence_img);
//                    Log.d("ff",Constants.IMAGE_URL+licence_str);
                }

            }else{

            }

        }
    }
}