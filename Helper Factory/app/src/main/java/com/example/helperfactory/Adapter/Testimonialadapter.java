package com.example.helperfactory.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Reviewmodel;
import com.example.helperfactory.Model.Slidermodel;
import com.example.helperfactory.Model.Testimonialmodel;
import com.example.helperfactory.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Testimonialadapter extends PagerAdapter {

    Activity context;
    List<Reviewmodel.Review> testimonialmodelList=new ArrayList<>();
    private LayoutInflater layoutInflater;


    public Testimonialadapter(Activity context, List<Reviewmodel.Review> testimonialmodelList) {
        this.context = context;
        this.testimonialmodelList=testimonialmodelList;
    }

    @Override
    public int getCount() {
        return testimonialmodelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_review_item,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.t_img);
        TextView t_name=(TextView)view.findViewById(R.id.t_name);
        TextView t_desc=(TextView)view.findViewById(R.id.t_desc);
        //Log.d("hello",testimonialmodelList.get(position).getT_img());
        t_name.setText(testimonialmodelList.get(position).getSub_name());
        t_desc.setText(testimonialmodelList.get(position).getMsg());
//        imageView.setImageResource(testimonialmodelList.get(position).getT_img());
        Picasso.get().load(Constant.IMAGE_URL+testimonialmodelList.get(position).getSub_image()).into(imageView);

        ViewPager vp=(ViewPager)container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);
    }


}
