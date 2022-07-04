package com.example.adminbtbs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView adminName;
    private Button btnLocation, btnAddBus, btnTickets;
    private ProgressDialog progressDialog, progressLocation;
    private String Name, Location, PinCode;
    private DatabaseReference databaseAddress;
    private EditText edtLocation,edtPinNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressLocation = new ProgressDialog(this);

        Initialize();
        Buttons();

        progressDialog.setTitle("Configuring environment");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void Initialize(){
        adminName = findViewById(R.id.name);
        //EditText
        edtLocation = findViewById(R.id.addLocation);
        edtPinNo = findViewById(R.id.addLocationPin);

        //Button
        btnLocation = findViewById(R.id.btnLocation);
        btnAddBus = findViewById(R.id.btnBus);
        btnTickets = findViewById(R.id.btnTicket);

        DatabaseReference databaseName = FirebaseDatabase.getInstance().getReference().child("Admin");
        databaseAddress = FirebaseDatabase.getInstance().getReference().child("Location");

        databaseName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Name = dataSnapshot.child("Name").getValue().toString();
                adminName.setText(Name);
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,Name,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Buttons(){
        btnAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEdt();
                Intent in = new Intent(MainActivity.this,AddBus.class);
                startActivity(in);
            }
        });

        btnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,AdminTickets.class);
                startActivity(in);
            }
        });
        
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location = edtLocation.getText().toString();
                PinCode = edtPinNo.getText().toString();
                if(!Location.isEmpty() && !PinCode.isEmpty())
                {
                    progressLocation.setTitle("Registering");
                    progressLocation.setCancelable(false);
                    progressLocation.show();
                    SetLocation(Location,PinCode);
                }else
                {
                    Toast.makeText(MainActivity.this,"Please fill each box",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SetLocation(String Location,String PinCode){
        databaseAddress = FirebaseDatabase.getInstance().getReference().child("Location").child(PinCode);
        HashMap<String, String> loc = new HashMap<>();
        loc.put("Location_pin",PinCode);
        loc.put("Place",Location.toUpperCase());
        databaseAddress.setValue(loc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Location Updated in database",Toast.LENGTH_SHORT).show();
                    progressLocation.dismiss();
                    removeEdt();
                }else {
                    Toast.makeText(MainActivity.this,"Location not updated ",Toast.LENGTH_SHORT).show();
                    progressLocation.dismiss();
                }
            }
        });
    }

    private void removeEdt(){
        edtPinNo.setText("");
        edtLocation.setText("");
    }
}