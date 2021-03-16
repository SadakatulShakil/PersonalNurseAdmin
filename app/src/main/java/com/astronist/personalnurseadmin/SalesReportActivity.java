package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.ReviewAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SalesReportActivity extends AppCompatActivity {
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<PrescriptionNote> reviewNoteArrayList = new ArrayList<>();
    private DatabaseReference presRef;
    private PrescriptionNote pressNoteInfo;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        inItView();
        getReviewList();
    }

    private void getReviewList() {
        presRef = FirebaseDatabase.getInstance().getReference().child("SalesReview");
        presRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                        reviewNoteArrayList.add(pressNoteInfo);

                }
                progressBar.setVisibility(View.GONE);
                reviewRecyclerView.setLayoutManager(new LinearLayoutManager(SalesReportActivity.this));
                reviewAdapter = new ReviewAdapter(SalesReportActivity.this, reviewNoteArrayList);
                reviewRecyclerView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SalesReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inItView() {
        reviewRecyclerView = findViewById(R.id.notificationReviewRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }


}