package com.astronist.personalnurseadmin.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.Admin;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.astronist.personalnurseadmin.R;
import com.astronist.personalnurseadmin.StatusShowActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.astronist.personalnurseadmin.SolvedPrescriptionActivity.TAG;

public class SalesSelectAdapter extends RecyclerView.Adapter<SalesSelectAdapter.viewHolder> {
    private int selectedStartPosition = -1;
    private Context context;
    private ArrayList<Admin> adminArrayList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private String prescriptionImage;
    private DatabaseReference mAdminReference;
    private String dateTime;

    public SalesSelectAdapter(Context context, ArrayList<Admin> adminArrayList, String prescriptionImage) {
        this.context = context;
        this.adminArrayList = adminArrayList;
        this.prescriptionImage = prescriptionImage;
    }

    @NonNull
    @Override
    public SalesSelectAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_list, parent, false);
        return new SalesSelectAdapter.viewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull SalesSelectAdapter.viewHolder holder, int position) {

        Admin adminInfo = adminArrayList.get(position);
        holder.phone.setText(adminInfo.getPhoneNo());
        holder.shopName.setText(adminInfo.getShopName());
        holder.area.setText(adminInfo.getShopArea());

        dateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Share Prescription");
                alertDialog.setMessage("Do you really want to share now !");
                alertDialog.setIcon(R.drawable.ic_share);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdminReference = FirebaseDatabase.getInstance().getReference("PresNotification");
                        String uploadId = mAdminReference.push().getKey();
                        PrescriptionNote prescriptionNote = new PrescriptionNote(uploadId, adminInfo.getPushId(), prescriptionImage, dateTime);
                        mAdminReference.child(uploadId).setValue(prescriptionNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    ((Activity)context).finish();
                                    Intent successIntent = new Intent(context, StatusShowActivity.class);
                                    context.startActivity(successIntent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { {
                        dialog.dismiss();

                    }
                    }
                });

                alertDialog.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return adminArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView phone, shopName, area, selectOption;
        private LinearLayout selectLay;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            phone = itemView.findViewById(R.id.salesPhone);
            shopName = itemView.findViewById(R.id.shopName);
            area = itemView.findViewById(R.id.shopArea);
            selectOption = itemView.findViewById(R.id.selectedIcon);
            selectLay = itemView.findViewById(R.id.selectLay);
        }
    }
}
