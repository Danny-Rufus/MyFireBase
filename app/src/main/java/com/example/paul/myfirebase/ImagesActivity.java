package com.example.paul.myfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    ImageAdapter mAdapter;

    private DatabaseReference mDataBase;
    private FirebaseStorage mStorage;
    private ValueEventListener mDbListener;

    private List<Upload> mUpload;

    private ProgressBar mCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCircle = findViewById(R.id.progress_circle);

        mUpload =  new ArrayList<>();

        mAdapter = new ImageAdapter(ImagesActivity.this,mUpload);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        mStorage =FirebaseStorage.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference("uploads");

        mDbListener = mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUpload.clear();

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUpload.add(upload);
                }
                mAdapter.notifyDataSetChanged();


                mCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mCircle.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(this, "normal click" +position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDeleteClick(int Position) {
        Upload selectedItem = mUpload.get(Position);
        final String SelectedKey = selectedItem.getmKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getmImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDataBase.child(SelectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBase.removeEventListener(mDbListener);
    }
}
