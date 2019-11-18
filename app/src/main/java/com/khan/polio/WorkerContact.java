package com.khan.polio;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.khan.polio.Models.ViewActivityModel;
import com.khan.polio.Models.WorkerContactModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerContact extends Fragment {

    View mView;

    DatabaseReference mRef;
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<WorkerContactModel> firebaseRecyclerOptions;
    public WorkerContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_worker_contact, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerViewContact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<WorkerContactModel>()
                        .setQuery(mRef, WorkerContactModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WorkerContactModel, ViewHolder>(firebaseRecyclerOptions) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.worker_contact_list, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, WorkerContactModel model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setEmail("Email: "+model.getEmail());
                holder.setPhone("Phone: "+model.getPhone());
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        TextView contactNumber, mEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;

        }

        public void setEmail(String email) {
            mEmail = myView.findViewById(R.id.workerEmail);
            mEmail.setText(email);
        }

        public void setPhone(String phone) {
            contactNumber = myView.findViewById(R.id.workerContact);
            contactNumber.setText(phone);
        }
    }

}
