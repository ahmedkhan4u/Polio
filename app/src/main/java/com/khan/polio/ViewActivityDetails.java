package com.khan.polio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivityDetails extends AppCompatActivity {
    String PostKey;
    ImageView imageView;
    TextView mName,mEmail,mCnic,mContact;
    DatabaseReference mRef;
    Button mDeleteButton,mViewActivitiesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        Intent intent = getIntent();
        PostKey = intent.getStringExtra("PostKey");
        mRef = FirebaseDatabase.getInstance().getReference().child("Workers");

        imageView = findViewById(R.id.viewDetailsImage);
        mName = findViewById(R.id.workerDetailsName);
        mEmail = findViewById(R.id.workerDetailsEmail);
        mCnic = findViewById(R.id.workerDetailsCnic);
        mContact = findViewById(R.id.workerDetailsContact);

        mViewActivitiesBtn = findViewById(R.id.viewDetailsBtnDetails);
        mDeleteButton = findViewById(R.id.viewDetailsBtnDelete);

        viewDetails();

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(PostKey).removeValue();
                goToHome();
            }
        });

        mViewActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWorkerActivities();
            }
        });

    }

    private void viewWorkerActivities() {
        Intent intent = new Intent(getApplicationContext(),AdminViewActivities.class);
        intent.putExtra("PostKey",PostKey);
        startActivity(intent);
    }

    private void goToHome() {
        finish();
    }

    private void viewDetails() {
        mRef.child(PostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                    String cnic = dataSnapshot.child("cnic").getValue().toString();
                    String contact = dataSnapshot.child("contact").getValue().toString();

                    mName.setText("Name: "+name);
                    mEmail.setText("Email: "+email);
                    Picasso.with(getApplicationContext()).load(imageUrl).into(imageView);
                    mCnic.setText("Cnic: "+cnic);
                    mContact.setText("Contact: "+contact);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
