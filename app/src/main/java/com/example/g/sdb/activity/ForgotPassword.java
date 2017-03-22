package com.example.g.sdb.activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.g.sdb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {


    //stating the variables that are going be used in this activity
    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;
    private RelativeLayout activity_forgot;


    //firebase (live database/server) authentication
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //linking the views with the id's that are in the xml file
        input_email = (EditText)findViewById(R.id.forgot_email);
        btnResetPass = (Button)findViewById(R.id.forgot_btn_reset);
        btnBack = (TextView)findViewById(R.id.forgot_btn_back);
        activity_forgot = (RelativeLayout)findViewById(R.id.activity_forgot_password);


        //setting on click listener
        btnResetPass.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();

    }
    //function checks which button is clicked and takes it to the specified activity
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.forgot_btn_back)
        {
            startActivity(new Intent(this,Login.class));
            finish();
        }
        else  if(view.getId() == R.id.forgot_btn_reset)
        {
            resetPassword(input_email.getText().toString());
        }
    }

    //function that resets password is it requireds to be reset
    //it automaticaly sends an email to the users email

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Snackbar snackBar = Snackbar.make(activity_forgot,"We have sent password to email: "+email,Snackbar.LENGTH_SHORT);
                            snackBar.show();
                        }
                        else{
                            Snackbar snackBar = Snackbar.make(activity_forgot,"Failed to send password",Snackbar.LENGTH_SHORT);
                            snackBar.show();
                        }
                    }
                });
    }
}
