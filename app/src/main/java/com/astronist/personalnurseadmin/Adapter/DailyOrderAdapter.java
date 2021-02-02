package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurseadmin.DailyOrderDetailsActivity;
import com.astronist.personalnurseadmin.Model.DailyOrder;
import com.astronist.personalnurseadmin.R;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyOrderAdapter extends RecyclerView.Adapter<DailyOrderAdapter.viewHolder> {
    private Context context;
    private ArrayList<DailyOrder> orderArrayList;

    public DailyOrderAdapter(Context context, ArrayList<DailyOrder> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public DailyOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_order_list_view, parent, false);
        return new DailyOrderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyOrderAdapter.viewHolder holder, int position) {
        DailyOrder dailyOrder = orderArrayList.get(position);
        String pTitle = dailyOrder.getProductTitle();
        String uTime = dailyOrder.getUpTime();
        String uDate = dailyOrder.getUpdate();
        String dateNTime = uDate+" "+uTime;
        String pQuantity = dailyOrder.getProductQuantity();
        String pTotalPrice = String.valueOf(dailyOrder.getTotalPrice());

        holder.productTitle.setText(pTitle);
        holder.timeAndDate.setText(dateNTime);
        holder.quantity.setText("Quantity :"+pQuantity);
        holder.totalPrice.setText("Total price : ৳ "+pTotalPrice);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(context, DailyOrderDetailsActivity.class);
                details.putExtra("dailyInfo", dailyOrder);
                context.startActivity(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView timeAndDate, productTitle, quantity, totalPrice;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            timeAndDate = itemView.findViewById(R.id.orderDateTime);
            productTitle = itemView.findViewById(R.id.productTitle);
            quantity = itemView.findViewById(R.id.productQuantity);
            totalPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
