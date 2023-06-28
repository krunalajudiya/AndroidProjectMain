package com.example.helperfactory.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Slidermodel;
import com.example.helperfactory.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter  extends  PagerAdapter {


    Activity context;
    List<Slidermodel.Slider> sliderList=new ArrayList<>();
    private LayoutInflater layoutInflater;


    public SliderAdapter(Activity context, List<Slidermodel.Slider> categoryArrayList) {
        this.context = context;
        this.sliderList=categoryArrayList;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_item,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.slider_img);
        Log.d("hello",sliderList.get(position).getPhoto());
        //imageView.setTag(position);
        //Glide.with(context).load(Constant.IMAGE_URL+sliderList.get(position).getPhoto()).into(imageView);
        Picasso.get().load(Constant.IMAGE_URL+sliderList.get(position).getPhoto()).into(imageView);
        //imageView.setImageDrawable(context.getDrawable(R.drawable.salonbanner));
        ViewPager vp= (ViewPager) container;
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
