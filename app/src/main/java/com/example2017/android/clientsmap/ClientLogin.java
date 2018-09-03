package com.example2017.android.clientsmap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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


        firebaseAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                if (user!=null)
                {

                    Intent intent=new Intent(ClientLogin.this,ClientMap.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(ClientLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Intent intent=new Intent(ClientLogin.this,ClientMap.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(ClientLogin.this, "done", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);

    }
}