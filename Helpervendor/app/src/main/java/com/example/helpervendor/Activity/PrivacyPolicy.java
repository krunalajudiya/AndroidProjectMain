package com.example.helpervendor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.helpervendor.R;

public class PrivacyPolicy extends AppCompatActivity {

    TextView header,content,address_t,number,email;
    String header_txt,content_txt,address_txt,number_txt,email_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        header=findViewById(R.id.header);
        content=findViewById(R.id.contant);
        address_t=findViewById(R.id.address_t);
        number=findViewById(R.id.number);
        email=findViewById(R.id.email);

        header_txt="VENDOR AGREEMENT";
        content_txt="1. Time period\n" +
                "You have to work with our company for 1 year minimum. In case of \n" +
                "emergency, we can cancel our contract at a time. After a year we both are \n" +
                "independence for work as a collaborator. Specially note for vendor, you \n" +
                "can’t join with any other agency or company for 1 year, (who provide home \n" +
                "service to customer) after leave our company.\n" +
                "2. Availability\n" +
                "You have to available at the point of work within 1 hour after receiving \n" +
                "appointment. \n" +
                "3. Work satisfaction\n" +
                "Hope from vendor side, you will be provided satisfy work as per \n" +
                "expectation of customer. (Never give the chance to complain to the \n" +
                "customer)\n" +
                "4. Maintain the quality\n" +
                "Hope from vendor, never compromise with work quality.\n" +
                "5. Consumable parts\n" +
                "In case of any consumable parts, you have to add charge of parts as \n" +
                "genuine.\n" +
                "6. Work guarantee\n" +
                "We will be provided work satisfaction guarantee for 30 days to customer, \n" +
                "you have to focus specially for this point.\n" +
                "7. Reporting\n" +
                "Vendor have to reporting must to our company after completion of work.\n" +
                "8. Economically bondage\n" +
                "Company will be pay to vendor as per commitment at a time of contract.\n" +
                "9. Identification of employee\n" +
                "Vendor will be provided full details of employee who is going for solve \n" +
                "fault / service. (ID, Current photograph and mobile number)\n" +
                "10. Accounting\n" +
                "All accounting will be completed after end of month. Company will be pay \n" +
                "to vendor up to 10th date of next month.\n" +
                "11. Dress code\n" +
                "Our company will be provided dress code for your employee, who is going \n" +
                "for solve fault / service.\n" +
                "12. Clarification\n" +
                "In case of any wrong behaviour of vendor or employee, we will be taking \n" +
                "legal action as per Indian government rules.";

        address_txt="Plot no.57/1, shiv township, bedi ring road,\n" +
                "Jamnagar, Gujarat 361002.";

        number_txt="Call: 9016716398\n" +
                "         8000088282";
        email_txt="Email: helperfactory@gmail.com\n" +
                "Web: www.helperfactory.com";


        header.setText(header_txt);
        content.setText(content_txt);
        address_t.setText(address_txt);
        number.setText(number_txt);
        email.setText(email_txt);
    }
}