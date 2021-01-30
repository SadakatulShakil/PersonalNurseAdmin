package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurseadmin.MediOrderAcceptActivity;
import com.astronist.personalnurseadmin.Model.MedicineOrder;
import com.astronist.personalnurseadmin.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedOrderAdapter extends RecyclerView.Adapter<MedOrderAdapter.viewHolder> {
    private Context context;
    private ArrayList<MedicineOrder> medicineOrderArrayList;

    public MedOrderAdapter(Context context, ArrayList<MedicineOrder> medicineOrderArrayList) {
        this.context = context;
        this.medicineOrderArrayList = medicineOrderArrayList;
    }

    @NonNull
    @Override
    public MedOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_order, parent, false);
        return new MedOrderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedOrderAdapter.viewHolder holder, int position) {

        MedicineOrder medicineOrder = medicineOrderArrayList.get(position);
        String uName = medicineOrder.getCustomerName();
        String uPhone = medicineOrder.getCustomerPhone();
        String date = medicineOrder.getOrderDate();
        String time = medicineOrder.getOrderTime();
        String orderDate = date+" "+time;
        String amount = medicineOrder.getTotalPrice();

        holder.name.setText(uName);
        holder.phone.setText(uPhone);
        holder.date.setText(orderDate);
        holder.amount.setText(amount+" taka");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, MediOrderAcceptActivity.class);
            intent.putExtra("mediOrderInfo", medicineOrder);
            context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineOrderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView name, phone, date, amount;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.userName);
            phone = itemView.findViewById(R.id.userPhone);
            date = itemView.findViewById(R.id.orderDate);
            amount = itemView.findViewById(R.id.orderAmount);
        }
    }
}
