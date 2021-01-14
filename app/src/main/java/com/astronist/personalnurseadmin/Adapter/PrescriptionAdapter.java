package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurseadmin.DetailsPrescription;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.astronist.personalnurseadmin.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.viewHolder>{
    private ArrayList<PrescriptionInfo> prescriptionInfoArrayList;
    private Context context;

    public PrescriptionAdapter(ArrayList<PrescriptionInfo> prescriptionInfoArrayList, Context context) {
        this.prescriptionInfoArrayList = prescriptionInfoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrescriptionAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescribe_view, parent, false);
        return new PrescriptionAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionAdapter.viewHolder holder, int position) {

        PrescriptionInfo prescriptionInfo = prescriptionInfoArrayList.get(position);
        String uDate = prescriptionInfo.getDate();
        String uTime = prescriptionInfo.getTime();

        holder.time.setText(uTime);
        holder.date.setText(uDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsPrescription.class);
                intent.putExtra("prescription_info", prescriptionInfo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionInfoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView date, time;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.upDate);
            time = itemView.findViewById(R.id.uploadTime);
        }
    }
}
