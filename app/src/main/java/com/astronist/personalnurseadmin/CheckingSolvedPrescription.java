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
import android.widget.TextView;

import com.astronist.personalnurseadmin.Model.Address;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.astronist.personalnurseadmin.Model.PriceOffer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CheckingSolvedPrescription extends AppCompatActivity {
    private ImageView previewPrescription, mainPrescription, removeBtn;
    private LinearLayout mainLayout, mainImageLayout;
    private TextView medicineList, userName, userPhone, userAddress1, userAddress2, userRoadNo, noAddressView;
    private PrescriptionInfo prescriptionInfo;
    private DatabaseReference mMedicineRef;
    private DatabaseReference mAddressRef;
    private String pushId,userId;
    public static final String TAG = "SolvedPres";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_solved_prescription);
        inItView();
        Intent intent = getIntent();
        prescriptionInfo = (PrescriptionInfo) intent.getSerializableExtra("prescription_info");
        pushId = prescriptionInfo.getPushId();
        userId = prescriptionInfo.getUserId();

        Picasso.get().load(prescriptionInfo.getPrescriptionUrl()).into(previewPrescription);
        Picasso.get().load(prescriptionInfo.getPrescriptionUrl()).into(mainPrescription);

        mMedicineRef = FirebaseDatabase.getInstance().getReference("OfferedPrice");
        mAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");

        previewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                mainImageLayout.setVisibility(View.VISIBLE);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                mainImageLayout.setVisibility(View.GONE);

            }
        });

        getMedicineList(pushId);
        getAddress(userId);

    }

    private void getMedicineList(String pushId) {
        mMedicineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    PriceOffer priceOffer = productSnap.getValue(PriceOffer.class);
                    if(pushId.equals(priceOffer.getPushId())){
                        medicineList.setText(priceOffer.getMedicineList());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAddress(String userId) {
        mAddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    Address address = productSnap.getValue(Address.class);
                    if(address != null) {
                        noAddressView.setVisibility(View.GONE);
                        if (userId.equals(address.getUserId())) {
                            userName.setText(address.getName());
                            userPhone.setText(address.getPhone());
                            userAddress1.setText(address.getAddressLine1());
                            userAddress2.setText(address.getGetAddressLine2());
                            userRoadNo.setText(address.getRoadNo()+" no. road");
                        }
                    }else{
                        noAddressView.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        previewPrescription = findViewById(R.id.previewImage);
        mainPrescription = findViewById(R.id.fullImage);
        mainLayout = findViewById(R.id.mainLay);
        mainImageLayout = findViewById(R.id.mainImageLay);
        medicineList = findViewById(R.id.medicineList);
        removeBtn = findViewById(R.id.remove);

        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userAddress1 = findViewById(R.id.addressLine1);
        userAddress2= findViewById(R.id.addressLine2);
        userRoadNo= findViewById(R.id.userRoadNo);
        noAddressView = findViewById(R.id.noAddressView);
    }
}