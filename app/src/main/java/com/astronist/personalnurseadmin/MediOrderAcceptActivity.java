package com.astronist.personalnurseadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.astronist.personalnurseadmin.Model.MedicineOrder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MediOrderAcceptActivity extends AppCompatActivity {

    private TextView orderDate, medicineList, customerName, customerPhone, addressLine1, addressLine2, roadNo, amountOfDay, singleDayPrice, totalPrice, paymentType;
    private ExtendedFloatingActionButton completeOrder;
    private MedicineOrder medicineOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_order_accept);
        inItView();

        Intent intent = getIntent();
        medicineOrder = (MedicineOrder) intent.getSerializableExtra("mediOrderInfo");

        orderDate.setText(medicineOrder.getOrderDate()+" "+medicineOrder.getOrderTime());
        medicineList.setText(medicineOrder.getMedicineList());
        customerName.setText(medicineOrder.getCustomerName());
        customerPhone.setText(medicineOrder.getCustomerPhone());
        addressLine1.setText(medicineOrder.getAddressLine1());
        addressLine2.setText(medicineOrder.getAddressLine2());
        roadNo.setText(medicineOrder.getRoadNo()+ " no. road");
        amountOfDay.setText(medicineOrder.getDayCount()+" days");
        singleDayPrice.setText(medicineOrder.getSinglePrice()+" taka");
        totalPrice.setText(medicineOrder.getTotalPrice()+" taka");
        String pMethod = medicineOrder.getPaymentMethod();
        if(pMethod.equals("cod")){

            paymentType.setText("Cash on delivery");
        }
    }

    private void inItView() {
        orderDate = findViewById(R.id.orderDateTime);
        medicineList = findViewById(R.id.medicineList);
        customerName = findViewById(R.id.userName);
        customerPhone = findViewById(R.id.userPhone);
        addressLine1 = findViewById(R.id.addressLine1);
        addressLine2 = findViewById(R.id.addressLine2);
        roadNo = findViewById(R.id.userRoadNo);
        amountOfDay = findViewById(R.id.dayCount);
        singleDayPrice = findViewById(R.id.singlePrice);
        totalPrice = findViewById(R.id.totalPrice);
        paymentType = findViewById(R.id.paymentType);
    }
}