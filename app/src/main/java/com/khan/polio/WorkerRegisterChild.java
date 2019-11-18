package com.khan.polio;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khan.polio.Models.WorkerRegisterChildModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerRegisterChild extends Fragment {

    View mView;
    FloatingActionButton floatingActionButton;
    ImageView btnLogout;
    FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    DatabaseReference mRef;
    String currentUser;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseRecyclerOptions <WorkerRegisterChildModel> firebaseRecyclerOptions;
    public WorkerRegisterChild() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_worker_register_child, container, false);

        mRecyclerView = mView.findViewById(R.id.workerChildRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.hasFixedSize();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Register Childs").child(currentUser);
        floatingActionButton = mView.findViewById(R.id.fab);
        btnLogout = mView.findViewById(R.id.log_out);
        mAuth = FirebaseAuth.getInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ChildRegisteration.class));

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                goToMainActivity();
            }
        });



        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<WorkerRegisterChildModel>()
                .setQuery(mRef,WorkerRegisterChildModel.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WorkerRegisterChildModel,ChildViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View myView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.worker_register_child_list,parent,false);
                return new ChildViewHolder(myView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChildViewHolder holder, int position, @NonNull WorkerRegisterChildModel model) {
                final String PostKey = getRef(position).getKey();
                holder.setHouseNumber("House No: "+model.getHouseNumber());
                holder.setName("Name: "+model.getName());
                holder.setFatherName("Father Name: "+model.getFatherName());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),ChildData.class);
                        intent.putExtra("PostKey",PostKey);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void goToMainActivity() {
        startActivity(new Intent(getActivity(),MainActivity.class));
        getActivity().finish();
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
