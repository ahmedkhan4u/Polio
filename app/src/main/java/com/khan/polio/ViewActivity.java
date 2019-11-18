package com.khan.polio;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khan.polio.Models.ViewActivityModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewActivity extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    CardView cardView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    String currentUser;
    ImageButton mLogout;
    public ViewActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_view, container, false);
        mRecyclerView = mView.findViewById(R.id.viewActivityRecyclerView);
        mRecyclerView.hasFixedSize();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mLogout = mView.findViewById(R.id.admin_Logout);
        mRef  = FirebaseDatabase.getInstance().getReference().child("Admins");

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Workers");

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLogout();
            }
        });

        return mView;
    }

    private void adminLogout() {
        SharedPreferences sharedPreferences = this.getActivity()
                .getSharedPreferences("Session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Status","false");
        editor.commit();
        startActivity(new Intent(getContext(),MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ViewActivityModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<ViewActivityModel>()
                        .setQuery(mRef,ViewActivityModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ViewActivityModel, PostsViewHolder>(firebaseRecyclerOptions) {
            @Override
            public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_activity_list, parent, false);

                return new PostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostsViewHolder holder, int position, ViewActivityModel model) {
                final String PostKey = getRef(position).getKey();
                holder.setImageUrl(getContext(),model.getImageUrl());
                holder.setName(model.getName());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),ViewActivityDetails.class);
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

    public static class PostsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CircleImageView profileImage;
        TextView profileText;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setImageUrl(Context ctx,String imageUrl) {
            profileImage = mView.findViewById(R.id.viewActivityProfileImage);
            Picasso.with(ctx).load(imageUrl).into(profileImage);
        }
        public void setName(String name) {
            profileText = mView.findViewById(R.id.viewActivityProfileName);
            profileText.setText(name);
        }
    }
}
