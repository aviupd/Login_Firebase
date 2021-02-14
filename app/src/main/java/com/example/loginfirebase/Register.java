package com.example.loginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Connect to xml resources
        mFullName = findViewById(R.id.Fullname);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.regButton);
        mLoginBtn = findViewById(R.id.loginText);

        fAuth = FirebaseAuth.getInstance();     //We are getting the current instance of the database from the firebase To perform the various operation on the database
        progressBar = findViewById(R.id.progressBar);

        //Check the user is already logged in or not or created already account then send them to main activity
        if(fAuth.getCurrentUser() != null){
            //Going to send the user in the main Activity
            //To do that start the new activity, inside this we need new intent from the current context send user to main activity
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        // What happened when register button is clicked?
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Get the entered value(string in this case) from the email and password field
                String email = mEmail.getText().toString().trim();  //Since we get an object so convert it into string using toString() and trim the data
                String password = mPassword.getText().toString().trim();

                //Validate the data
                //Check if the values are entered or not
                if(TextUtils.isEmpty(email)){     //If true then user has not entered anything in this field by passing the above email string
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be >= 6 Characters.");
                    return;
                }

                //If everything if correctly entered then we are going to
                progressBar.setVisibility(View.VISIBLE);

                //Register the user in firebase and add some event listener so that we know that registration is successful or not
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Process of registering the user is called task. If the task is successful that means we have successfully created the user.
                        //If not then we have encountered some error. So display the error.
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            //Going to send the user in the main Activity
                            //To do that start the new activity, inside this we need new intent from the current context send user to main activity
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else{
                            //Otherwise display message to user saying some error occurred
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //If progress bar is still running
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        //If already register then we send the user to login activity
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}