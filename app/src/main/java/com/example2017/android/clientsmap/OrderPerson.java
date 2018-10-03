package com.example2017.android.clientsmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderPerson extends AppCompatActivity {


    ListView listView;
    DatabaseReference users;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_person);

        users= FirebaseDatabase.getInstance().getReference().child("username");
        listView=(ListView)findViewById(R.id.listView_person);

        Intent myintent=getIntent();
        key =   myintent.getStringExtra("key");


        final FirebaseListAdapter<String>firebaseListAdapter=new FirebaseListAdapter<String>(
                this,
                String.class,
                R.layout.listview_items,
                users


        ) {
            @Override
            protected void populateView(View v, final String model, int position) {

                TextView textView=(TextView)v.findViewById(R.id.textView_plcae);

                textView.setText(model);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(key.equals("orders")) {
                            Intent intent = new Intent(OrderPerson.this, Orders.class);
                            intent.putExtra("name", model);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(OrderPerson.this, Reports.class);
                            intent.putExtra("name", model);
                            startActivity(intent);
                        }


                    }
                });

            }
        };


        listView.setAdapter(firebaseListAdapter);
    }
}
