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
import com.astronist.personalnurseadmin.Adapter.ProductAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UnsolvedPrescriptionActivity extends AppCompatActivity {

    private RecyclerView allopathicRecyclerView;
    private PrescriptionAdapter mPrescribeAdapter;
    private ArrayList<PrescriptionInfo> mAlloInfoList = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();
    private DatabaseReference prescribeRef;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId, status;

    public static final String TAG = "Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsolved_prescription);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        //userId = firebaseAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        getAllopathicList();
    }


    private void getAllopathicList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo");

        prescribeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    userList.add(productSnap.getKey());
                }
                Log.d(TAG, "onUserData: "+ userList);
                for(int i= 0; i<userList.size(); i++){
                    userId = userList.get(i);
                    Log.d(TAG, "getUserId: "+ userId);

                    prescribeRef.child(userId).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot productSnap : snapshot.getChildren()) {

                                PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                                Log.d(TAG, "onData: " + prescriptionInfo.toString());
                                status = prescriptionInfo.getStatus();
                                if(prescriptionInfo.getStatus().equals("unsolved")){

                                    mAlloInfoList.add(prescriptionInfo);
                                }

                            }
                            progressBar.setVisibility(View.GONE);
                            allopathicRecyclerView.setLayoutManager(new LinearLayoutManager(UnsolvedPrescriptionActivity.this));
                            mPrescribeAdapter = new PrescriptionAdapter(mAlloInfoList, UnsolvedPrescriptionActivity.this, status);
                            allopathicRecyclerView.setAdapter(mPrescribeAdapter);
                            mPrescribeAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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