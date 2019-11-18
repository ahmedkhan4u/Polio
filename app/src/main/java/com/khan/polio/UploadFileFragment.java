package com.khan.polio;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khan.polio.Models.PdfFileModel;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFileFragment extends Fragment {


    public UploadFileFragment() {
        // Required empty public constructor
    }

    View mView;
    private TextView mFileText;
    private Button mUploadFile,mChooseFile;
    StorageReference mStorage;
    DatabaseReference mRef;
    String downloadUrl;
    ProgressDialog dialog;
    EditText mFileName;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_upload_file, container, false);
        mUploadFile = mView.findViewById(R.id.btn_uploadFile);
        mFileText = mView.findViewById(R.id.txt_uploadFile);
        mChooseFile = mView.findViewById(R.id.btnChooseFile);
        dialog = new ProgressDialog(getContext());
        mRef = FirebaseDatabase.getInstance().getReference().child("Files");
        mFileName = mView.findViewById(R.id.edt_FileName);
        mStorage = FirebaseStorage.getInstance().getReference().child("PdfFiles");
        mUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Please Wait...");
                dialog.setMessage("File Uploading In Progress");
                dialog.setCancelable(false);
                dialog.show();
                uploadFileToFirebase(uri);

            }
        });

        mChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        return mView;
    }

    private void uploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null){
            uri = data.getData();
        }
    }

    private void uploadFileToFirebase(Uri uri) {
        final String file_name = mFileName.getText().toString().trim();

        if (file_name.isEmpty()){
            mFileName.setError("Required");
            dialog.dismiss();
            return;
        }
       if (uri==null){
           dialog.dismiss();
           Snackbar.make(mView.findViewById(R.id.uploadLayout),
                   "Please choose a file",Snackbar.LENGTH_SHORT).show();
           return;
       }
       else {
           Date d = new Date();
           String date = d.toLocaleString();
           final StorageReference storageReference =  mStorage.child(file_name+".pdf");
           Toast.makeText(getContext(), storageReference.toString(), Toast.LENGTH_SHORT).show();
           Task uplodTask = storageReference.putFile(uri);
           Task<Uri> urlTask = uplodTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
               @Override
               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if (!task.isSuccessful()) {
                       throw task.getException();
                   }
                   dialog.dismiss();
                   return storageReference.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if (task.isSuccessful()) {
                       downloadUrl = task.getResult().toString();
                       Snackbar.make(mView.findViewById(R.id.uploadLayout),
                               "File Uploaded Successfully",Snackbar.LENGTH_SHORT).show();
                       saveDataOnFirebaseDatabase(file_name);
                   } else {
                       dialog.dismiss();
                       Snackbar.make(mView.findViewById(R.id.uploadLayout),
                               task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                   }
               }
           });
       }
    }

    private void saveDataOnFirebaseDatabase(String file_name) {
        PdfFileModel pdfFileModel = new PdfFileModel(file_name,downloadUrl);
        mRef.push().setValue(pdfFileModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Snackbar.make(mView.findViewById(R.id.uploadLayout),
                            "File Saved In Database Successfully",Snackbar.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    Snackbar.make(mView.findViewById(R.id.uploadLayout),
                            task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
