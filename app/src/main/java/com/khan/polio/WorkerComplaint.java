package com.khan.polio;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khan.polio.Models.ComplainModel;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerComplaint extends Fragment {

    EditText mTitle, mAddress, mDescription;
    Button btnPostComplain;
    DatabaseReference mRef;
    private String title,address,desc;
    Date currentDate;
    View mView;
    ProgressDialog dialog;
    public WorkerComplaint() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_worker_complaint, container, false);
        mRef = FirebaseDatabase.getInstance().getReference().child("Complains");
        mTitle = mView.findViewById(R.id.complainTitle);
        mAddress = mView.findViewById(R.id.complainAddress);
        mDescription = mView.findViewById(R.id.complainDesc);
        btnPostComplain = mView.findViewById(R.id.postComplain);
        dialog = new ProgressDialog(getContext());
        btnPostComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatingFields();
            }
        });

        return  mView;
    }

    private void validatingFields() {
        title = mTitle.getText().toString().trim();
        address = mAddress.getText().toString().trim();
        desc = mDescription.getText().toString().trim();

        if (title.isEmpty()){
            mTitle.setError("Required");
            return;
        }
        else if (address.isEmpty()){
            mAddress.setError("Required");
            return;
        }
        else if (desc.isEmpty()){
            mDescription.setError("Required");
            return;
        }
        else {
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Posting complain in progress");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            saveDataToFirebaseDatabase();
        }
    }

    private void saveDataToFirebaseDatabase() {
        currentDate = new Date();
        String date = currentDate.toLocaleString();
        ComplainModel complainModel = new ComplainModel();

        complainModel.setTitle(title);
        complainModel.setAddress(address);
        complainModel.setDescription(desc);
        complainModel.setDate(date);

        mRef.child(date).setValue(complainModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Complaint Register Successfully", Toast.LENGTH_SHORT).show();
                    clearEditFields();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void clearEditFields() {
        mTitle.setText(null);
        mDescription.setText(null);
        mAddress.setText(null);
        dialog.dismiss();
    }

}
