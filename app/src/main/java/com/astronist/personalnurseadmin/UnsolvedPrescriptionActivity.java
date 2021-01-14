package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Adapter.ProductAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UnsolvedPrescriptionActivity extends AppCompatActivity {

    private RecyclerView allopathicRecyclerView, homeopathicRecyclerView;
    private PrescriptionAdapter mPrescribeAdapter;
    private ArrayList<PrescriptionInfo> mHomeoInfoList = new ArrayList<>();
    private ArrayList<PrescriptionInfo> mAlloInfoList = new ArrayList<>();
    private DatabaseReference prescribeRef;
    private ExtendedFloatingActionButton allopathicBtn, homeopathicBtn;
    private ProgressBar progressBar;

    public static final String TAG = "Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsolved_prescription);
        inItView();

        progressBar.setVisibility(View.VISIBLE);
        allopathicRecyclerView.setLayoutManager(new LinearLayoutManager(UnsolvedPrescriptionActivity.this));
        mPrescribeAdapter = new PrescriptionAdapter(mAlloInfoList, UnsolvedPrescriptionActivity.this);
        allopathicRecyclerView.setAdapter(mPrescribeAdapter);
        getAllopathicList();

        getDifferentTypePrescribe();
    }

    private void getDifferentTypePrescribe() {
        allopathicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeopathicRecyclerView.setVisibility(View.GONE);
                allopathicRecyclerView.setVisibility(View.VISIBLE);
                getAllopathicList();

            }
        });

        homeopathicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeopathicRecyclerView.setVisibility(View.VISIBLE);
                allopathicRecyclerView.setVisibility(View.GONE);
                getHomeopathicList();

            }
        });

    }

    private void getHomeopathicList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo").child("homeo");

        prescribeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mHomeoInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                        mHomeoInfoList.add(prescriptionInfo);
                        progressBar.setVisibility(View.GONE);


                }
                Log.d(TAG, "onDataChange: " + mHomeoInfoList.size());
                homeopathicRecyclerView.setLayoutManager(new LinearLayoutManager(UnsolvedPrescriptionActivity.this));
                mPrescribeAdapter = new PrescriptionAdapter(mHomeoInfoList, UnsolvedPrescriptionActivity.this);
                homeopathicRecyclerView.setAdapter(mPrescribeAdapter);
                mPrescribeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllopathicList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo").child("allo");

        prescribeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAlloInfoList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                    mAlloInfoList.add(prescriptionInfo);
                    progressBar.setVisibility(View.GONE);


                }
                Log.d(TAG, "onDataChange: " + mAlloInfoList.size());

                mPrescribeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {

        allopathicRecyclerView = findViewById(R.id.allopathicRecycleView);
        homeopathicRecyclerView = findViewById(R.id.homeopathicRecycleView);
        allopathicBtn = findViewById(R.id.allopathicBtn);
        homeopathicBtn = findViewById(R.id.homeopathicBtn);
        progressBar = findViewById(R.id.progressBar);

    }
}