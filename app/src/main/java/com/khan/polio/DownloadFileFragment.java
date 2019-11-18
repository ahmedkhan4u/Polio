package com.khan.polio;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khan.polio.Models.PdfFileModel;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFileFragment extends Fragment {

    public DownloadFileFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Button btnDownloadFile;
    private DatabaseReference mRef;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_download_file, container, false);
        recyclerView = mView.findViewById(R.id.recyclerViewPdf);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mRef = FirebaseDatabase.getInstance().getReference().child("Files");

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<PdfFileModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<PdfFileModel>()
                .setQuery(mRef,PdfFileModel.class)
                .build();

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PdfFileModel,PdfViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View myView = LayoutInflater.from(getContext())
                        .inflate(R.layout.pdf_file_list,viewGroup,false);
                return new PdfViewHolder(myView);
            }

            @Override
            protected void onBindViewHolder(@NonNull PdfViewHolder holder, final int position, @NonNull PdfFileModel model) {
                holder.setFileName(model.getFileName());
                holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String PostKey = getRef(position).getKey();
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String mFileName = dataSnapshot.child(PostKey).child("fileName").getValue().toString();
                                String mFileUrl = dataSnapshot.child(PostKey).child("fileUrl").getValue().toString();
                                final String myFileName = mFileName+".pdf";
                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("PdfFiles");
                                mStorage.child(myFileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        downloadFile(getContext(),myFileName,DIRECTORY_DOWNLOADS,url);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        // downloadFile(getContext(),"Muhammad_Ahmed_CV.pdf",DIRECTORY_DOWNLOADS,url);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public void downloadFile(Context context,String fileName,String destinationDirectory,String uri){
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    Uri uri1 = Uri.parse(uri);
    DownloadManager.Request request = new DownloadManager.Request(uri1);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName);
    downloadManager.enqueue(request);
}

public class PdfViewHolder extends RecyclerView.ViewHolder{
    View mViw;
    ImageButton btnDownload;
    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        btnDownload = mView.findViewById(R.id.btnDownloadFile);
    }

    public void setFileName(String fileName) {
        TextView mFileName = mView.findViewById(R.id.pdf_fileName);
        mFileName.setText(fileName);
    }
    public void setFileUrl(String fileUrl) {

    }
}
}
