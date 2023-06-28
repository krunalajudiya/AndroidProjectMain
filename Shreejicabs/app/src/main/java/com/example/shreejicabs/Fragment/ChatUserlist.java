package com.example.shreejicabs.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.Adapter.Useradapter;
import com.example.shreejicabs.Model.Chatmodel;
import com.example.shreejicabs.Model.Chatusermodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatUserlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatUserlist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView chatuserrec;
    DatabaseReference reference;
    UserSession userSession;
    User user;
    ArrayList<Chatusermodel> chatUserlistArrayList=new ArrayList<>();


    public ChatUserlist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatUserlist.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatUserlist newInstance(String param1, String param2) {
        ChatUserlist fragment = new ChatUserlist();
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
        View view= inflater.inflate(R.layout.fragment_chat_userlist, container, false);
        chatuserrec=(RecyclerView)view.findViewById(R.id.chatuserrec);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        chatuserrec.setLayoutManager(linearLayoutManager);
        chatuserrec.setHasFixedSize(true);
        userSession=new UserSession(getActivity());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatUserlistArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Chatmodel chatmodel=dataSnapshot.getValue(Chatmodel.class);


                    if (chatmodel.getSender().equals(user.getId()))
                    {

                        Chatusermodel chatusermodel=new Chatusermodel(chatmodel.getReceivername(),chatmodel.getReceiver());
                        chatUserlistArrayList.add(chatusermodel);
                        Log.d("receivername",chatusermodel.getId());
                    }
                    if (chatmodel.getReceiver().equals(user.getId()))
                    {
                        Chatusermodel chatusermodel=new Chatusermodel(chatmodel.getSendername(),chatmodel.getSender());
                        chatUserlistArrayList.add(chatusermodel);
                        Log.d("sendername",chatusermodel.getId());
                    }

                }
                HashSet<Chatusermodel> hashSet = new HashSet(chatUserlistArrayList);
                //ArrayList<Chatusermodel> chatusermodelArrayList1=new ArrayList<>(hashSet);
                chatUserlistArrayList.clear();
                chatUserlistArrayList.addAll(hashSet);
                Log.d("size",String.valueOf(chatUserlistArrayList.size()));



                Useradapter useradapter=new Useradapter(getActivity(),chatUserlistArrayList);
                chatuserrec.setAdapter(useradapter);
                useradapter.setOnItemClickListener(new Useradapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos, View v) {
                        Intent intent=new Intent(getActivity(), Chat.class);
                        intent.putExtra("receiver",chatUserlistArrayList.get(pos).getId());
                        intent.putExtra("receivername",chatUserlistArrayList.get(pos).getName());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}