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
import com.astronist.personalnurseadmin.ViewReviewActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.viewHolder> {
    private Context context;
    private ArrayList<PrescriptionNote> reviewNotesList;

    public ReviewAdapter(Context context, ArrayList<PrescriptionNote> reviewNotesList) {
        this.context = context;
        this.reviewNotesList = reviewNotesList;
    }

    @NonNull
    @Override
    public ReviewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_view, parent, false);
        return new ReviewAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.viewHolder holder, int position) {
        PrescriptionNote prescriptionNote = reviewNotesList.get(position);
        String dateUp = prescriptionNote.getDateTime();

        holder.dateTime.setText(dateUp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewNote = new Intent(context, ViewReviewActivity.class);
                viewNote.putExtra("noteDetails", prescriptionNote);
                context.startActivity(viewNote);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewNotesList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.upDate);
        }
    }
}
