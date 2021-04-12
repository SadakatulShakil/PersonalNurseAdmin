package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.DailyOrderAdapter;
import com.astronist.personalnurseadmin.Adapter.MedOrderAdapter;
import com.astronist.personalnurseadmin.Adapter.PrescriptionAdapter;
import com.astronist.personalnurseadmin.Adapter.ReviewAdapter;
import com.astronist.personalnurseadmin.Model.Admin;
import com.astronist.personalnurseadmin.Model.DailyOrder;
import com.astronist.personalnurseadmin.Model.MedicineOrder;
import com.astronist.personalnurseadmin.Model.PrescriptionInfo;
import com.astronist.personalnurseadmin.Model.PrescriptionNote;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar dToolbar;
    FirebaseAuth firebaseAuth;
    private View headerView;
    private TextView logOut, notifications, givenPrices, medicineOrderCount, completeOrderCount, DailyOrderCount, UnsolvedOrderCount, solvedOrderCount, salesReviewCount;
    private DatabaseReference userRef, presRef, reviewRef;
    private ConstraintLayout uploadbtn;
    private SharedPreferences preferences;
    private Admin userInfo;
    private PrescriptionNote pressNoteInfo;
    private String retrievedToken, salesId, notificationCount, reviewCount;
    private ArrayList<Admin> mUserList = new ArrayList<>();
    private ArrayList<Admin> mUserList2 = new ArrayList<>();
    private ArrayList<PrescriptionNote> mPresNoteList = new ArrayList<>();
    private ArrayList<PrescriptionNote> mRevNoteList = new ArrayList<>();
    private ArrayList<PrescriptionNote> reviewNoteArrayList = new ArrayList<>();
    private RelativeLayout masterAdmin, generalAdmin;
    private ActionBarDrawerToggle drawerToggle;
    private String adminPhone = "01777807066", adminPassword = "PNAdmin";
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    public static final String TAG = "Main";
    private CardView unsolvedPresCard, solvedPresCard, medicineOrderCard, dailyOrder, completeOrderCard, newNotiLayout, newRevLayout, salesReviewLayout;
    private MedOrderAdapter mMedOrderAdapter;
    private DatabaseReference medOrderRef, dailyOrderRef, prescribeRef;
    private ArrayList<MedicineOrder> mMedOrderList = new ArrayList<>();
    private ArrayList<MedicineOrder> medicineOrderArrayList = new ArrayList<>();
    private ArrayList<DailyOrder> dailyOrderArrayList = new ArrayList<>();
    private ArrayList<PrescriptionInfo> mUnSolveAlloInfoList = new ArrayList<>();
    private ArrayList<PrescriptionInfo> mSolveAlloInfoList = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();
    private String newMedicineOrderCount, comOrderCount, pendingDailyCount, userId, unsolvedCount, solvedCount, salesReportCount;
    private int comDailyOrder, comMedCount, comOrder=0;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inItView();
        getUserInfo();
        getAllAdminData();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, dToolbar,
                R.string.drawer_open, R.string.drawer_closed);
        drawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerToggle.setDrawerArrowDrawable(drawerToggle.getDrawerArrowDrawable());
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);
        if(retrievedToken.equals(adminPhone)){
            masterAdmin.setVisibility(View.VISIBLE);
            generalAdmin.setVisibility(View.GONE);
            drawerToggle.setDrawerIndicatorEnabled(true);
            logOut.setVisibility(View.GONE);
        }else{
            masterAdmin.setVisibility(View.GONE);
            generalAdmin.setVisibility(View.VISIBLE);
            drawerToggle.setDrawerIndicatorEnabled(false);
            logOut.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    finish();
                    preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN", null).apply();
                    Intent intent = new Intent(MainActivity.this, RegisterAndLogInActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Successfully Log Out", Toast.LENGTH_LONG).show();
                }
            });
        }
        firebaseAuth = FirebaseAuth.getInstance();
        initNavigationViewDrawer();

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload = new Intent(MainActivity.this, UploadProductActivity.class);
                startActivity(upload);
            }
        });

        unsolvedPresCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this, UnsolvedPrescriptionActivity.class);
                startActivity(inten);
            }
        });

        solvedPresCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SolvedPrescriptionActivity.class);
                startActivity(intent);
            }
        });

        medicineOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewMedicineOrderActivity.class);
                startActivity(intent);
            }
        });

        dailyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyOrderActivity.class);
                startActivity(intent);
            }
        });

        completeOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent complete = new Intent(MainActivity.this, CompleteOrderActivity.class);
                startActivity(complete);
            }
        });

        salesReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllAdminData() {
        getNewMedicineOrder();
        getCompleteOrder();
        getDailyOrder();
        getUnsolvedPrescribe();
        getSolvedPrescribe();
        getReviewList();

    }

    private void getReviewList() {
        presRef = FirebaseDatabase.getInstance().getReference().child("SalesReview");
        presRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    reviewNoteArrayList.add(pressNoteInfo);

                }
                salesReportCount = String.valueOf(reviewNoteArrayList.size());
                progressBar1.setVisibility(View.GONE);
                if(salesReportCount!=null){

                    salesReviewCount.setText(salesReportCount+" items");
                }else{
                    salesReviewCount.setText("0"+" items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSolvedPrescribe() {
        progressBar1.setVisibility(View.VISIBLE);
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo");

        prescribeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                    Log.d(TAG, "onData: " + prescriptionInfo.toString());
                    if(prescriptionInfo.getStatus().equals("Solved")){
                        mSolveAlloInfoList.add(prescriptionInfo);
                    }
                    solvedCount = String.valueOf(mSolveAlloInfoList.size());
                    progressBar1.setVisibility(View.GONE);
                    if(solvedCount!=null){
                        solvedOrderCount.setText(solvedCount+" items");
                    }else{

                        solvedOrderCount.setText("0"+" items");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUnsolvedPrescribe() {
        progressBar1.setVisibility(View.VISIBLE);
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        prescribeRef = fdb.getReference("PrescriptionInfo");

        prescribeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    userList.add(productSnap.getKey());
                }
                Log.d(TAG, "onUserData: "+ userList);
                for(int i= 0; i<userList.size(); i++){
                    userId = userList.get(i);
                    Log.d(TAG, "getUserId: "+ userId);

                    prescribeRef.child(userId).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot productSnap : snapshot.getChildren()) {

                                PrescriptionInfo prescriptionInfo = productSnap.getValue(PrescriptionInfo.class);

                                Log.d(TAG, "onData: " + prescriptionInfo.toString());
                                if(prescriptionInfo.getStatus().equals("unsolved")){

                                    mUnSolveAlloInfoList.add(prescriptionInfo);
                                }
                                unsolvedCount = String.valueOf(mUnSolveAlloInfoList.size());
                                Log.d(TAG, "onUnsolved: "+ unsolvedCount);
                                progressBar1.setVisibility(View.GONE);
                                if(unsolvedCount!= null){

                                    UnsolvedOrderCount.setText(unsolvedCount+" items");
                                }else{
                                    UnsolvedOrderCount.setText("0"+" items");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDailyOrder() {
        progressBar1.setVisibility(View.VISIBLE);
        dailyOrderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");

        dailyOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyOrderArrayList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    DailyOrder dailyOrder = productSnap.getValue(DailyOrder.class);
                    if(dailyOrder.getStatus().equals("pending")){

                        dailyOrderArrayList.add(dailyOrder);
                    }
                }

                Log.d(TAG, "onDaily: " + dailyOrderArrayList.size());
                pendingDailyCount = String.valueOf(dailyOrderArrayList.size());
                progressBar1.setVisibility(View.GONE);
                if(pendingDailyCount!=null){

                    DailyOrderCount.setText(pendingDailyCount+" items");
                }else{
                    DailyOrderCount.setText("0"+" items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCompleteOrder() {
        progressBar1.setVisibility(View.VISIBLE);
        medOrderRef = FirebaseDatabase.getInstance().getReference("Order");
        medOrderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                medicineOrderArrayList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    MedicineOrder medicineOrder = productSnap.getValue(MedicineOrder.class);

                    if(medicineOrder.getStatus().equals("complete")){
                        medicineOrderArrayList.add(medicineOrder);
                    }
                }
                comMedCount = medicineOrderArrayList.size();
                Log.d(TAG, "onCOmMedicine: "+comMedCount);
                getComDailyOrder(comMedCount);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getComDailyOrder(int comMedCount) {
        dailyOrderRef = FirebaseDatabase.getInstance().getReference("DailyOrder");

        dailyOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyOrderArrayList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    DailyOrder dailyOrder = productSnap.getValue(DailyOrder.class);
                    if(dailyOrder.getStatus().equals("complete")){

                        dailyOrderArrayList.add(dailyOrder);
                    }
                }
                progressBar1.setVisibility(View.GONE);
                comDailyOrder = dailyOrderArrayList.size();
                Log.d(TAG, "onComDaily: "+ comDailyOrder);

                comOrder = comMedCount+comDailyOrder;
                comOrderCount = String.valueOf(comOrder);
                if(comOrderCount!=null){

                    completeOrderCount.setText(comOrderCount+" items");
                }else{
                    completeOrderCount.setText("0"+" items");
                }
                Log.d(TAG, "onComplete: "+comOrderCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNewMedicineOrder() {
        progressBar1.setVisibility(View.VISIBLE);
        medOrderRef = FirebaseDatabase.getInstance().getReference("Order");
        medOrderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mMedOrderList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {

                    MedicineOrder medicineOrder = productSnap.getValue(MedicineOrder.class);

                    Log.d(TAG, "onData: " + medicineOrder.toString());
                    if(medicineOrder.getStatus().equals("pending")){
                        mMedOrderList.add(medicineOrder);
                    }

                }
                newMedicineOrderCount = String.valueOf(mMedOrderList.size());
                Log.d(TAG, "onNewMedicine: "+newMedicineOrderCount);
                progressBar1.setVisibility(View.GONE);
                if(newMedicineOrderCount!=null){

                    medicineOrderCount.setText(newMedicineOrderCount+" items");
                }else{
                    medicineOrderCount.setText("0"+" items");
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserInfo() {
        userRef = FirebaseDatabase.getInstance().getReference().child("AdminInfo");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    userInfo = productSnap.getValue(Admin.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    mUserList.add(userInfo);
                    if(userInfo.getPhoneNo().equals(retrievedToken)){
                        salesId = userInfo.getPushId();
                        getPresNotification(salesId);
                        clickedEvennts(salesId);
                        getReviewedNote(salesId);
                        clickReview(salesId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickReview(String salesId) {
    newRevLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AdminReviewActivity.class);
            intent.putExtra("id", salesId);
            startActivity(intent);
        }
    });
    }

    private void getReviewedNote(String salesId) {
        reviewRef = FirebaseDatabase.getInstance().getReference().child("SalesReview");
        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    if(salesId.equals(pressNoteInfo.getSalesId())){
                        mRevNoteList.add(pressNoteInfo);
                        reviewCount = String.valueOf(mRevNoteList.size());
                        Log.d(TAG, "onReviewChange: "+pressNoteInfo.toString()+reviewCount);
                        progressBar2.setVisibility(View.GONE);
                    }
                }
                if(reviewCount!=null){
                    givenPrices.setText(reviewCount+" items");
                    salesReviewCount.setText(reviewCount+" items");
                }else{
                    givenPrices.setText("0"+" items");
                    salesReviewCount.setText("0"+" items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickedEvennts(String salesId) {
        newNotiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminNotificationActivity.class);
                intent.putExtra("id", salesId);
                startActivity(intent);
            }
        });
    }

    private void getPresNotification(String salesId) {
        presRef = FirebaseDatabase.getInstance().getReference().child("PresNotification");
        presRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    pressNoteInfo = productSnap.getValue(PrescriptionNote.class);
                    //Log.d(TAG, "onAdmin: "+ adminInfo.toString());
                    if(salesId.equals(pressNoteInfo.getSalesId())){
                        mPresNoteList.add(pressNoteInfo);
                        notificationCount = String.valueOf(mPresNoteList.size());
                    }
                }
                if(notificationCount!=null){
                    notifications.setText(notificationCount+" items");
                }else{
                    notifications.setText("0"+" items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNavigationViewDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch ((item.getItemId())) {

                    case R.id.addProducts:
                       Intent upload = new Intent(MainActivity.this, UploadProductActivity.class);
                       startActivity(upload);
                        //Toast.makeText(MainActivity.this, "Settings Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.product:
                        /*Intent intent = new Intent(context, FeedBackActivity.class);
                        startActivity(intent);*/
                        //Toast.makeText(MainActivity.this, "FeedBack Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        Intent product = new Intent(MainActivity.this, ProductActivity.class);
                        startActivity(product);
                        break;
                    case R.id.order:
                        /*Intent intent1 = new Intent(context, AdminActivity.class);
                        startActivity(intent1);*/
                        Toast.makeText(MainActivity.this, "Admin Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.message:
                       /* Intent intent3 = new Intent(context, AboutUsActivity.class);
                        startActivity(intent3);*/
                        Toast.makeText(MainActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.slider:
                        Intent intent3 = new Intent(MainActivity.this, SliderReviewActivity.class);
                        startActivity(intent3);
                        //Toast.makeText(MainActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;


                    case R.id.logOut:
                        firebaseAuth.signOut();
                        finish();
                        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN", null).apply();
                        Intent intent = new Intent(MainActivity.this, RegisterAndLogInActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Successfully Log Out", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void inItView() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationDrawer);
        dToolbar = findViewById(R.id.toolbar);
        uploadbtn = findViewById(R.id.upload);
        unsolvedPresCard = findViewById(R.id.unsolvedPrescribeLayout);
        solvedPresCard = findViewById(R.id.solvedPrescribeLayout);
        medicineOrderCard = findViewById(R.id.newMedicineOrderLayout);
        dailyOrder = findViewById(R.id.dailyOrderLayout);
        completeOrderCard = findViewById(R.id.completeOrderLayout);
        masterAdmin = findViewById(R.id.masterAdminPanel);
        generalAdmin = findViewById(R.id.generalAdmin);
        logOut = findViewById(R.id.logout);
        notifications = findViewById(R.id.newNotificationCount);
        newNotiLayout = findViewById(R.id.newNotificationLayout);
        givenPrices = findViewById(R.id.givenPriceCount);
        newRevLayout = findViewById(R.id.givenPriceLayout);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        ///medicineOrderCount, completeOrderCount, DailyOrderCount, UnsolvedOrderCount, solvedOrderCount///
        medicineOrderCount = findViewById(R.id.newOrderCount);
        completeOrderCount = findViewById(R.id.deliveredOrderCount);
        DailyOrderCount = findViewById(R.id.completeOrderAmount);
        UnsolvedOrderCount = findViewById(R.id.totalRevenue);
        solvedOrderCount = findViewById(R.id.itemsCount);
        salesReviewCount = findViewById(R.id.incompleteItemsAmount);
        salesReviewLayout = findViewById(R.id.anotherLayout);
    }
}