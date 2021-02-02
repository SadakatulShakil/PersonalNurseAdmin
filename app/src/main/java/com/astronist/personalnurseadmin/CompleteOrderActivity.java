package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurseadmin.Adapter.DailyOrderAdapter;
import com.astronist.personalnurseadmin.Adapter.MedOrderAdapter;
import com.astronist.personalnurseadmin.Model.DailyOrder;
import com.astronist.personalnurseadmin.Model.MedicineOrder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompleteOrderActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton medicineBtn, dailyDealBtn;
    private RecyclerView medicineRecyclerView, dailyDealRecyclerView;
    private MedOrderAdapter medOrderAdapter;
    private DailyOrderAdapter dailyOrderAdapter;
    private ArrayList<MedicineOrder> medicineOrderArrayList = new ArrayList<>();
    private ArrayList<DailyOrder> dailyOrderArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private DatabaseReference medOrderRef, dailyOrderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        inItView();
        progressBar.setVisibility(View.VISIBLE);
        getCompleteMedicine();
        getCompleteDaily();

        medicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dailyDealBtn.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    medicineBtn.setBackgroundTintList(getResources().getColorStateList(R.color.color_primary));
                }
                dailyDealRecyclerView.setVisibility(View.GONE);
                medicineRecyclerView.setVisibility(View.VISIBLE);
                getCompleteMedicine();
            }
        });

        dailyDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dailyDealBtn.setBackgroundTintList(getResources().getColorStateList(R.color.color_primary));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    medicineBtn.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                }
                dailyDealRecyclerView.setVisibility(View.VISIBLE);
                medicineRecyclerView.setVisibility(View.GONE);
                getCompleteDaily();
            }
        });
    }

    private void getCompleteDaily() {
        dailyOrderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");

        dailyOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyOrderArrayList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    DailyOrder dailyOrder = productSnap.getValue(DailyOrder.class);
                    if(dailyOrder.getStatus().equals("pending")){

                        dailyOrderArrayList.add(dailyOrder);
                    }
                }
                progressBar.setVisibility(View.GONE);
                dailyDealRecyclerView.setLayoutManager(new LinearLayoutManager(CompleteOrderActivity.this));
                dailyOrderAdapter = new DailyOrderAdapter(CompleteOrderActivity.this, dailyOrderArrayList);
                dailyDealRecyclerView.setAdapter(dailyOrderAdapter);
                dailyOrderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCompleteMedicine() {
        medOrderRef = FirebaseDatabase.getInstance().getReference("Order");
        medOrderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                medicineOrderArrayList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    MedicineOrder medicineOrder = productSnap.getValue(MedicineOrder.class);

                    if(medicineOrder.getStatus().equals("complete")){
                        medicineOrderArrayList.add(medicineOrder);
                    }

                }
                progressBar.setVisibility(View.GONE);
                medicineRecyclerView.setLayoutManager(new LinearLayoutManager(CompleteOrderActivity.this));
                medOrderAdapter = new MedOrderAdapter(CompleteOrderActivity.this, medicineOrderArrayList);
                medicineRecyclerView.setAdapter(medOrderAdapter);
                medOrderAdapter.notifyDataSetChanged();
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
        medicineBtn = findViewById(R.id.medicineOrder);
        dailyDealBtn = findViewById(R.id.dailyOrder);
        medicineRecyclerView = findViewById(R.id.medicineOrderRecycleView);
        dailyDealRecyclerView = findViewById(R.id.dailyOrderRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}