package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.Admin;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterAndLogInActivity extends AppCompatActivity {

    private ConstraintLayout logInLay, registerLay;
    private EditText lPhoneEt, lPasswordEt, rPhoneEt, rEmailEt, rShopNameEt, rShopAreaEt, rPasswordEt;
    private TextView logInBtn, registerBtn, goToRegisterBtn, goToLogInBtn;
    private ProgressBar progressBar, progressBar1;
    private DatabaseReference mAdminReference;
    private String adminPhone = "01777807066", adminPassword = "PNAdmin";
    private ArrayList<Admin> adminList = new ArrayList<>();
    public static final String TAG = "admin";
    private Admin adminInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_log_in);
        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        if(retrievedToken!=null){
            Intent main = new Intent(RegisterAndLogInActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }
        inItView();
        getAdmin();
        mAdminReference = FirebaseDatabase.getInstance().getReference("AdminInfo");
        goToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInLay.setVisibility(View.GONE);
                registerLay.setVisibility(View.VISIBLE);
            }
        });
        goToLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInLay.setVisibility(View.VISIBLE);
                registerLay.setVisibility(View.GONE);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"clicked true");
                String rPhone = rPhoneEt.getText().toString().trim();
                String rEmail = rEmailEt.getText().toString().trim();
                String rShopName = rShopNameEt.getText().toString().trim();
                String rShopArea = rShopAreaEt.getText().toString().trim();
                String rPassword = rPasswordEt.getText().toString().trim();

                if (rPhone.isEmpty()) {
                    rPhoneEt.setError("This Field is required !");
                    rPhoneEt.requestFocus();
                    return;
                }

                if (rShopName.isEmpty()) {
                    rShopNameEt.setError("This Field is required !");
                    rShopNameEt.requestFocus();
                    return;
                }
                if (rShopArea.isEmpty()) {
                    rShopAreaEt.setError("This Field is required !");
                    rShopAreaEt.requestFocus();
                    return;
                }
                if (rPassword.isEmpty()) {
                    rPasswordEt.setError("This Field is required !");
                    rPasswordEt.requestFocus();
                    return;
                }
                if(!rPhone.isEmpty()){
                    for(int j=0; j<adminList.size(); j++){

                        if(rPhone.equals(adminList.get(j).getPhoneNo())){
                            Log.d(TAG, "onVerify: " + adminList.get(j).getPhoneNo());
                            rPhoneEt.setError("Number already exits !");
                            rPhoneEt.requestFocus();
                            return;
                        }
                    }
                    RegisterSetUp(rPhone, rEmail, rShopName, rShopArea, rPassword);
                }
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lPhone = lPhoneEt.getText().toString().trim();
                String lPassword = lPasswordEt.getText().toString().trim();

                if (lPhone.isEmpty()) {
                    lPhoneEt.setError("This Field is required !");
                    lPhoneEt.requestFocus();
                    return;
                }
                if (lPassword.isEmpty()) {
                    rPasswordEt.setError("This Field is required !");
                    rPasswordEt.requestFocus();
                    return;
                }

                if(!lPhone.isEmpty() && !lPassword.isEmpty()){
                    for(int i =0; i<adminList.size(); i++){
                        if((lPhone.equals(adminList.get(i).getPhoneNo()) && lPassword.equals(adminList.get(i).getPassword())) || lPhone.equals(adminPhone)&&lPassword.equals(adminPassword)){
                            finish();
                            Intent intent = new Intent(RegisterAndLogInActivity.this, MainActivity.class);
                            intent.putExtra("phoneNo", lPhone);
                            startActivity(intent);
                            SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            preferences.edit().putString("TOKEN", lPhone).apply();
                            Toast.makeText(RegisterAndLogInActivity.this, "Successfully Login !", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(RegisterAndLogInActivity.this, "successfully loged in ! ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });
    }

    private void getAdmin() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        mAdminReference = fdb.getReference("AdminInfo");
            mAdminReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot productSnap : snapshot.getChildren()) {
                        adminInfo = productSnap.getValue(Admin.class);
                        Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                        adminList.add(adminInfo);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void RegisterSetUp(final String rPhone, final String rEmail, final String rShopName, final String rShopArea, final String rPassword) {
        Log.d(TAG, "RegisterSetUp: " + "register function true");
        progressBar1.setVisibility(View.VISIBLE);

        String uploadId = mAdminReference.push().getKey();
        Admin admin = new Admin(uploadId, rPhone, rEmail, rShopName, rShopArea, rPassword);
        mAdminReference.child(uploadId).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    rPhoneEt.setText("");
                    rEmailEt.setText("");
                    rShopNameEt.setText("");
                    rShopAreaEt.setText("");
                    rPasswordEt.setText("");
                    progressBar1.setVisibility(View.GONE);
                registerLay.setVisibility(View.GONE);
                logInLay.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterAndLogInActivity.this, "Register successful !", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            progressBar1.setVisibility(View.GONE);
                Toast.makeText(RegisterAndLogInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inItView() {
        logInLay = findViewById(R.id.logInLay);
        registerLay = findViewById(R.id.registrationLay);
        lPhoneEt = findViewById(R.id.phoneEt);
        lPasswordEt = findViewById(R.id.passwordEt);
        rPhoneEt = findViewById(R.id.phoneEt1);
        rEmailEt = findViewById(R.id.emailEt);
        rShopNameEt = findViewById(R.id.shopEt);
        rShopAreaEt = findViewById(R.id.areaNameEt);
        rPasswordEt = findViewById(R.id.passwordEt1);
        registerBtn = findViewById(R.id.registerBt);
        logInBtn = findViewById(R.id.logInBt);
        goToRegisterBtn = findViewById(R.id.goToRegister);
        goToLogInBtn = findViewById(R.id.goToLogIn);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);

    }
}