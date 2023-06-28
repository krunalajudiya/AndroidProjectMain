package com.technocometsolutions.salesdriver.fregment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.technocometsolutions.salesdriver.R;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class FormStep2 extends Fragment implements Step , BlockingStep {

    private static final int PICK_FILE_REQUEST = 1001;
    private static final int PICK_FILE_REQUEST1 = 1002;
    private static final int PERMISSION_CODE=1001;
    EditText dateofbirth,dateofmarriage,proprietorname,mobilenumber;
    DatePickerDialog[] datePickerDialog;
    Button addmorepartner;
    LinearLayout addpartnerlayout;
    ImageView photoofproprietor,signatureofproprietor;
    String dateofbirth_txt,dateofmarriage_txt,proprietorname_txt,mobilenumber_txt;
    Uri photoofproprietor_uri,signatureofproprietor_uri;
    int patener=0;
    int idconst=100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step2, container, false);

        dateofbirth=view.findViewById(R.id.dateofbirth);
        dateofmarriage=view.findViewById(R.id.dateofmarriage);
        addmorepartner=view.findViewById(R.id.addmorepartner);
        addpartnerlayout=view.findViewById(R.id.addpartnercontainer);
        proprietorname=view.findViewById(R.id.proprietorname);
        mobilenumber=view.findViewById(R.id.mobilenumber);
        photoofproprietor=view.findViewById(R.id.photoofproprietor);
        signatureofproprietor=view.findViewById(R.id.signatureofproprietor);


        Bundle bundle=this.getArguments();
        if (bundle!=null){
            Log.d("dataoffragment",bundle.getString("step1data"));
        }





        photoofproprietor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile();
                    }
                }else {
                    ChooseFile();
                }
            }
        });
        signatureofproprietor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile1();
                    }
                }else {
                    ChooseFile1();
                }
            }
        });
        addmorepartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patener++;
                addpartner();
            }
        });
        datePickerDialog= new DatePickerDialog[1];
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog[0] = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateofbirth.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog[0].show();
            }
        });

        dateofmarriage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog[0] = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateofmarriage.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog[0].show();
            }
        });

        return view;
    }
    void getalldata(){
        if (dateofmarriage.getText().toString().trim().length()>0 && dateofbirth.getText().toString().trim().length()>0 &&
                proprietorname.getText().toString().trim().length()>0 && mobilenumber.getText().toString().trim().length()>0 ){

            dateofbirth_txt=dateofbirth.getText().toString().trim();
            dateofmarriage_txt=dateofmarriage.getText().toString().trim();
            proprietorname_txt=proprietorname.getText().toString().trim();
            mobilenumber_txt=mobilenumber.getText().toString().trim();


        }
        if (patener>0){

        }

    }

    public void addpartner(){

        LinearLayout linearLayout=new LinearLayout(getContext());
        LinearLayout.LayoutParams linearLayoutlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutlayoutParams);
        linearLayoutlayoutParams.topMargin=20;
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        View border=new View(getActivity());
        LinearLayout.LayoutParams borderlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,4);
        border.setLayoutParams(borderlayoutParams);
        border.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        linearLayout.addView(border);

        EditText nameofpatener=new EditText(getActivity());
        LinearLayout.LayoutParams nameofpatenerlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        nameofpatener.setId((idconst+=1));
        nameofpatener.setLayoutParams(nameofpatenerlayoutParams);
        nameofpatenerlayoutParams.topMargin=30;
        nameofpatener.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nameofpatener.setHint("Name of Patener");
        Log.d("id", String.valueOf(nameofpatener.getId()));
        linearLayout.addView(nameofpatener);

        EditText mobilenumber=new EditText(getActivity());
        LinearLayout.LayoutParams mobilenumberlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mobilenumber.setId((idconst+=1));
        mobilenumber.setLayoutParams(mobilenumberlayoutParams);
        mobilenumberlayoutParams.topMargin=10;
        mobilenumber.setHint("Mobile Number");
        mobilenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        Log.d("id", String.valueOf(mobilenumber.getId()));
        linearLayout.addView(mobilenumber);

        TextView dateofbirthtext=new TextView(getActivity());
        LinearLayout.LayoutParams dateofbirthtextlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dateofbirthtext.setLayoutParams(dateofbirthtextlayoutParams);
        dateofbirthtextlayoutParams.topMargin=10;
        dateofbirthtext.setText("Date of Birth");
        dateofbirthtext.setTextSize(15);
        dateofbirthtext.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        linearLayout.addView(dateofbirthtext);

        EditText dateofbirth=new EditText(getActivity());
        LinearLayout.LayoutParams dateofbirthlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dateofbirth.setLayoutParams(dateofbirthlayoutParams);
        dateofbirth.setId((idconst+=1));
        dateofbirth.setHint("DD/MM/YYYY");
        dateofbirth.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(dateofbirth);

        TextView dateofmarriagetext=new TextView(getActivity());
        LinearLayout.LayoutParams dateofmarriagetextlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dateofmarriagetext.setLayoutParams(dateofmarriagetextlayoutParams);
        dateofmarriagetextlayoutParams.topMargin=10;
        dateofmarriagetext.setText("Date of Marriage");
        dateofmarriagetext.setTextSize(15);
        dateofmarriagetext.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        linearLayout.addView(dateofmarriagetext);

        EditText dateofmarriage=new EditText(getActivity());
        LinearLayout.LayoutParams dateofmarriagelayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dateofmarriage.setLayoutParams(dateofmarriagelayoutParams);
        dateofmarriage.setId((idconst+=1));
        dateofmarriage.setHint("DD/MM/YYYY");
        dateofmarriage.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(dateofmarriage);

        LinearLayout layouttext=new LinearLayout(getActivity());
        LinearLayout.LayoutParams layouttextlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layouttext.setLayoutParams(layouttextlayoutParams);
        layouttextlayoutParams.topMargin=10;
        layouttext.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(layouttext);

        TextView phototext=new TextView(getActivity());
        LinearLayout.LayoutParams phototextlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        phototext.setLayoutParams(phototextlayoutParams);
        phototext.setTextSize(15);
        phototext.setText("Photo");
        phototext.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        phototext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layouttext.addView(phototext);

        TextView signaturetext=new TextView(getActivity());
        LinearLayout.LayoutParams signaturetextlayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        signaturetext.setLayoutParams(signaturetextlayoutParams);
        signaturetext.setTextSize(15);
        signaturetext.setText("Signature");

        signaturetext.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
        signaturetext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layouttext.addView(signaturetext);


        LinearLayout layoutsymbol=new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutsymbollayoutParams=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutsymbol.setLayoutParams(layoutsymbollayoutParams);
        layoutsymbollayoutParams.topMargin=10;
        layoutsymbol.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(layoutsymbol);

        ImageView photoimageview=new ImageView(getActivity());
        LinearLayout.LayoutParams photoimageviewlayoutParams=
                new LinearLayout.LayoutParams(100,100,1.0f);
        photoimageview.setLayoutParams(photoimageviewlayoutParams);
        photoimageview.setId((idconst+=1));
        photoimageview.setImageResource(R.drawable.ic_photo_upload);
        layoutsymbol.addView(photoimageview);


        ImageView signatureimageview=new ImageView(getActivity());
        LinearLayout.LayoutParams signatureimageviewlayoutParams=
                new LinearLayout.LayoutParams(100,100,1.0f);
        signatureimageview.setLayoutParams(signatureimageviewlayoutParams);
        signatureimageview.setId((idconst+=1));
        signatureimageview.setImageResource(R.drawable.ic_photo_upload);
        layoutsymbol.addView(signatureimageview);

        addpartnerlayout.addView(linearLayout);

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        Log.d("ffclick","nextClick");

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        callback.goToPrevStep();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private void ChooseFile() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FILE_REQUEST);

    }
    private void ChooseFile1() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FILE_REQUEST1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ff","enter");
        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST ){
            photoofproprietor.setImageURI(data.getData());
            photoofproprietor_uri=data.getData();
        }else if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST1 )
        {
            signatureofproprietor.setImageURI(data.getData());
            signatureofproprietor_uri=data.getData();
        }


    }
}