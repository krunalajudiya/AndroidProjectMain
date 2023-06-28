package com.example.shreejicabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shreejicabs.Activity.LoginActivity;
import com.example.shreejicabs.Fragment.All_avaliability_display;
import com.example.shreejicabs.Fragment.Change_password;
import com.example.shreejicabs.Fragment.Contact_us;
import com.example.shreejicabs.Fragment.FraudList;
import com.example.shreejicabs.Fragment.Main;
import com.example.shreejicabs.Fragment.MyProfile;
import com.example.shreejicabs.Fragment.Privacy_Policy;
import com.example.shreejicabs.Fragment.Search_Package;
import com.example.shreejicabs.Fragment.notification_view;
import com.example.shreejicabs.Fragment.required_Vehicle;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.Session.UserSession;
import com.example.shreejicabs.Utils.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    UserSession userSession;
    BottomNavigationView bottomNavigationView;
    User user;
    String type_str;
    public static TextView title;
    Menu menu;
    boolean doubleBackToExitPressedOnce = false;
    AlertDialog.Builder builder,exitbuilder;
    AlertDialog exitdialog;
    private static long back_pressed;
    private static final int UPDATE_REQ = 1000;
    ImageView notification;
    TextView textCartItemCount;
    JSONParser jParser=new JSONParser();
    int mCartItemCount;
    public static String totalpoint_str,referralcode_str;
    String token,androidDeviceId;


    private static final String TAG_SUCCESS = "error";
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession=new UserSession(getApplicationContext());
        Log.d("dd11","gbgvg");
        Updateapp();
        if (!userSession.isLoggedIn())
        {
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }else{
            Gson gson=new Gson();
            user=gson.fromJson(userSession.getUserDetails(),User.class);
            type_str=user.getType();
            Log.d("user_id",user.getId());
        }
        setContentView(R.layout.activity_main);

        //getMenuInflater().inflate(R.menu.notification,menu);
        notification=findViewById(R.id.notification);
        title = (TextView)findViewById(R.id.appname);
        textCartItemCount=findViewById(R.id.cart_badge);
        //textCartItemCount.setText("3");
        new getbagecount().execute();
        new LoadUserDetails().execute();
        Log.d("value", String.valueOf(userSession.gettoken()));
        if (userSession.gettoken()){
            new gettoken().execute();
            Log.d("token","in");
        }
        exitbuilder=new AlertDialog.Builder(this);


        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//        bottomNavigationView.getMenu().removeItem(R.id.pickdrop);
        /*if (type_str=="Driver")
        {
            bottomNavigationView.getMenu().removeItem(R.id.packages);
        }*/

            title.setText("Required Vehicles");

        load(new required_Vehicle());
        inflateNavDrawer();
        if (NetworkUtil.getconnectivitystatus(MainActivity.this)) {

        }
        else{
            bottomNavigationView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Internet Not Connected Please Turn On", Toast.LENGTH_LONG).show();

        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("Notification");
                //new getbagecount().execute();
                loadFragment(new notification_view());
            }
        });



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.search:
                title.setText("TaxiTrip");
                loadFragment(new Search_Package());
                break;
            case R.id.pickdrop:
//                loadFragment(new Search_Package());
                title.setText("Required Vehicles");
                loadFragment(new required_Vehicle());
                break;
//            case R.id.chat:
//                title.setText("TaxiTrip");
//                loadFragment(new ChatUserlist());
//                break;
            case R.id.fraudlist:
                title.setText("Fraud List");
                loadFragment(new FraudList());
                break;
            case R.id.avalibility:
                title.setText("Available Taxi");
                loadFragment(new All_avaliability_display());

                break;
            case R.id.packages:
                title.setText("TaxiTrip");
                loadFragment(new MyProfile());
                break;

        }
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.notification,menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.notification:
//                loadFragment(new notification_view());
//                //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_LONG).show();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    class getbagecount extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "get_notification_count"));
                params.add(new BasicNameValuePair("client_id", user.getId()));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.d("succes", String.valueOf(success));
                            String total_notification = "0";
                            try {
                                total_notification = json.getString("total_notification");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("total notification", total_notification);
                            mCartItemCount = Integer.parseInt(total_notification);
                            //textCartItemCount.setText(String.valueOf(mCartItemCount));
                            //textCartItemCount.setVisibility(View.GONE);

                            if (mCartItemCount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            } else {
                                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                    textCartItemCount.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                    //setupBadge();

                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (success==200){

            }
        }
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            Log.d("mCartItemCount", String.valueOf(mCartItemCount));
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    private void inflateNavDrawer() {

        //set Custom toolbar to activity -----------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the AccountHeader ----------------------------------------------------------------

        //Profile Making
        IProfile profile = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            profile = new ProfileDrawerItem()
                   .withName("TaxiTrip")
                   .withIcon(R.drawable.logo);

        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(profile)
                .withCompactStyle(true)
                .withTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gen_black))
                .withCurrentProfileHiddenInList(true)
                .build();

        //Adding nav drawer items ------------------------------------------------------------------
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(R.drawable.home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.contact_us).withIcon(R.drawable.helpccenter);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.policy).withIcon(R.drawable.policy);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.shareapp).withIcon(R.drawable.share);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.changepassword).withIcon(R.drawable.lock);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.logout).withIcon(R.drawable.logout);




        //creating navbar and adding to the toolbar ------------------------------------------------
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withAccountHeader(headerResult)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1, item2, item3, item4, item5,item6
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position) {

                            case 1:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                title.setText("TaxiTrip");
                                loadFragment(new Main());
                                break;
                            case 2:
                                loadFragment(new Contact_us());
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                title.setText("Support");
                                break;
                            case 3:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                title.setText("TaxiTrip");
                                loadFragment(new Privacy_Policy());
                                break;
                            case 4:
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                try {
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                                    String shareMessage= "\nLet me recommend you this application\n\n";
                                    if (referralcode_str!=null){
                                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"+"Referral Code: "+referralcode_str;
                                    }else {
                                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                                    }

                                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                                } catch(Exception e) {
                                    //e.toString();
                                }
                                break;
                            case 5:
                                loadFragment(new Change_password());
                                if (result != null && result.isDrawerOpen()) {
                                    result.closeDrawer();
                                }
                                title.setText("Change Password");

                                break;
                            case 6:
                                userSession.logoutUser();
                                finish();
                                break;


                            default:
                                Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_LONG).show();

                        }

                        return true;
                    }
                })
                .build();

        //Setting crossfader drawer------------------------------------------------------------

        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));

        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();

        //build the view for the MiniDrawer
        View view = miniResult.build(this);

        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));

        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }

    class gettoken extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

            androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            token = FirebaseInstanceId.getInstance().getToken();
            Log.d("token",token);
            Log.d("device id",androidDeviceId);

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "updatetoken"));
                params.add(new BasicNameValuePair("client_id", user.getId()));
                params.add(new BasicNameValuePair("deviceuid", androidDeviceId));
                params.add(new BasicNameValuePair("devicetoken", token));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    userSession.settoken(false);
                    Log.d("token update","success");
                    //setupBadge();

                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (success==200){

            }
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof required_Vehicle)
        {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }else if (fragment instanceof Search_Package)
        {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }

    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            exitbuilder.setTitle("Are You Sure Want to Exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                    dialogInterface.cancel();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            exitdialog=exitbuilder.create();
            exitdialog.show();
            return;
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
            if (result != null && result.isDrawerOpen()) {
                result.closeDrawer();
            }
            else {
                Log.d("double", String.valueOf(doubleBackToExitPressedOnce));
                super.onBackPressed();
                return;
            }
        }
        /*this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
*/

    }

    class LoadUserDetails extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
//            pDialog.setMessage("Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "clientdetail"));
                params.add(new BasicNameValuePair("client_id", user.getId()));
                Log.d("id",user.getId());
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("UserDetail: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    referralcode_str=c1.getString("referral_code");
                    totalpoint_str=c1.getString("total_earn");




                    Log.d("referral_code",referralcode_str);

//                    referralcode.setText(referralcode_str);
//
//                    totalpoint.setText(totalpoint_str);



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//            layout_main.setVisibility(View.VISIBLE);
//            loading_screen.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQ){
            Toast.makeText(this,"Downloading Start",Toast.LENGTH_LONG).show();
            if (requestCode != RESULT_OK){
                Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Updateapp(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE
                            ,MainActivity.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
    }


}
