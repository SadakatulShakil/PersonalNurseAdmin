package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.DailyOrder;
import com.astronist.personalnurseadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DailyOrderDetailsActivity extends AppCompatActivity {

    private DailyOrder dailyOrder;
    private TextView categoryTv, productTitleTv, quantityTv, priceTv, userNameTv, userPhoneTv, address1Tv, address2Tv, roadNoTv, paymentStatus;
    private ExtendedFloatingActionButton completeOrder;
    private String userId, pushId;
    private DatabaseReference acceptOrderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_details);
        inItView();

        Intent intent = getIntent();
        dailyOrder = (DailyOrder) intent.getSerializableExtra("dailyInfo");
        pushId = dailyOrder.getPushId();
        acceptOrderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");
        String uPrice = String.valueOf(dailyOrder.getTotalPrice());
        categoryTv.setText(dailyOrder.getProductCategory());
        productTitleTv.setText(dailyOrder.getProductTitle());
        quantityTv.setText(dailyOrder.getProductQuantity());
        userNameTv.setText(dailyOrder.getUserName());
        userPhoneTv.setText(dailyOrder.getUserPhone());
        address1Tv.setText(dailyOrder.getUserAddress1());
        address2Tv.setText(dailyOrder.getUserAddress2());
        roadNoTv.setText(dailyOrder.getUserRoadNo());
        priceTv.setText("৳ "+uPrice);
        if(dailyOrder.getPaymentStatus().equals("cod")){
            paymentStatus.setText("Cash On Delivery");
        }

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUpCompleteOrder();
            }
        });
    }

    private void SetUpCompleteOrder() {
        acceptOrderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");
        HashMap hashMap = new HashMap();
        hashMap.put("status", "complete");
        acceptOrderRef.child(pushId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent dailyOrder = new Intent(DailyOrderDetailsActivity.this, DailyOrderActivity.class);
                    startActivity(dailyOrder);
                    Toast.makeText(DailyOrderDetailsActivity.this, "Data going to complete section !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inItView() {
        categoryTv = findViewById(R.id.categoryName);
        productTitleTv = findViewById(R.id.productTitle);
        quantityTv = findViewById(R.id.quantity);
        priceTv = findViewById(R.id.price);
        userNameTv = findViewById(R.id.userName);
        userPhoneTv = findViewById(R.id.userPhone);
        address1Tv = findViewById(R.id.address1);
        address2Tv = findViewById(R.id.address2);
        roadNoTv = findViewById(R.id.roadNo);
        paymentStatus = findViewById(R.id.paymentSatus);
        completeOrder = findViewById(R.id.completeBtn);
    }
}