package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.astronist.personalnurseadmin.Adapter.DailyOrderAdapter;
import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Model.DailyOrder;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DailyOrderActivity extends AppCompatActivity {

    private RecyclerView dailyOrderRecyclerView;
    private DailyOrderAdapter dailyOrderAdapter;
    private ArrayList<DailyOrder> dailyOrderArrayList = new ArrayList<>();
    private DatabaseReference dailyOrderRef;
    private String userId;
    private ProgressBar progressBar;
    public static final String TAG = "daily";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order);
        inItView();
        progressBar.setVisibility(View.VISIBLE);
        getDailyOrder();
    }

    private void getDailyOrder() {
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
                dailyOrderRecyclerView.setLayoutManager(new LinearLayoutManager(DailyOrderActivity.this));
                dailyOrderAdapter = new DailyOrderAdapter(DailyOrderActivity.this, dailyOrderArrayList);
                dailyOrderRecyclerView.setAdapter(dailyOrderAdapter);
                dailyOrderAdapter.notifyDataSetChanged();
                Log.d(TAG, "onData: " + dailyOrderArrayList.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        dailyOrderRecyclerView = findViewById(R.id.dailyOrderRecycleView);
        progressBar = findViewById(R.id.progressBar);
    }
}