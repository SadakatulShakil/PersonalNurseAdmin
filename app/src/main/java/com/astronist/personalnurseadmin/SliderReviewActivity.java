package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.SliderAdapter;
import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.astronist.personalnurseadmin.Model.Slider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SliderReviewActivity extends AppCompatActivity {

    private ImageView showImage, pickImage;
    private Uri imageUri;
    private TextView saveImage;
    private String addingTime, addingDate, uploadTime;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressBar progressBar;
    private ArrayList<Slider> sliderArrayList = new ArrayList<>();
    private SliderAdapter mSliderAdapter;
    private RecyclerView sliderRevView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_review);
        inItView();
        progressBar.setVisibility(View.VISIBLE);
        mStorageRef = FirebaseStorage.getInstance().getReference("SliderInfo");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SliderInfo");
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        sliderRevView.setLayoutManager(new GridLayoutManager(SliderReviewActivity.this, 2, RecyclerView.VERTICAL, false));
        mSliderAdapter = new SliderAdapter(sliderArrayList, SliderReviewActivity.this);
        sliderRevView.setAdapter(mSliderAdapter);
        getSetSliderImage();

    }

    private void OpenFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            String path = imageUri.getLastPathSegment();
            Log.d(TAG, "onActivityResult: " + path);
            Picasso.get().load(imageUri).into(showImage);
            //choosePhoto.setText(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                showImage.setClipToOutline(true);
            }
            //saveImageBtn.setVisibility(View.VISIBLE);
            saveImageErl(imageUri);
        }
    }

    private void saveImageErl(Uri imageUri) {
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                addingTime = myTimeFormat.format(calendar.getTime());
                SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                addingDate = myDateFormat.format(calendar.getTime());
                uploadTime = addingDate+" "+addingTime;

                if (imageUri != null) {
                    StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }, 500);
                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uri.isComplete()) ;
                                    Uri url = uri.getResult();
                                    //Toast.makeText(UploadProductActivity.this, "image Upload Successful!", Toast.LENGTH_SHORT).show();
                                    uploadSliderInfo(url.toString(), uploadTime);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SliderReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
                } else {
                    Toast.makeText(SliderReviewActivity.this, "No file Selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadSliderInfo(String imageUrl, String uploadTime) {

        String uploadId = mDatabaseRef.push().getKey();
        Slider sliderInfo = new Slider(imageUrl, uploadTime, uploadId);
        mDatabaseRef.
                child(uploadId).
                setValue(sliderInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(SliderReviewActivity.this, "Slider Upload Successful!", Toast.LENGTH_SHORT).show();
                    showImage.setImageResource(R.drawable.image_3);
                    Intent infoProduct = new Intent(SliderReviewActivity.this, SliderReviewActivity.class);
                    startActivity(infoProduct);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void getSetSliderImage() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        mDatabaseRef = fdb.getReference("SliderInfo");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot productSnap :snapshot.getChildren()){

                    Slider sliderInfo = productSnap.getValue(Slider.class);

                    sliderArrayList.add(sliderInfo);
                }
                Log.d(TAG, "onDataChange: "+ sliderArrayList.size());

                mSliderAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void inItView() {
        showImage = findViewById(R.id.showImage);
        pickImage = findViewById(R.id.pickImage);
        saveImage = findViewById(R.id.saveImage);
        sliderRevView = findViewById(R.id.sliderRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}