package com.example.shreejicabs.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Remote.IUploadAPI;
import com.example.shreejicabs.Remote.RetrofitClient;
import com.example.shreejicabs.Session.UserSession;
import com.example.shreejicabs.Utils.ProgressRequestBody;
import com.example.shreejicabs.Utils.UploadCallBacks;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNew_Vehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNew_Vehicle extends Fragment implements UploadCallBacks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    EditText model_name,passing_year,vehicle_color;
    RadioGroup fule_type,carrier_avalible;
    Button save,choose_image;
    RadioButton radioButton,available;
    ImageView taxi_image;
    UserSession userSession;
    JSONParser jsonParser=new JSONParser();
    String user_id;
    String model,passing,color,fule,carrier;
    String image_name;
    IUploadAPI mService;
    ProgressDialog progressDialog;

    Uri taxi_img_url;

    public static final int PICK_FILE_REQ=1000;
    private static final String TAG_SUCCESS = "error";
    public static final String BASE_URL = Constants.BASE_URL;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public AddNew_Vehicle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNew_Vehicle.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNew_Vehicle newInstance(String param1, String param2) {
        AddNew_Vehicle fragment = new AddNew_Vehicle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private IUploadAPI getAPIUpload()
    {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_add_new__vehicle, container, false);
        mService = getAPIUpload();
        model_name=view.findViewById(R.id.modelname);
        passing_year=view.findViewById(R.id.passingyear);
        vehicle_color=view.findViewById(R.id.colorvehicle);
        fule_type=view.findViewById(R.id.fuletype);
        choose_image=view.findViewById(R.id.chooseimage);
        save=view.findViewById(R.id.save);
        taxi_image=view.findViewById(R.id.taxiimage);
        carrier_avalible=view.findViewById(R.id.carrier_available);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        User user=gson.fromJson(userSession.getUserDetails(), User.class);
        user_id=user.getId();




        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseFile();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model_name.getText().toString().trim().length()>0 && passing_year.getText().toString().trim().length()>0
                        && vehicle_color.getText().toString().trim().length()>0 && taxi_img_url!=null){
                    getvalue(view);
                    uploadFile(taxi_img_url,image_name);
                    new UploadCarDetails().execute();

                }

            }
        });

        return view;
    }


    private void getvalue(View view){
        model=model_name.getText().toString().trim();
        passing=passing_year.getText().toString().trim();
        color=vehicle_color.getText().toString().trim();
        int ful=fule_type.getCheckedRadioButtonId();
        radioButton=view.findViewById(ful);
        fule=radioButton.getText().toString();
        int carrier_avl=carrier_avalible.getCheckedRadioButtonId();
        available=view.findViewById(carrier_avl);
        carrier=available.getText().toString();
        if(taxi_img_url != null){
            File file = FileUtils.getFile(getActivity(), taxi_img_url);
            image_name = getSaltString() + file.getName();
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
        startActivityForResult(intent, PICK_FILE_REQ);
    }
    private void uploadFile(Uri uri,String name){
        if (uri!=null){
            File file=FileUtils.getFile(getActivity(),uri);
            ProgressRequestBody requestFile = new ProgressRequestBody(file,this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "uploaded_file", name, requestFile);
            Log.d("ff", String.valueOf(requestFile));
            new Thread(new Runnable() {
                @Override
                public void run() {

                    mService.uploadFile(body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d("ff1", String.valueOf(response.code()));
//                                    progressDialog.dismiss();

                                    //Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("ff2",t.getMessage());
//                                    progressDialog.dismiss();
                                    //Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ff","out");
        switch (requestCode) {
            case PICK_FILE_REQ:
                if (resultCode == RESULT_OK) {
                    taxi_img_url = data.getData();
                    taxi_image.setImageURI(taxi_img_url);
                }
                break;

        }
    }
    class UploadCarDetails extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> params=new ArrayList<>();
                params.add(new BasicNameValuePair("tag","add_driver_car"));
                params.add(new BasicNameValuePair("user_id",user_id));
                params.add(new BasicNameValuePair("car_name",model));
                params.add(new BasicNameValuePair("color",color));
                params.add(new BasicNameValuePair("fuel",fule));
                params.add(new BasicNameValuePair("year",passing));
                params.add(new BasicNameValuePair("carrier",carrier));
                params.add(new BasicNameValuePair("photo",image_name));

                JSONObject jsonObject=jsonParser.makeHttpRequest(Constants.url,"POST",params);

                int success = jsonObject.getInt(TAG_SUCCESS);
                Log.d("ff", String.valueOf(success));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(getActivity(),"upload car details succesfull",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }
}