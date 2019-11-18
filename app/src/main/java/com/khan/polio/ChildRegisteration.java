package com.khan.polio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.khan.polio.Models.ChildRegistrationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ChildRegisteration extends AppCompatActivity {

    private EditText mName,mFatherName,mAge,mHouseNumber,mHomeAddress;
    String name,fatherName,age,houseNumber,homeAddress,status,currentUser;
    private DatabaseReference mRef,allChildRef;
    private Button btnRegisterChild;
    private RadioButton mBtnDone,mBtnLeft;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_registeration);

        mRef = FirebaseDatabase.getInstance().getReference().child("Register Childs");
        allChildRef = FirebaseDatabase.getInstance().getReference().child("All Childs");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        linearLayout = findViewById(R.id.container);
        mName = findViewById(R.id.registerChildName);
        mFatherName = findViewById(R.id.registerChildFName);
        mAge = findViewById(R.id.registerChildAge);
        mHouseNumber = findViewById(R.id.registerChildHouseNo);
        mHomeAddress = findViewById(R.id.registerChildHomeAddress);
        mBtnDone = findViewById(R.id.btnDone);
        mBtnLeft = findViewById(R.id.btnLeft);
        dialog = new ProgressDialog(this);
        btnRegisterChild = findViewById(R.id.registerChildBtn);
        btnRegisterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatingFields();
            }
        });
    }

    private void validatingFields() {

        name = mName.getText().toString().trim();
        fatherName = mFatherName.getText().toString().trim();
        age = mAge.getText().toString().trim();
        houseNumber = mHouseNumber.getText().toString().trim();
        homeAddress = mHomeAddress.getText().toString().trim();

        radioButtonStatus();
        if (name.isEmpty()){
            mName.setError("Required");
            return;
        }
        else if (fatherName.isEmpty()){
            mFatherName.setError("Required");
            return;
        }
        else if (age.isEmpty()){
            mAge.setError("Required");
            return;
        }
        else if (houseNumber.isEmpty()){
            mHouseNumber.setError("Required");
            return;
        }
        else if (homeAddress.isEmpty()){
            mHomeAddress.setError("Required");
            return;
        }
        else if (status == null){
            Toast.makeText(getApplicationContext(), "Please select one \'Done\' or \'Left\'", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Data storing in progress");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            saveDataToFirebaseDatabase(name,fatherName,age,houseNumber,homeAddress,status);
        }
    }

    private void saveDataToFirebaseDatabase(String name, String fatherName, String age, String houseNumber, String homeAddress, String status) {

        Date date = new Date();
        String currentDate = date.toLocaleString();
        ChildRegistrationModel childRegistrationModel = new ChildRegistrationModel();
        childRegistrationModel.setName(name);
        childRegistrationModel.setFatherName(fatherName);
        childRegistrationModel.setAge(age);
        childRegistrationModel.setHouseNumber(houseNumber);
        childRegistrationModel.setHomeAddress(homeAddress);
        childRegistrationModel.setStatus(status);
        childRegistrationModel.setDate(currentDate);

        mRef.child(currentUser).child(currentDate).setValue(childRegistrationModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(ChildRegisteration.this, "Child Registerd Successfully", Toast.LENGTH_SHORT).show();
                    clearAllFields();
                    goToWorkerRegisterChild();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(ChildRegisteration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        allChildRef.child(currentDate).setValue(childRegistrationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void goToWorkerRegisterChild() {
        dialog.dismiss();
        WorkerRegisterChild fragment = new WorkerRegisterChild();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
        finish();
    }

    private void clearAllFields() {
        mName.setText(null);
        mFatherName.setText(null);
        mAge.setText(null);
        mHouseNumber.setText(null);
        mHomeAddress.setText(null);
        mBtnDone.setChecked(false);
        mBtnLeft.setChecked(false);
        dialog.dismiss();
    }

    private void radioButtonStatus() {
        if (mBtnDone.isChecked()){
            status = "Done";
        }
        else {
            status = "Left";
        }
    }
}
