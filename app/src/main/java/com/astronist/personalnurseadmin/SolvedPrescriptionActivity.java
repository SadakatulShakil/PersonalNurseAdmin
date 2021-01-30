package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SolvedPrescriptionActivity extends AppCompatActivity {

    private RecyclerView allopathicRecyclerView;
    private PrescriptionAdapter mPrescribeAdapter;
    private ArrayList<PrescriptionInfo> mAlloInfoList = new ArrayList<>();
    private DatabaseReference prescribeRef;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId, status;
    public static final String TAG = "solve";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved_prescription);
        inItView();

        firebaseAuth = FirebaseAuth.getInstance();
        //userId = firebaseAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        getAllopathicList();
    }

    private void getAllopathicList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo");

        prescribeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                    Log.d(TAG, "onData: " + prescriptionInfo.toString());
                    status = prescriptionInfo.getStatus();
                    if(prescriptionInfo.getStatus().equals("Solved")){
                        mAlloInfoList.add(prescriptionInfo);
                    }

                }
                progressBar.setVisibility(View.GONE);
                allopathicRecyclerView.setLayoutManager(new LinearLayoutManager(SolvedPrescriptionActivity.this));
                mPrescribeAdapter = new PrescriptionAdapter(mAlloInfoList, SolvedPrescriptionActivity.this, status);
                allopathicRecyclerView.setAdapter(mPrescribeAdapter);
                mPrescribeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        allopathicRecyclerView = findViewById(R.id.allopathicRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}