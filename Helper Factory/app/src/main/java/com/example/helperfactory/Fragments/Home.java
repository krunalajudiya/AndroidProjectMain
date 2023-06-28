package com.example.helperfactory.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperfactory.Activity.Subcategory;
import com.example.helperfactory.Adapter.CategoryAdapter;
import com.example.helperfactory.Adapter.Offeradapter;
import com.example.helperfactory.Adapter.SliderAdapter;
import com.example.helperfactory.Adapter.Testimonialadapter;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Offermodel;
import com.example.helperfactory.Model.Reviewmodel;
import com.example.helperfactory.Model.Slidermodel;
import com.example.helperfactory.Model.Testimonialmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.github.demono.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Categorymodel.Category> categorymodelArrayList=new ArrayList<>();
    List<Offermodel.Offer> offerArrayList=new ArrayList<>();
    List<Reviewmodel.Review> testimonialmodelList=new ArrayList<>();
    RecyclerView categoryrec,offerrec;
    ViewPager slider;
    ViewPager testimonialslider;
    List<Slidermodel.Slider> sliderList=new ArrayList<>();
    ProgressDialog pdialog;
    private int currentPage = 0;
    Timer timer;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        categoryrec=(RecyclerView)view.findViewById(R.id.categoryrec);
        offerrec=(RecyclerView)view.findViewById(R.id.offerrec);
        slider=(ViewPager) view.findViewById(R.id.slider);
        testimonialslider=(ViewPager)view.findViewById(R.id.testimoniallider);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        categoryrec.setLayoutManager(layoutManager);
        categoryrec.suppressLayout(true);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getActivity(),2);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        offerrec.setLayoutManager(linearLayoutManager);
        offerrec.suppressLayout(true);
        getslider();
//        slider.
//        testimonialmodelList.add(new Testimonialmodel("Bilanhashmi","One stop shop solution for all your needs at your steps.",R.drawable.image1));
//        testimonialmodelList.add(new Testimonialmodel("AdityaRpatro","Helper factory you guys rock,dint expect this exceptional service even in this pandamic situation",R.drawable.image1));
//        testimonialmodelList.add(new Testimonialmodel("sankeyboy","Had a short circuit in the apartment Electrician visit scheduled in a flash.Now things are back to normal.Thanks Helper Factory",R.drawable.image1));
        testimonialslider.setPageMargin(24);
//        Testimonialadapter testimonialadapter=new Testimonialadapter(getActivity(),testimonialmodelList);
//        testimonialslider.setAdapter(testimonialadapter);
        getReview();

        setupAutoPager();
        return view;

    }
    public  void getcategory()
    {

        Call<Categorymodel> call = Retrofitclient.getInstance().getMyApi().Category("categorydetails");
        call.enqueue(new Callback<Categorymodel>() {
            @Override
            public void onResponse(Call<Categorymodel> call, Response<Categorymodel> response) {
                //pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                getoffer();
                if (response.isSuccessful()) {
                    Categorymodel categorymodel = response.body();
                    categorymodelArrayList = categorymodel.getData().getCategory();
                    CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categorymodelArrayList);
                    categoryrec.setAdapter(categoryAdapter);
                    categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {
                            Intent subcat = new Intent(getActivity(), Subcategory.class);
                            subcat.putExtra("cat_id", categorymodelArrayList.get(pos).getC_id());
                            subcat.putExtra("cat_img",categorymodelArrayList.get(pos).getC_img());
                            startActivity(subcat);
                            Log.d("dddd", categorymodelArrayList.get(pos).getC_id());
                        }
                    });
                    Log.d("array", String.valueOf(categorymodelArrayList.size()));
                }

                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Categorymodel> call, Throwable t) {
               // pdialog.dismiss();
                //handle error or failure cases here
                getoffer();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }
    public  void getoffer()
    {

        Call<Offermodel> call = Retrofitclient.getInstance().getMyApi().Offer_list("offer_list");
        call.enqueue(new Callback<Offermodel>() {
            @Override
            public void onResponse(Call<Offermodel> call, Response<Offermodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Offermodel offermodel = response.body();
                    offerArrayList=offermodel.getData().getOfferList();
                    Offeradapter offeradapter=new Offeradapter(getActivity(),offerArrayList);
                    offerrec.setAdapter(offeradapter);
                    Log.d("array", String.valueOf(offerArrayList.size()));
                }

                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Offermodel> call, Throwable t) {
                pdialog.dismiss();
                //handle error or failure cases here
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }
    public  void getslider()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Slidermodel> slidercall = Retrofitclient.getInstance().getMyApi().Slider("slider");
        slidercall.enqueue(new Callback<Slidermodel>() {
            @Override
            public void onResponse(Call<Slidermodel> call, Response<Slidermodel> response) {
                getcategory();
                //In this point we got our hero list
                //thats damn easy right ;)
                Slidermodel slidermodel=response.body();

                if (response.isSuccessful()) {
                    sliderList = slidermodel.getData().getSlider();

                    SliderAdapter sliderAdapter = new SliderAdapter(getActivity(),sliderList);
                    slider.setAdapter(sliderAdapter);

                    Log.d("array", String.valueOf(slidermodel.getData().getSlider().size()));
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Slidermodel> call, Throwable t) {
                getcategory();
                //handle error or failure cases here
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }

    private void setupAutoPager()
    {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run()
            {
                slider.setCurrentItem(currentPage, true);
                if(currentPage == sliderList.size())
                {
                    currentPage = 0;
                }
                else
                {
                    ++currentPage ;
                }
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 2500);
    }
    public void getReview()
    {
//        pdialog=new ProgressDialog(getActivity());
//        pdialog.setCancelable(false);
//        pdialog.setMessage("Loading...");
//        pdialog.setIndeterminate(false);
//        pdialog.show();

        Call<Reviewmodel> reviewmodelCall=Retrofitclient.getInstance().getMyApi().Fetch_Review("fetch_review");
        reviewmodelCall.enqueue(new Callback<Reviewmodel>() {
            @Override
            public void onResponse(Call<Reviewmodel> call, Response<Reviewmodel> response) {
                if (response.isSuccessful()){
                    Reviewmodel reviewmodel=response.body();
                    testimonialmodelList=reviewmodel.getData().getReviewList();
                    Testimonialadapter testimonialadapter=new Testimonialadapter(getActivity(),testimonialmodelList);
                    testimonialslider.setAdapter(testimonialadapter);

                }
            }

            @Override
            public void onFailure(Call<Reviewmodel> call, Throwable t) {

            }
        });
    }
}