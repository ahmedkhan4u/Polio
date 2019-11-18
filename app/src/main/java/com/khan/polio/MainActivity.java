package com.khan.polio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLogin;
    TextView mAdminLogin, mWorkerLogin;
    DatabaseReference mDatabase;
    String parentDbName = "Workers";
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String phone,userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPassword);
        mAdminLogin = findViewById(R.id.adminLogin);
        mWorkerLogin = findViewById(R.id.workerLogin);
        mLogin = findViewById(R.id.btnLogin);
        dialog = new ProgressDialog(this);
        //FirebaseApp.initializeApp(MainActivity.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        mAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin.setText("Admin Login");
                mWorkerLogin.setVisibility(View.VISIBLE);
                mAdminLogin.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
                mEmail.setHint("Enter Phone Number");
            }
        });

        mWorkerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin.setText("Login");
                mWorkerLogin.setVisibility(View.INVISIBLE);
                mAdminLogin.setVisibility(View.VISIBLE);
                parentDbName = "Workers";
                mEmail.setHint("Enter Email");
            }
        });
    }

    private void LoginUser() {

        phone = mEmail.getText().toString().trim();
        userPassword = mPassword.getText().toString().trim();

        if (phone.isEmpty()){
            mEmail.setError("Required");
            return;
        }
        else if (userPassword.isEmpty()){
            mPassword.setText("Required");
            return;
        }

        else if (parentDbName.equals("Admins")){

            showDialogMessage();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(parentDbName).child(phone).exists()){
                        Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                        if (usersData.getPhone().equals(phone))
                        {
                            if (usersData.getPassword().equals(userPassword))
                            {       dialog.dismiss();
                                    goToAdminActivity();

                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "No user exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (parentDbName.equals("Workers")){
            showDialogMessage();
            mAuth.signInWithEmailAndPassword(phone,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        goToWorkerActivity();
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showDialogMessage() {
        dialog.setTitle("Please Wait...");
        dialog.setCancelable(false);
        dialog.setMessage("Authentication in progress");
        dialog.show();
    }

    private void goToWorkerActivity() {
        Intent workerIntent = new Intent(getApplicationContext(),WorkerActivity.class);
        startActivity(workerIntent);
        finish();
    }

    private void goToAdminActivity() {

        sharedPreferences = getSharedPreferences("Session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Status","true");
        editor.commit();

        Intent adminIntent = new Intent(MainActivity.this,AdminActivity.class);
        startActivity(adminIntent);
        finish();
        Toast.makeText(this, "Admin Loged in Successful", Toast.LENGTH_SHORT).show();
        mEmail.setText(null);
        mPassword.setText(null);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserExistence();
        checkAdminStatus();
    }

    private void checkAdminStatus() {
        sharedPreferences = getSharedPreferences("Session",Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("Status","false");
        if (status.equals("true")){
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            finish();
        }

    }

    private void checkUserExistence() {
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser != null){
            goToWorkerActivityy();
        }
    }

    private void goToWorkerActivityy() {
        startActivity(new Intent(getApplicationContext(),WorkerActivity.class));
        finish();
    }
}
