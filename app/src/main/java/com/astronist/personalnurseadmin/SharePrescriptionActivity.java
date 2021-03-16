package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Adapter.SalesSelectAdapter;
import com.astronist.personalnurseadmin.Model.Admin;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SharePrescriptionActivity extends AppCompatActivity {
    private RecyclerView salesListRecyclerView;
    private SalesSelectAdapter mSalesAdapter;
    private ArrayList<Admin> mAdminInfoList = new ArrayList<>();
    private DatabaseReference adminRef;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId, status;
    private PrescriptionInfo prescriptionInfo;
    public static final String TAG = "share";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_prescription);
        inItView();

        firebaseAuth = FirebaseAuth.getInstance();
        //userId = firebaseAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        prescriptionInfo = (PrescriptionInfo) intent.getSerializableExtra("prescription");
        progressBar.setVisibility(View.VISIBLE);

        getAllSalesList();
        salesListRecyclerView.setLayoutManager(new LinearLayoutManager(SharePrescriptionActivity.this));
        mSalesAdapter = new SalesSelectAdapter(SharePrescriptionActivity.this, mAdminInfoList, prescriptionInfo.getPrescriptionUrl());
        salesListRecyclerView.setAdapter(mSalesAdapter);

    }

    private void getAllSalesList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        adminRef = fdb.getReference("AdminInfo");

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    Admin adminInfo = productSnap.getValue(Admin.class);
                    Log.d(TAG, "onData: " + adminInfo.toString());
                    mAdminInfoList.add(adminInfo);
                }
                Log.d(TAG, "onDataChange: "+mAdminInfoList.size());
                progressBar.setVisibility(View.GONE);
                mSalesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SharePrescriptionActivity.this, "Something Went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inItView() {
        salesListRecyclerView = findViewById(R.id.salesListRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}