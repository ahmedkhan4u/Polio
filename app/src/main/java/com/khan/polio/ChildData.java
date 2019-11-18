package com.khan.polio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khan.polio.Models.ChildRegistrationModel;
import com.khan.polio.Models.GetChildDate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChildData extends AppCompatActivity {
    private String PostKey,currentUser,name,fatherName,age,houseNo,homeAddress,status,date;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    TextView mName,mFatherName,mAge,mHouseNumber,mHomeAddress,mDate;
    Date newDate;
    RecyclerView mRecyclerView;
    RadioButton btnDone,btnLeft;
    Button btnDeleteRecord;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recyclerViewDate);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.hasFixedSize();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        PostKey = intent.getStringExtra("PostKey");

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
        newDate = new Date();
        btnDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.removeValue();
                Toast.makeText(ChildData.this, "Child Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        gettingDate();

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
                    mDate.setText("Date: "+date+ " Status: "+status+"\n");

                }
                else {
                    Toast.makeText(ChildData.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnDone.isChecked()){
                    String txtDone = "Done";
                    Toast.makeText(ChildData.this, "Done", Toast.LENGTH_SHORT).show();
                    final String currentDate = newDate.toLocaleString();

                    Map map = new HashMap<String,String>();
                    map.put("date",currentDate);
                    map.put("status",txtDone);

                    mRef.child(PostKey).child(currentDate).setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                btnDone.setChecked(false);
                                Toast.makeText(ChildData.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ChildData.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLeft.isChecked()){
                    String txtLeft = "Left";
                    Toast.makeText(ChildData.this, "Left", Toast.LENGTH_SHORT).show();
                    String currentDate = newDate.toLocaleString();

                    Map map = new HashMap<String,String>();
                    map.put("date",currentDate);
                    map.put("status",txtLeft);

                    mRef.child(PostKey).child(currentDate).setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        btnLeft.setChecked(false);
                                        Toast.makeText(ChildData.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ChildData.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        
    }

    private void gettingDate() {

        DatabaseReference mPostRef = FirebaseDatabase.getInstance().getReference().child("Register Childs")
                .child(currentUser).child(PostKey).child(PostKey);
        FirebaseRecyclerOptions<GetChildDate> firebaseRecyclerOptions
                = new FirebaseRecyclerOptions.Builder<GetChildDate>()
                .setQuery(mPostRef,GetChildDate.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<GetChildDate,mViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.register_child_date_list,viewGroup,false);
                return new mViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull mViewHolder holder, int position, @NonNull GetChildDate model) {
                holder.setDate("Date: "+model.getDate());
                holder.setStatus("Status: "+model.getStatus());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public static class mViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView mDate,mStatus;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDate(String date) {
            mDate = mView.findViewById(R.id.text_view_date);
            mDate.setText(date);
        }

        public void setStatus(String status) {
            mStatus = mView.findViewById(R.id.text_view_status);
            mStatus.setText(status);
        }

    }
}
