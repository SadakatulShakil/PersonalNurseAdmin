package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.astronist.personalnurseadmin.Model.PriceOffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailsPrescription extends AppCompatActivity {

    private ImageView previewPrescription, mainPrescription, removeBtn;
    private LinearLayout mainLayout, mainImageLayout;
    private EditText medicineList, oneDayPrice;
    private ExtendedFloatingActionButton submitBtn;
    private PrescriptionInfo prescriptionInfo;
    private String addingTime, addingDate;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mUpdateRef;
    private String pushId,userId;
    public static final String TAG = "detailsPrescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_prescription);
        inItView();

        Intent intent = getIntent();
        prescriptionInfo = (PrescriptionInfo) intent.getSerializableExtra("prescription_info");
        pushId = prescriptionInfo.getPushId();
        userId = prescriptionInfo.getUserId();


        Picasso.get().load(prescriptionInfo.getPrescriptionUrl()).into(previewPrescription);
        Picasso.get().load(prescriptionInfo.getPrescriptionUrl()).into(mainPrescription);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OfferedPrice");
        mUpdateRef = FirebaseDatabase.getInstance().getReference("PrescriptionInfo");

        previewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
                mainImageLayout.setVisibility(View.VISIBLE);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                mainImageLayout.setVisibility(View.GONE);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storePriceList();
                removeFromUnsolved();
            }
        });
    }

    private void removeFromUnsolved() {
        HashMap hashMap = new HashMap();
        hashMap.put("status", "Solved");
       mUpdateRef.child(userId).child(pushId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(DetailsPrescription.this, "Data going to solved section !", Toast.LENGTH_SHORT).show();
            }
           }
       });

    }

    private void storePriceList() {

        String uMedicine = medicineList.getText().toString().trim();
        String uOneDayPrice = oneDayPrice.getText().toString().trim();
        int oneWeak = (Integer.parseInt(uOneDayPrice)) * 7;
        int fifteen = (Integer.parseInt(uOneDayPrice)) * 15;
        int oneMonth = (Integer.parseInt(uOneDayPrice)) * 30;
        String uOneWeakPrice = String.valueOf(oneWeak);
        String uFifteenDaysPrice = String.valueOf(fifteen);
        String uOneMonthPrice = String.valueOf(oneMonth);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        addingTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        addingDate = myDateFormat.format(calendar.getTime());

        finalStep(prescriptionInfo.getUserId(), uMedicine, uOneDayPrice, uOneWeakPrice, uFifteenDaysPrice, uOneMonthPrice, addingTime, addingDate);

    }

    private void finalStep(final String userId, final String uMedicine, final String uOneDayPrice, final String uOneWeakPrice,
                           final String uFifteenDaysPrice, final String uOneMonthPrice, final String addingTime, final String addingDate) {

        PriceOffer priceOffer = new PriceOffer(userId, pushId, uMedicine, uOneDayPrice, uOneWeakPrice, uFifteenDaysPrice, uOneMonthPrice, addingDate, addingTime);
        mDatabaseRef.child(pushId).setValue(priceOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(DetailsPrescription.this, "Info Upload Successful!", Toast.LENGTH_SHORT).show();
                    Intent infoProduct = new Intent(DetailsPrescription.this, MainActivity.class);
                    startActivity(infoProduct);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }

    private void inItView() {

        previewPrescription = findViewById(R.id.previewImage);
        mainPrescription = findViewById(R.id.fullImage);
        mainLayout = findViewById(R.id.mainLay);
        mainImageLayout = findViewById(R.id.mainImageLay);
        medicineList = findViewById(R.id.medicineList);
        submitBtn = findViewById(R.id.submitBtn);
        removeBtn = findViewById(R.id.remove);
        oneDayPrice = findViewById(R.id.oneDayTotal);
    }
}