package com.queen.kartish.queen.LoginRegister;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.queen.kartish.queen.Home.HomeActivity;
import com.queen.kartish.queen.R;

import static java.lang.Character.isDigit;
import static java.lang.Character.isUpperCase;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password;
    Button next;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseUser fuser;
    CoordinatorLayout coordinatorLayout;
    int nameactive=0,emailactive,passactive;
    TextView signup;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        next=findViewById(R.id.next);
        coordinatorLayout=findViewById(R.id.coordinator);
        progressBar=findViewById(R.id.progress);
        signup=findViewById(R.id.signup);
        back=findViewById(R.id.backbt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
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

                if((nameactive==1 && emailactive==1 && passactive==1)){
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.button_background2);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.button_background1);
                    next.setTextColor(Color.parseColor("#aeaeae"));
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
        });name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String pr=s.toString();

                if(pr.length()>0){
                    nameactive=1;
                }

                else
                    nameactive=0;

                if((nameactive==1 && emailactive==1 && passactive==1)){
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.button_background2);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.button_background1);
                    next.setTextColor(Color.parseColor("#aeaeae"));
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
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String pr=s.toString();

                if(pr.length()>5){

                    int x1=0,x2=0;

                    for(int i=0;i<pr.length();i++){
                        if(isUpperCase(pr.charAt(i)))
                            x1=1;
                        if(isDigit(pr.charAt(i)))
                            x2=1;
                    }

                    if(x1==1 && x2==1)
                        passactive=1;
                }

                else
                    passactive=0;

                if((nameactive==1 && emailactive==1 && passactive==1)){
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.button_background2);
                    next.setTextColor(Color.parseColor("#ffffff"));
                }

                else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.button_background1);
                    next.setTextColor(Color.parseColor("#aeaeae"));
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


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                signinUsingEmail(email.getText().toString(),password.getText().toString());
            }
        });
    }

    public void signinUsingEmail(String mail_id,String pass_wd){

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(mail_id,pass_wd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    fuser = firebaseAuth.getCurrentUser();

                    if(fuser==null)
                        return;

                    String userid=firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                    String mail_id = email.getText().toString();
                    String full_name = name.getText().toString();

                    databaseReference.child("Email_ID").setValue(mail_id);
                    databaseReference.child("Full_Name").setValue(full_name);

                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


                } else {

                    try {
                        throw (task.getException());
                    }

                    catch (FirebaseAuthWeakPasswordException weakPassword)
                    {
                        password.setError("Weak Password");
                        password.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                    {
                        email.setError("Invalid Email ID");
                        email.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        Snackbar snackbar=Snackbar.make(coordinatorLayout,"Email ID already exists", Snackbar.LENGTH_LONG);

                        View snackview=snackbar.getView();
                        snackview.setBackgroundColor(Color.rgb(255, 99, 71));
                        TextView textView=snackview.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        progressBar.setVisibility(View.GONE);

                        snackbar.show();
                    }
                    catch (Exception e)
                    {
                        email.setError("Invalid Email ID");
                        email.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
