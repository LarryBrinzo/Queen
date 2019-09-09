package com.queen.kartish.queen.LoginRegister;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.queen.kartish.queen.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    Button reset;
    FirebaseAuth firebaseauth;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progress;
    int emailactive=0;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseauth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        coordinatorLayout=findViewById(R.id.coordinator);
        reset=findViewById(R.id.next);
        back=findViewById(R.id.backbt);
        progress=findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String pr=s.toString();

                if(pr.length()>0){
                    emailactive=1;
                }

                else
                    emailactive=0;

                if(emailactive==1){
                    reset.setEnabled(true);
                    reset.setBackgroundResource(R.drawable.button_background2);
                    reset.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    reset.setEnabled(false);
                    reset.setBackgroundResource(R.drawable.button_background1);
                    reset.setTextColor(Color.parseColor("#aeaeae"));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail_id=email.getText().toString();
                progress.setVisibility(View.VISIBLE);
                    firebaseauth.sendPasswordResetEmail(mail_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Snackbar snackbar=Snackbar.make(coordinatorLayout,"Password Reset Link has been sent to your mail",Snackbar.LENGTH_LONG);
                                progress.setVisibility(View.GONE);


                                View snackview=snackbar.getView();
                                snackview.setBackgroundColor(Color.rgb(255, 99, 71));
                                TextView textView=snackview.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);

                                snackbar.show();
                            }
                            else{
                                Snackbar snackbar=Snackbar.make(coordinatorLayout,"Account not registered",Snackbar.LENGTH_LONG);
                                progress.setVisibility(View.GONE);

                                View snackview=snackbar.getView();
                                snackview.setBackgroundColor(Color.rgb(255, 99, 71));
                                TextView textView=snackview.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);

                                snackbar.show();
                            }
                        }
                    });

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
