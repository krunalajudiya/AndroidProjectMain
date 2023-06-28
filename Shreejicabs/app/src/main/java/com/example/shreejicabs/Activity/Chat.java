package com.example.shreejicabs.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shreejicabs.Adapter.ChatAdapter;
import com.example.shreejicabs.Model.Chatmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.MySingleton;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {

    ImageView sendButton;
    EditText messageArea;
    Button removbutton;
    DatabaseReference reference;
    UserSession userSession;
    User user;
    String reciver_str,recivername_str;
    ChatAdapter chatAdapter;
    RecyclerView chatrec;
    ArrayList<Chatmodel> chatmodelArrayList=new ArrayList<>();
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA0GlGw6A:APA91bFQAJPgsf8J1JZ0MEaDDSAy4gNBu1hkjmi08TlrzSd2OGT6ajTB14rcooZdeBoWpNb7q4Q5aB9BdDKpApLQ6Djb_yMkRpNkusI9WYcODy9tEkjl1diI10DzKbVZNKevCtTRm6hA";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";




    protected void onStart(){
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userSession=new UserSession(getApplicationContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        //layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        removbutton =(Button) findViewById(R.id.removechat);
//        scrollView = (ScrollView)findViewById(R.id.scrollView);
        chatrec=(RecyclerView)findViewById(R.id.chatrec);
        chatrec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        chatrec.setLayoutManager(linearLayoutManager);
        Bundle b=getIntent().getExtras();
        reciver_str=b.getString("receiver");
        recivername_str=b.getString("receivername");
        readmessage(user.getId(),reciver_str);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageArea.getText().toString();
                messageArea.setText("");
                sendmessage(user.getId(),user.getName(),reciver_str,recivername_str,message);
            }
        });
        removbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removechat();
            }
        });

    }
    private  void sendmessage(String sender,String sendername,String receiver,String receivername,String message)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd'|'HH:mm");
        String CurrentDateTime=simpleDateFormat.format(new Date());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("sendername",sendername);
        hashMap.put("receiver",receiver);
        hashMap.put("receivername",receivername);
        hashMap.put("message",message);
        hashMap.put("time",CurrentDateTime);
        reference.child("Chats").push().setValue(hashMap);
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", receivername);
            notifcationBody.put("message", message);
            notifcationBody.put("userid",receiver);
            notifcationBody.put("type","2");
;
            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    private void readmessage(final String myid, final String userid)
    {
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatmodelArrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Chatmodel  chatmodel=snapshot1.getValue(Chatmodel.class);
                    if (chatmodel.getReceiver().equals(myid) && chatmodel.getSender().equals(userid) || chatmodel.getReceiver().equals(userid) && chatmodel.getSender().equals(myid))
                    {
                        chatmodelArrayList.add(chatmodel);
                    }
                    chatAdapter=new ChatAdapter(Chat.this,chatmodelArrayList);
                    chatrec.setAdapter(chatAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void removechat(){
        DatabaseReference ref=  FirebaseDatabase.getInstance().getReference("Chats");
        ref.child("-MYEZDVkwkVj3fHFe0Cy");
        ref.removeValue();
//        Query query=FirebaseDatabase.getInstance().getReference("Chats").orderByChild("sender")
//                .equalTo(chatmodelArrayList.get(1).getSender());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    Chatmodel chatmodel=snapshot1.getValue(Chatmodel.class);
//                    Log.d("ff",chatmodel.getReceiver());
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        Log.d("ke", String.valueOf(reference.child("message").setValue("hello how are you")));
//        Log.d("key", String.valueOf(reference));
        Log.d("getsender",chatmodelArrayList.get(1).getSender());
    }


    /*public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(17);
        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            lp.gravity = Gravity.RIGHT;
            lp.setMargins(200, 5, 10, 5);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            lp.gravity = Gravity.LEFT;
            lp.setMargins(10, 5, 200, 5);
        }
        textView.setLayoutParams(lp);
        layout.addView(textView);
        //scrollView.scrollTo(0, scrollView.getBottom());
        scrollView.fullScroll(View.FOCUS_DOWN);
    }*/
}