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
import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNotificationActivity extends AppCompatActivity {

    private RecyclerView notifyRecyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<PrescriptionNote> prescriptionNoteArrayList = new ArrayList<>();
    private DatabaseReference presRef;
    private PrescriptionNote pressNoteInfo;
    private String salesId;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
        notifyRecyclerView = findViewById(R.id.notificationsRecycleView);
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        salesId = intent.getStringExtra("id");
        getNotificationList();

    }

    private void getNotificationList() {

        presRef = FirebaseDatabase.getInstance().getReference().child("PresNotification");
        presRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    if(salesId.equals(pressNoteInfo.getSalesId())){
                        prescriptionNoteArrayList.add(pressNoteInfo);

                    }
                }
                progressBar.setVisibility(View.GONE);
                notifyRecyclerView.setLayoutManager(new LinearLayoutManager(AdminNotificationActivity.this));
                notificationAdapter = new NotificationAdapter(AdminNotificationActivity.this, prescriptionNoteArrayList);
                notifyRecyclerView.setAdapter(notificationAdapter);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminNotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}