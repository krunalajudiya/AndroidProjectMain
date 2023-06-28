package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Change_password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Change_password extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText oldpassword,newpassword,confirmpassword;
    String user_id,oldpassword_str,newpassword_str,confirmpassword_str;
    Button changepassword;
    User user;
    UserSession userSession;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private ProgressDialog pDialog;
    int success=0;

    public Change_password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Change_password.
     */
    // TODO: Rename and change types and number of parameters
    public static Change_password newInstance(String param1, String param2) {
        Change_password fragment = new Change_password();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_change_password, container, false);
        oldpassword=(EditText)view.findViewById(R.id.oldpassword);
        newpassword=(EditText)view.findViewById(R.id.newpassword);
        confirmpassword=(EditText)view.findViewById(R.id.confirmpassword);
        changepassword=(Button)view.findViewById(R.id.changepassword);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        Log.d("ddd",user_id);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                if (oldpassword_str.isEmpty())
                {
                    oldpassword.setError("Enter Old Password");
                    oldpassword.requestFocus();
                }else if (newpassword_str.isEmpty())
                {
                    newpassword.setError("Enter New Password");
                    newpassword.requestFocus();
                }else if (confirmpassword_str.isEmpty())
                {
                    confirmpassword.setError("Enter Confirm Password");
                    confirmpassword.requestFocus();
                }else if (!newpassword_str.equals(confirmpassword_str))
                {
                    newpassword.setError("Please Check New Password And Confirm Password");
                    newpassword.requestFocus();
                }else{
                    new Change_password_detail().execute();
                }
            }
        });

        return view;
    }
    public void getvalue()
    {
        oldpassword_str=oldpassword.getText().toString().trim();
        newpassword_str=newpassword.getText().toString().trim();
        confirmpassword_str=confirmpassword.getText().toString().trim();
    }
    class Change_password_detail extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "chnge_password"));
                params.add(new BasicNameValuePair("old_password", oldpassword_str));
                params.add(new BasicNameValuePair("new_password", newpassword_str));
                params.add(new BasicNameValuePair("Reg_id", user_id));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("change password: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));

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
            if (success==200) {

                Toast.makeText(getActivity(),"Change Password SuccessFully",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity(),"Change Password Not SuccessFully",Toast.LENGTH_LONG).show();
            }

        }
    }
}