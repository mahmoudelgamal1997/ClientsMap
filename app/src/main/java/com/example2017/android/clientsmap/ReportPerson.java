package com.example2017.android.clientsmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportPerson extends AppCompatActivity {

    ListView listView;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_person);

        users= FirebaseDatabase.getInstance().getReference().child("username");
        listView=(ListView)findViewById(R.id.listView_person);


        final FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(
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

                        Intent intent=new Intent(ReportPerson.this,Reports.class);
                        intent.putExtra("name",model);
                        startActivity(intent);



                    }
                });

            }
        };


        listView.setAdapter(firebaseListAdapter);
    }
}
