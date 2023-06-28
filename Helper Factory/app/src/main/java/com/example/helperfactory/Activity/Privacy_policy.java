package com.example.helperfactory.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.helperfactory.R;

public class Privacy_policy extends AppCompatActivity {

    TextView contain_txt1,contain_txt2,contain_txt3,contain_txt4,contain_txt5,contain_txt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        contain_txt1=findViewById(R.id.contaion_txt1);
        contain_txt2=findViewById(R.id.contaion_txt2);
        contain_txt3=findViewById(R.id.contaion_txt3);
        contain_txt4=findViewById(R.id.contaion_txt4);
        contain_txt5=findViewById(R.id.contaion_txt5);
        contain_txt6=findViewById(R.id.contaion_txt6);

        String txt_policy1="Helper Factory is owner and operator of www.Helper Factory.in (“the Website”), Helper Factory’s Mobile Application for on demand home services (“Helper Factory App”),collectively, including all content provided by Helper Factory through the Helper Factory App and the Website “Helper Factory Service”.";
        String txt_policy2="You have right not to supply personally identifiable information. If You intend to view this Helper Factory Service and/or or share your data with the Helper Factory Service, it is solely at your own volition, risk and after reading and confirming it to this Privacy Policy.";
        String txt_policy3="At the time of using the Helper Factory Service you will share certain Personal Information with Helper Factory, Helper Factory respects your Personal Information. Such Personal Information may include your personal identifiable information such as your name, address, mobile number, your e-mail ids, your age, IP address, payment details and any other personal information which You may share in connection with the services.";
        String txt_policy4="When you use Helper Factory’s Service through Helper Factory App, Helper Factory may use Your mobile device’s ID (the unique identification assigned to a mobile device by the manufacturer). Helper Factory does this to store your preferences and track your use of Helper Factory App. You understand and agree that Helper Factory has every right to share such ID with third parties for its promotions, analysis, etc.";
        String txt_policy5="In case You participate in any other schemes, facilities, services provided by the Helper Factory Service, You will be required to provide additional information. Such information will also remain in Helper Factory’s database and will be considered as your Personal Information and will be treated as confidential.";

        String txt_policy6="Helper Factory needs to caution You that the Helper Factory Service could be vulnerable to hacking, virus attacks and your personal data may be at risk. Helper Factory takes all the necessary and reasonable caution to protect the Helper Factory Service and data.";

        String txt_policy7="Helper Factory may place a text file called a \"cookie\" in the browser files of your Mobile. The cookie itself does not contain Personal Information although it will enable the Helper Factory Service to relate your use of the Helper Factory Service to information that you have specifically and knowingly provided to Helper Factory.";
        String txt_policy8="The Cookies enables Helper Factory to remember your choices and some data field contents which you would be required to fill-in.";

        String txt_policy9="Helper Factory endeavours to protect your Personal Information. Helper Factory may share your Personal Information with its sponsors, service providers and its business partners. Personal Information could be shared so that you may receive newsletters, offers, information about new products, services, launches, facilities, schemes and other information, if applicable. The information collected from You and other users may be analysed in different manners. Helper Factory may also share such analysis with its service providers and its business partners.";
        String txt_policy10="In case Helper Factory is required to disclose your Personal Information in order to assist the Government Authority or in adherence to the Court or to protect the interest of the Helper Factory Service and/or any particular user(s), Helper Factory will disclose it without obtaining prior permission from you.";

        String txt_policy11="This Privacy Policy applies only to the Services. The Services may contain links to other web sites not operated or controlled by Helper Factory (the \"Third Party Sites\"). The policies and procedures we described here do not apply to the Third Party Sites. The links from the Services do not imply that Helper Factory endorses or has reviewed the Third Party Sites. We suggest contacting those sites directly for information on their privacy policies.";

        String txt_policy12="Helper Factory have and continue to have the right to modify, change or update Privacy Policy at any time. However, Helper Factory will use your Personal Information in a manner consistent with Privacy Policy at the time you shared Personal Information. You are encouraged to check the Helper Factory Service often to get updated about Privacy Policy.";


        contain_txt1.setText(txt_policy1+"\n\n"+txt_policy2+"\n\n"+txt_policy3+"\n\n"+txt_policy4+"\n\n"+txt_policy5);
        contain_txt2.setText(txt_policy6);
        contain_txt3.setText(txt_policy7+"\n\n"+txt_policy8);
        contain_txt4.setText(txt_policy9+"\n\n"+txt_policy10);
        contain_txt5.setText(txt_policy11);
        contain_txt6.setText(txt_policy12);
    }
}