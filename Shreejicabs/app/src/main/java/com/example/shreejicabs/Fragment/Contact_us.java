package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shreejicabs.Activity.LoginActivity;
import com.example.shreejicabs.Activity.RegisterActivity;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contact_us#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contact_us extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView taxil;
    EditText name,mobilenumber,comment;
    String name_str,mobile_number_str,comment_str;
    Button submit;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private static final String TAG_MESSAGE = "error_msg";
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    int success;

    public Contact_us() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contact_us.
     */
    // TODO: Rename and change types and number of parameters
    public static Contact_us newInstance(String param1, String param2) {
        Contact_us fragment = new Contact_us();
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
        View view= inflater.inflate(R.layout.fragment_contact_us, container, false);
        taxil=(ImageView)view.findViewById(R.id.taxi1gif);
        name=(EditText) view.findViewById(R.id.name);
        mobilenumber=(EditText)view.findViewById(R.id.mobilenumber);
        comment=(EditText)view.findViewById(R.id.comment);
        submit=(Button)view.findViewById(R.id.submit);
        Glide.with(getActivity()).load(R.drawable.taxi1).into(taxil);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                new Add_contact_us().execute();
            }
        });

        return view;
    }
    public void getvalue()
    {
        name_str=name.getText().toString();
        mobile_number_str=mobilenumber.getText().toString();
        comment_str=comment.getText().toString();
    }
    private class Add_contact_us extends AsyncTask<String, String, String> {
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
                params .add(new BasicNameValuePair("tag", "inquiry_support"));
                params .add(new BasicNameValuePair("Name", name_str));
                params .add(new BasicNameValuePair("Mobile", mobile_number_str));
                params .add(new BasicNameValuePair("descripation", comment_str));


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
                Toast.makeText(getActivity(), "Add Suggestion successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
            else{
                Toast.makeText(getActivity(), "Add Suggestion not successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}