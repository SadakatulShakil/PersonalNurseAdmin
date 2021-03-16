package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class StatusShowActivity extends AppCompatActivity {

    private ImageView closeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_show);
        closeBtn = findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent homeIntent = new Intent(StatusShowActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StatusShowActivity.this);
        alertDialog.setTitle("Going Home");
        alertDialog.setMessage("Do you want to go Home !");
        alertDialog.setIcon(R.drawable.ic_home);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent homeIntent = new Intent(StatusShowActivity.this, MainActivity.class);
                startActivity(homeIntent);
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
}