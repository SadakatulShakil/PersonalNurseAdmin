package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.astronist.personalnurseadmin.Adapter.ProductAdapter;
import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView productRecyclerView;
    private ArrayList<ProductInfo> mProductInfoList = new ArrayList<>();
    private ProductAdapter mProductAdapter;
    private DatabaseReference productRef;
    public static final String TAG = "Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        inItView();
        productRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        mProductAdapter = new ProductAdapter(mProductInfoList, ProductActivity.this);
        productRecyclerView.setAdapter(mProductAdapter);

        retrieveProductList();

    }

    private void retrieveProductList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot productSnap :snapshot.getChildren()){

                    ProductInfo productInfo = productSnap.getValue(ProductInfo.class);

                    mProductInfoList.add(productInfo);
                }
                Log.d(TAG, "onDataChange: "+ mProductInfoList.size());
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        productRecyclerView = findViewById(R.id.productRecycleView);
    }
}