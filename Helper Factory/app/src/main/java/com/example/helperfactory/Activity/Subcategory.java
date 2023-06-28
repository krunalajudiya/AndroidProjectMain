package com.example.helperfactory.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Adapter.CategoryAdapter;
import com.example.helperfactory.Adapter.SubcategoryAdapter;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Subcategorymodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subcategory extends AppCompatActivity {


    List<Subcategorymodel.SubCategory> subCategoryArrayList=new ArrayList<>();
    RecyclerView subcategoryrec;
    ProgressDialog pdialog;
    String cat_id,cat_img;
    Toolbar toolbar;
    ImageView category_img;
    TextView nosubcategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.app_name));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    onBackPressed();
                }
            });
        }
        category_img=(ImageView)findViewById(R.id.category_img);
        subcategoryrec=(RecyclerView)findViewById(R.id.subcatrec);
        nosubcategory=(TextView)findViewById(R.id.nosubcategory);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        subcategoryrec.setLayoutManager(layoutManager);
        Bundle b=getIntent().getExtras();
        cat_id=b.getString("cat_id");
        cat_img=b.getString("cat_img");
        Glide.with(getApplicationContext()).load(Constant.IMAGE_URL+cat_img).into(category_img);
        getsubcategory();




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void getsubcategory()
    {
        pdialog = new ProgressDialog(Subcategory.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Subcategorymodel> call = Retrofitclient.getInstance().getMyApi().Subcategory("subcategory",cat_id);
        call.enqueue(new Callback<Subcategorymodel>() {
            @Override
            public void onResponse(Call<Subcategorymodel> call, Response<Subcategorymodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    nosubcategory.setVisibility(View.GONE);
                    Subcategorymodel subcategorymodel = response.body();
                    subCategoryArrayList = subcategorymodel.getData().getSubcategory();
                    SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(Subcategory.this, subCategoryArrayList);
                    subcategoryrec.setAdapter(subcategoryAdapter);
                    subcategoryAdapter.setOnItemClickListener(new SubcategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                            subCategoryArrayList.get(pos);
                            String sub_cat_detail;
                            Gson gson=new Gson();
                            sub_cat_detail=gson.toJson(subCategoryArrayList.get(pos));
                            Intent details=new Intent(getApplicationContext(),Subcategory_details.class);
                            details.putExtra("details",sub_cat_detail);
                            details.putExtra("cat_id",cat_id);
                            startActivity(details);

                        }

                    });
                    Log.d("array", String.valueOf(subCategoryArrayList.size()));
                }else{
                    subcategoryrec.setVisibility(View.GONE);
                    nosubcategory.setVisibility(View.VISIBLE);
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Subcategorymodel> call, Throwable t) {
                pdialog.dismiss();
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                subcategoryrec.setVisibility(View.GONE);
                nosubcategory.setVisibility(View.VISIBLE);
                Log.d("fffff",t.getMessage());
            }
        });
    }
}