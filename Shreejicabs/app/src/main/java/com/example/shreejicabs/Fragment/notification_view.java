package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shreejicabs.Activity.LoginActivity;
import com.example.shreejicabs.Adapter.Notificationadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Model.Notificationmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class notification_view extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog pDialog;
    int success;
    UserSession userSession;
    User user;
    private static final String TAG_SUCCESS = "error";
    JSONParser jParser = new JSONParser();
    List<Notificationmodel> notificationmodelList;
    LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification_view, container, false);
        recyclerView=view.findViewById(R.id.notificationrecyclerview);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        Log.d("user_id",user.getId());
        notificationmodelList=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(getActivity());

        new notificationdata().execute();

        return view;
    }

    private class notificationdata extends AsyncTask<String, String, String> {
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
                params .add(new BasicNameValuePair("tag", "get_notification"));
                params .add(new BasicNameValuePair("client_id", user.getId()));

                JSONObject jsonObject =jParser.makeHttpRequest(Constants.url, "POST", params);

                // check your log for json response
                Log.d("Register Details", jsonObject.toString());
                success = jsonObject.getInt(TAG_SUCCESS);
                if (success==200)
                {

                    JSONObject jsonobject1=jsonObject.getJSONObject("data");
                    JSONArray notification=jsonobject1.getJSONArray("notification");
                    for (int i=0;i<notification.length();i++){
                        JSONObject jsonObject2=notification.getJSONObject(i);
                        String title=jsonObject2.getString("title");
                        String message=jsonObject2.getString("message");
                        String date=jsonObject2.getString("date");

                        //Log.d("title",title);
                        //Log.d("message",message);

                        notificationmodelList.add(new Notificationmodel(title,message,date));


                    }






                }

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
                Notificationadapter notificationadapter=new Notificationadapter(getActivity(),notificationmodelList);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(notificationadapter);
                

            }
            else{

            }
        }
    }
}