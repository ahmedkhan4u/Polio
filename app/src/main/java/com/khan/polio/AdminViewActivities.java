package com.khan.polio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khan.polio.Models.WorkerRegisterChildModel;

public class AdminViewActivities extends AppCompatActivity {

    RecyclerView mRecyclerView;
    String UserId;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerOptions<WorkerRegisterChildModel>firebaseRecyclerOptions;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    DatabaseReference mRef,mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_activities);

        Intent intent = getIntent();
        UserId = intent.getStringExtra("PostKey");
        mRecyclerView = findViewById(R.id.viewActivityRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.hasFixedSize();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRef = FirebaseDatabase.getInstance().getReference().child("Register Childs")
                .child(UserId);

        Toast.makeText(this, UserId, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<WorkerRegisterChildModel>()
                .setQuery(mRef,WorkerRegisterChildModel.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WorkerRegisterChildModel,WorkerRegisterChild.ChildViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public WorkerRegisterChild.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View myView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.worker_register_child_list,parent,false);
                return new WorkerRegisterChild.ChildViewHolder(myView);
            }

            @Override
            protected void onBindViewHolder(@NonNull WorkerRegisterChild.ChildViewHolder holder, int position, @NonNull WorkerRegisterChildModel model) {
                final String PostKey = getRef(position).getKey();
                holder.setHouseNumber("House No: "+model.getHouseNumber());
                holder.setName("Name: "+model.getName());
                holder.setFatherName("Father Name: "+model.getFatherName());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),AdminViewChildDetails.class);
                        intent.putExtra("PostKey",PostKey);
                        intent.putExtra("UserId",UserId);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView Name,fName,HouseNumber;
        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name) {
            Name = mView.findViewById(R.id.wRegisterChildName);
            Name.setText(name);
        }
        public void setFatherName(String fatherName) {
            fName = mView.findViewById(R.id.wRegisterChildFName);
            fName.setText(fatherName);
        }
        public void setHouseNumber(String houseNumber) {
            HouseNumber = mView.findViewById(R.id.wRegisterChildFHNo);
            HouseNumber.setText(houseNumber);
        }
    }
}
