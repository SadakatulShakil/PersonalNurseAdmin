package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.astronist.personalnurseadmin.R;
import com.astronist.personalnurseadmin.SalesReviewActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {
    private Context context;
    private ArrayList<PrescriptionNote> prescriptionNotesList;

    public NotificationAdapter(Context context, ArrayList<PrescriptionNote> prescriptionNotesList) {
        this.context = context;
        this.prescriptionNotesList = prescriptionNotesList;
    }

    @NonNull
    @Override
    public NotificationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_view, parent, false);
        return new NotificationAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.viewHolder holder, int position) {

        PrescriptionNote prescriptionNote = prescriptionNotesList.get(position);
        String dateUp = prescriptionNote.getDateTime();

        holder.dateTime.setText(dateUp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewNote = new Intent(context, SalesReviewActivity.class);
                viewNote.putExtra("noteDetails", prescriptionNote);
                context.startActivity(viewNote);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionNotesList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.upDate);
        }
    }
}
