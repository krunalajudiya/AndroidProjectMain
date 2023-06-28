package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.Model.Chatmodel;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    ArrayList<Chatmodel> chatmodelArrayList=new ArrayList<>();
    Activity context;
    UserSession userSession;
    User user;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView show_message;
        public final TextView dateandtime;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            show_message=(TextView)view.findViewById(R.id.show_message);
            dateandtime=(TextView)view.findViewById(R.id.dateandtime);





        }
    }

    public ChatAdapter(Activity context, ArrayList<Chatmodel> chatmodelArrayList) {
        this.userSession=new UserSession(context);
        this.context=context;
        this.chatmodelArrayList=chatmodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Chatmodel chat=chatmodelArrayList.get(position);
        holder.show_message.setText(chat.getMessage());
        holder.dateandtime.setText(chat.getTime());


    }

    @Override
    public int getItemCount() {
        return chatmodelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        if (chatmodelArrayList.get(position).getSender().equals(user.getId()))
        {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}