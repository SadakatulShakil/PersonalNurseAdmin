package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.NotificationAdapter;
import com.astronist.personalnurseadmin.Adapter.ReviewAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminReviewActivity extends AppCompatActivity {
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<PrescriptionNote> reviewNoteArrayList = new ArrayList<>();
    private DatabaseReference presRef;
    private PrescriptionNote pressNoteInfo;
    private String salesId;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review);
        inItView();
        Intent intent = getIntent();
        salesId = intent.getStringExtra("id");

        getReviewList(salesId);

    }

    private void getReviewList(String salesId) {
        presRef = FirebaseDatabase.getInstance().getReference().child("SalesReview");
        presRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    if(salesId.equals(pressNoteInfo.getSalesId())){
                        reviewNoteArrayList.add(pressNoteInfo);

                    }
                }
                progressBar.setVisibility(View.GONE);
                reviewRecyclerView.setLayoutManager(new LinearLayoutManager(AdminReviewActivity.this));
                reviewAdapter = new ReviewAdapter(AdminReviewActivity.this, reviewNoteArrayList);
                reviewRecyclerView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminReviewActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inItView() {
        reviewRecyclerView = findViewById(R.id.notificationReviewRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}