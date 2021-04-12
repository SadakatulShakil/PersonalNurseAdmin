package com.astronist.personalnurseadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.Slider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SliderUpdateActivity extends AppCompatActivity {

    private ImageView showImage;
    private TextView uploadTime, removeImage;
    private Slider slider;
    private String position;
    private DatabaseReference mDatabaseRef;
    public static final String TAG = "upload";
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_update);
        inItView();
        mStorageRef = FirebaseStorage.getInstance().getReference("SliderInfo");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SliderInfo");
        Intent intent = getIntent();
        slider = (Slider) intent.getSerializableExtra("sliderInfo");
        position = intent.getStringExtra("position");
        Picasso.get().load(slider.getSliderImageUrl()).into(showImage);
        uploadTime.setText(slider.getUploadTime());
        String upKey = slider.getUploadKey();
        Log.d(TAG, "onCreate: " + upKey);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(slider.getSliderImageUrl());
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SliderUpdateActivity.this, "Delete from Storage !", Toast.LENGTH_SHORT).show();
                    }
                });
                mDatabaseRef.child(slider.getUploadKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SliderUpdateActivity.this, "Delete from Database !", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                Intent intent1 = new Intent(SliderUpdateActivity.this, SliderReviewActivity.class);
                startActivity(intent1);
                //Toast.makeText(SliderUpdateActivity.this, "Removed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inItView() {
        showImage = findViewById(R.id.showImage);
        uploadTime = findViewById(R.id.uploadTime);
        removeImage = findViewById(R.id.removeImage);
    }
}