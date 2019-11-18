package com.khan.polio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewChildDetails extends AppCompatActivity {

    private String PostKey,currentUser,name,fatherName,age,houseNo,homeAddress,status,date;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    TextView mName,mFatherName,mAge,mHouseNumber,mHomeAddress,mDate;

    RadioButton btnDone,btnLeft;
    Button btnDeleteRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_child_details);

        Intent intent = getIntent();
        PostKey = intent.getStringExtra("PostKey");
        currentUser = intent.getStringExtra("UserId");
        mRef = FirebaseDatabase.getInstance().getReference().child("Register Childs")
                .child(currentUser).child(PostKey);

        mName = findViewById(R.id.childName);
        mFatherName = findViewById(R.id.childFatherName);
        mAge = findViewById(R.id.childAge);
        mHouseNumber = findViewById(R.id.childHouseNumber);
        mHomeAddress = findViewById(R.id.childHomeAddress);
        mDate = findViewById(R.id.childRegDate);
        btnDone = findViewById(R.id.childBtnDone);
        btnLeft = findViewById(R.id.childBtnLeft);
        btnDeleteRecord=findViewById(R.id.childBtnDeleteRecord);
        btnDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.removeValue();
                Toast.makeText(getApplicationContext(), "Child Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name = dataSnapshot.child("name").getValue().toString();
                    fatherName = dataSnapshot.child("fatherName").getValue().toString();
                    age = dataSnapshot.child("age").getValue().toString();
                    houseNo = dataSnapshot.child("houseNumber").getValue().toString();
                    homeAddress = dataSnapshot.child("homeAddress").getValue().toString();
                    status = dataSnapshot.child("status").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    mName.setText("Child Name: "+name);
                    mFatherName.setText("Father Name: "+fatherName);
                    mAge.setText("Age: "+age);
                    mHouseNumber.setText("House No: "+houseNo);
                    mHomeAddress.setText("Home Address: "+homeAddress);
                    mDate.setText("Registration Date: "+date);

                    if (status.equals("Done")){
                        btnDone.setChecked(true);
                        btnLeft.setClickable(false);

                    }
                    else {
                        btnLeft.setChecked(true);
                        btnDone.setClickable(false);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "No Data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
