package com.queen.kartish.queen.AccountDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.queen.kartish.queen.Home.HomeActivity;
import com.queen.kartish.queen.R;

public class OrderConfirmationActivity extends AppCompatActivity {

    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
