package com.khan.polio;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khan.polio.Models.RegisterModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddWorker extends Fragment {
    private String userName,userEmail,userPassword,userCnic,userContact,currentUserId,downloadUrl;
    EditText mRegisterName, mRegisterEmail, mRegisterPass, mRegisterCnic, mRegisterContact;
    Button mRegisterButton;
    View mView;
    CircleImageView circleImageView;
    Uri imageUri;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    StorageReference mStorage;
    private ProgressDialog dialog;
    public AddWorker() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_add_worker, container, false);
        //Inflating Layout Elements
        circleImageView = mView.findViewById(R.id.registerProfileImage);
        mRegisterName = mView.findViewById(R.id.registerEdtName);
        mRegisterEmail = mView.findViewById(R.id.registerEdtEmail);
        mRegisterPass = mView.findViewById(R.id.registerEdtPassword);
        mRegisterCnic = mView.findViewById(R.id.registerEdtCnicNumber);
        mRegisterContact = mView.findViewById(R.id.registerEdtContact);
        mRegisterButton = mView.findViewById(R.id.registerButton);
        //
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Workers");
        mStorage = FirebaseStorage.getInstance().getReference();
        dialog = new ProgressDialog(getContext());
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatingRegisterationFields();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });
        return mView;
    }

    private void validatingRegisterationFields() {
        userName = mRegisterName.getText().toString().trim();
        userEmail = mRegisterEmail.getText().toString().trim();
        userPassword = mRegisterPass.getText().toString().trim();
        userCnic = mRegisterCnic.getText().toString().trim();
        userContact = mRegisterContact.getText().toString().trim();

        if (userName.isEmpty()){
            mRegisterName.setError("Required");
            if (userName.length()<6)
            {
                mRegisterName.setError("Name length too short");
                return;
            }
            return;
        }

        else if (userEmail.isEmpty()){
            mRegisterEmail.setError("Required");
            if (userEmail.length()<6)
            {
                mRegisterEmail.setError("Email length too short");
                return;
            }
            return;
        }

        else if (userPassword.isEmpty()){
            mRegisterPass.setError("Required");
            if (userPassword.length()<6)
            {
                mRegisterPass.setError("Password length too short");
                return;
            }
            return;
        }
        else if (userCnic.isEmpty()){
            mRegisterCnic.setError("Required");
            if (userPassword.length()<6)
            {
                mRegisterCnic.setError("Cnic length too short");
                return;
            }
            return;
        }

        else if (userContact.isEmpty()){
            mRegisterContact.setError("Required");
            if (userPassword.length()<6)
            {
                mRegisterContact.setError("Password length too short");
                return;
            }
            return;
        }

        else if (imageUri == null){
            Toast.makeText(getContext(), "Please choose user image", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Data saving in progress");
            dialog.show();
            saveDataToFirebaseDatabase();
        }
    }

    private void saveDataToFirebaseDatabase() {
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Worker Authentication Successful", Toast.LENGTH_SHORT).show();
                    currentUserId = mAuth.getCurrentUser().getUid();
                    saveImageToFirebaseStorage();
                }
                else
                {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void saveUserInfoToFirebaseDatabase() {
        RegisterModel workerInfo = new RegisterModel();
        workerInfo.setName(userName);
        workerInfo.setEmail(userEmail);
        workerInfo.setCnic(userCnic);
        workerInfo.setContact(userContact);
        workerInfo.setImageUrl(downloadUrl);
        workerInfo.setUserId(currentUserId);
        mRef.child(currentUserId).setValue(workerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getContext(), "Worker data stored successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void saveImageToFirebaseStorage() {
        final StorageReference reference = mStorage.child("Worker Images").child(currentUserId+".jpg");
        Task uploadTask = reference.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    //dialog.dismiss();
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    //Toast.makeText(AddPost.this, downloadUrl, Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    saveUserInfoToFirebaseDatabase();
                    clearAllFields();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void clearAllFields() {
        circleImageView.setImageResource(R.drawable.profile_image);
        imageUri=null;
        mRegisterName.setText(null);
        mRegisterEmail.setText(null);
        mRegisterCnic.setText(null);
        mRegisterPass.setText(null);
        mRegisterContact.setText(null);
        mAuth.signOut();
        dialog.dismiss();
    }

    private void cropImage() {
        CropImage.activity()
                .setAspectRatio(1,1)
                .start(getContext(), this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                circleImageView.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
