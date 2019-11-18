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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khan.polio.Models.ViewComplainModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewComplaint extends Fragment {


    DatabaseReference mRef;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    View mView;
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    public ViewComplaint() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_view_complaint, container, false);
        mRef = FirebaseDatabase.getInstance().getReference().child("Complains");
        mRecyclerView = mView.findViewById(R.id.complainRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.hasFixedSize();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ViewComplainModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions
                        .Builder<ViewComplainModel>()
                        .setQuery(mRef,ViewComplainModel.class)
                .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ViewComplainModel,ComplainViewHolder>(firebaseRecyclerOptions){

                    @NonNull
                    @Override
                    public ComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View myView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.view_complain_list,parent, false);
                        return new ComplainViewHolder(myView);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ComplainViewHolder holder, int position, @NonNull ViewComplainModel model) {

                        holder.setTitle("Title: "+model.getTitle());
                        holder.setAddress("Address: "+model.getAddress());
                        holder.setDescription("Description: "+model.getDescription());
                        holder.setDate("Date: "+model.getDate());
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

    public static class ComplainViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView mTitle,mAddress,mDesctiption,mDate;
        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title) {
            mTitle = mView.findViewById(R.id.viewComplainTitle);
            mTitle.setText(title);
        }
        public void setAddress(String address) {
            mAddress = mView.findViewById(R.id.viewComplainAddress);
            mAddress.setText(address);
        }
        public void setDescription(String description) {
            mDesctiption = mView.findViewById(R.id.viewComplainDesc);
            mDesctiption.setText(description);
        }
        public void setDate(String date) {
            mDate = mView.findViewById(R.id.viewComplainDate);
            mDate.setText(date);
        }
    }
}
