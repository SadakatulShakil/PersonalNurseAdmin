package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalesReviewActivity extends AppCompatActivity {
    private ImageView previewPrescription, mainPrescription, removeBtn;
    private LinearLayout mainLayout, mainImageLayout;
    private EditText medicineList, oneDayPrice;
    private ExtendedFloatingActionButton submitBtn, shareBtn;
    private String addingTime, addingDate;
    private DatabaseReference mReviewRef;
    private DatabaseReference mRemoveNoteRef;
    private String pushId,userId;
    private PrescriptionNote prescriptionNote;
    public static final String TAG = "detailsPrescription";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_review);
        inItView();
        Intent intent = getIntent();
        prescriptionNote = (PrescriptionNote) intent.getSerializableExtra("noteDetails");
        pushId = prescriptionNote.getUploadId();
        userId = prescriptionNote.getSalesId();
        Picasso.get().load(prescriptionNote.getPrescriptionImage()).into(previewPrescription);
        Picasso.get().load(prescriptionNote.getPrescriptionImage()).into(mainPrescription);
        mReviewRef = FirebaseDatabase.getInstance().getReference("SalesReview");
        mRemoveNoteRef = FirebaseDatabase.getInstance().getReference("PresNotification");

        previewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
                mainImageLayout.setVisibility(View.VISIBLE);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                mainImageLayout.setVisibility(View.GONE);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeSalesPriceList(userId);
                removeFromNotification(pushId);
            }
        });
    }

    private void storeSalesPriceList(String userId) {
        String uMedicine = medicineList.getText().toString().trim();
        String uOneDayPrice = oneDayPrice.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        addingTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        addingDate = myDateFormat.format(calendar.getTime());
        String dateTime = addingDate +" : "+ addingTime;
        finalStep(uMedicine, userId, uOneDayPrice, dateTime, prescriptionNote.getPrescriptionImage());
    }

    private void finalStep(final String uMedicine, String userId, final String uOneDayPrice, final String dateTime, final String prescriptionImage) {

        String uploadId = mReviewRef.push().getKey();
        PrescriptionNote prescriptionNote = new PrescriptionNote(uploadId, userId, prescriptionImage, dateTime, uMedicine, uOneDayPrice);

        mReviewRef.child(uploadId).setValue(prescriptionNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(SalesReviewActivity.this, "Info Upload Successful!", Toast.LENGTH_SHORT).show();
                    Intent infoProduct = new Intent(SalesReviewActivity.this, MainActivity.class);
                    startActivity(infoProduct);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SalesReviewActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFromNotification(String pushId) {
    mRemoveNoteRef.child(pushId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(SalesReviewActivity.this, "Notification reviewed !", Toast.LENGTH_SHORT).show();

            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(SalesReviewActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void inItView() {
        previewPrescription = findViewById(R.id.previewImage);
        mainPrescription = findViewById(R.id.fullImage);
        mainLayout = findViewById(R.id.mainLay);
        mainImageLayout = findViewById(R.id.mainImageLay);
        medicineList = findViewById(R.id.medicineList);
        submitBtn = findViewById(R.id.submitBtn);
        removeBtn = findViewById(R.id.remove);
        oneDayPrice = findViewById(R.id.oneDayTotal);
        shareBtn = findViewById(R.id.shareBtn);
    }
}