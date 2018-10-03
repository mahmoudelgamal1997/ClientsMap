package com.example2017.android.clientsmap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientLogin extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    EditText email,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);


        firebaseAuth=FirebaseAuth.getInstance();



        email=(EditText)findViewById(R.id.editText_email);
        password=(EditText)findViewById(R.id.editText_password);
        login=(Button) findViewById(R.id.but_login);

        if (checkInternetConnection(ClientLogin.this)) {


            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth != null) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {

                            Intent intent = new Intent(ClientLogin.this, ClientMap.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ClientLogin.this, "مرحبا بك  " , Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
                ;


        }else {
            Toast.makeText(ClientLogin.this,"your are offline", Toast.LENGTH_SHORT).show();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternetConnection(ClientLogin.this)) {

                    if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                        firebaseAuth.signInWithEmailAndPassword(String.valueOf(email.getText().toString()), password.getText().toString()).addOnCompleteListener(ClientLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(ClientLogin.this, ClientMap.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(ClientLogin.this, "done", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ClientLogin.this, "Email or Password isn't correct", Toast.LENGTH_SHORT).show();

                                }


                            }
                        });

                    }else{
                        Toast.makeText(ClientLogin.this,"ادخل الاميل والباسورد", Toast.LENGTH_SHORT).show();

                    }
                }else
                {
                    Toast.makeText(ClientLogin.this,"you are offline", Toast.LENGTH_SHORT).show();

                }


            }


        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkInternetConnection(ClientLogin.this)){
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection(ClientLogin.this)) {

            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth != null) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {

                            Intent intent = new Intent(ClientLogin.this, ClientMap.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ClientLogin.this, "مرحبا بك  " , Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            };

            firebaseAuth.addAuthStateListener(firebaseAuthListener);

        }
    }


    public  boolean checkInternetConnection(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }



}