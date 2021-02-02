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

import com.astronist.personalnurseadmin.Adapter.MedOrderAdapter;
import com.astronist.personalnurseadmin.Model.MedicineOrder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewMedicineOrderActivity extends AppCompatActivity {
    private RecyclerView mNewOrderRecyclerView;
    private ProgressBar progressBar;
    private MedOrderAdapter mMedOrderAdapter;
    private ArrayList<MedicineOrder> mMedOrderList = new ArrayList<>();
    private DatabaseReference medOrderRef;
    private String userId, status;
    public static final String TAG = "medOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_order);

        inItView();
        progressBar.setVisibility(View.VISIBLE);
        medOrderRef = FirebaseDatabase.getInstance().getReference("Order");
        getNewMedicineOrder();
    }

    private void getNewMedicineOrder() {
    medOrderRef.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            mMedOrderList.clear();
            for (DataSnapshot productSnap : snapshot.getChildren()) {

                MedicineOrder medicineOrder = productSnap.getValue(MedicineOrder.class);

                Log.d(TAG, "onData: " + medicineOrder.toString());
                status = medicineOrder.getStatus();
                if(medicineOrder.getStatus().equals("pending")){
                    mMedOrderList.add(medicineOrder);
                }

            }
            progressBar.setVisibility(View.GONE);
            mNewOrderRecyclerView.setLayoutManager(new LinearLayoutManager(NewMedicineOrderActivity.this));
            mMedOrderAdapter = new MedOrderAdapter(NewMedicineOrderActivity.this, mMedOrderList);
            mNewOrderRecyclerView.setAdapter(mMedOrderAdapter);
            mMedOrderAdapter.notifyDataSetChanged();
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
        mNewOrderRecyclerView = findViewById(R.id.newOrderRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}