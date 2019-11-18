package com.khan.polio;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khan.polio.Models.WorkerRegisterChildModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerStartActivity extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerOptions<WorkerRegisterChildModel> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    DatabaseReference mRef;
    String current_User;
    FirebaseAuth mAuth;
    String searchText;
    EditText mSearchText;
    Button btnSearch;

    public WorkerStartActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_worker_start, container, false);
        mAuth = FirebaseAuth.getInstance();
        current_User = mAuth.getCurrentUser().getUid();
        mRecyclerView = mView.findViewById(R.id.searchRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.hasFixedSize();
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSearchText = mView.findViewById(R.id.searchChild);
        btnSearch = mView.findViewById(R.id.btnSearch);

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("Register Childs").child(current_User);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists());
                        //Toast.makeText(getContext(), child, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                searchText = mSearchText.getText().toString().trim();
                Toast.makeText(getContext(), "Start Listening", Toast.LENGTH_SHORT).show();

                Query firebaseSearchQuery = mRef.orderByChild("houseNumber")
                        .startAt(searchText).endAt(searchText+"\uf8ff");

                firebaseRecyclerOptions = new FirebaseRecyclerOptions
                        .Builder<WorkerRegisterChildModel>()
                        .setQuery(firebaseSearchQuery,WorkerRegisterChildModel.class)
                        .build();

                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WorkerRegisterChildModel, SearchChild.ChildViewHolder>(firebaseRecyclerOptions) {

                    @NonNull
                    @Override
                    public SearchChild.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.worker_register_child_list,parent,false);
                        return new SearchChild.ChildViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SearchChild.ChildViewHolder holder, int position, @NonNull WorkerRegisterChildModel model) {
                        final String PostKey = getRef(position).getKey();

                        holder.setName("Name: "+model.getName());
                        holder.setFatherName("Father Name: "+model.getFatherName());
                        holder.setHouseNumber("House No: "+model.getHouseNumber());

                        mSearchText.setText(null);

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
        });

        return mView;
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
