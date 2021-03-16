package com.astronist.personalnurseadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.squareup.picasso.Picasso;

public class ViewReviewActivity extends AppCompatActivity {
    private ImageView previewPrescription, mainPrescription, removeBtn;
    private LinearLayout mainLayout, mainImageLayout;
    private TextView medicineList, oneDayPrice;
    private PrescriptionNote prescriptionNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        inItView();
        Intent intent = getIntent();
        prescriptionNote = (PrescriptionNote) intent.getSerializableExtra("noteDetails");
        medicineList.setText(prescriptionNote.getMedicineList());
        oneDayPrice.setText(prescriptionNote.getOneDayAmount());
        Picasso.get().load(prescriptionNote.getPrescriptionImage()).into(previewPrescription);
        Picasso.get().load(prescriptionNote.getPrescriptionImage()).into(mainPrescription);

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
    }

    private void inItView() {
        previewPrescription = findViewById(R.id.previewImage);
        mainPrescription = findViewById(R.id.fullImage);
        mainLayout = findViewById(R.id.mainLay);
        mainImageLayout = findViewById(R.id.mainImageLay);
        medicineList = findViewById(R.id.medicineList);
        removeBtn = findViewById(R.id.remove);
        oneDayPrice = findViewById(R.id.oneDayTotal);
    }
}