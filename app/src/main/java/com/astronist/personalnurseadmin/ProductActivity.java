package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.CategoryAdapter;
import com.astronist.personalnurseadmin.Adapter.ProductAdapter;
import com.astronist.personalnurseadmin.Adapter.SubcategoryAdapter;
import com.astronist.personalnurseadmin.Model.Category;
import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.astronist.personalnurseadmin.Model.SubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView productRecyclerView;
    private ArrayList<ProductInfo> mProductInfoList = new ArrayList<>();
    private ProductAdapter mProductAdapter;
    private DatabaseReference productRef;
    private DatabaseReference searchRef;
    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private Spinner categorySpinner;
    private String categoryName;
    private CategoryAdapter mCategoryAdapter;
    private TextView goSearch, cancelSearch;
    private EditText searchView;
    public static final String TAG = "Product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        inItCategoryList();
        inItView();
        searchRef = FirebaseDatabase.getInstance().getReference("ProductInfo");
        productRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        mProductAdapter = new ProductAdapter(mProductInfoList, ProductActivity.this);
        productRecyclerView.setAdapter(mProductAdapter);
        retrieveProductList();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = searchView.getText().toString().trim();
                if (searchText.isEmpty()) {
                    cancelSearch.setVisibility(View.GONE);
                }else {
                    searchText = searchText.substring(0,1).toUpperCase() + searchText.substring(1).toLowerCase();
                    searchByProductName(searchText);
                    cancelSearch.setVisibility(View.VISIBLE);
                    cancelSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchView.setText("");
                            retrieveProductList();
                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        searchView.addTextChangedListener(textWatcher);

    }

    private void inItCategoryList() {
        mCategoryList.add(new Category("Select Category"));
        mCategoryList.add(new Category("Kids & Mom's"));
        mCategoryList.add(new Category("Medical Accessories"));
        mCategoryList.add(new Category("Nutrition"));
        mCategoryList.add(new Category("Daily Drug"));

    }

    private void retrieveProductList() {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        productRef = fdb.getReference("ProductInfo");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductInfoList.clear();
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
        searchView = findViewById(R.id.searchView);
        cancelSearch = findViewById(R.id.cancelSearch);
        productRecyclerView = findViewById(R.id.productRecycleView);
        categorySpinner = findViewById(R.id.categorySpinner);

        mCategoryAdapter = new CategoryAdapter(ProductActivity.this, mCategoryList);
        categorySpinner.setAdapter(mCategoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    Category clickedCategory = (Category) parent.getItemAtPosition(position);

                    categoryName = clickedCategory.getCategoryName();
                    Toast.makeText(ProductActivity.this, categoryName + " is selected !", Toast.LENGTH_SHORT).show();
                    searchProductByCategory(categoryName);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void searchByProductName(String searchKey) {
        //////Search By product Name/////////////
        Query nameQuery = searchRef.orderByChild("title")
                .startAt(searchKey)
                .endAt(searchKey+"\uf8ff");

        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductInfoList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ProductInfo productInfo = userSnapshot.getValue(ProductInfo.class);
                    mProductInfoList.add(productInfo);
                }
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchProductByCategory(String categoryName) {
        ///////Search By Category/////////////
        Query nameQuery = searchRef.orderByChild("category")
                .startAt(categoryName)
                .endAt(categoryName+"\uf8ff");

        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProductInfoList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    ProductInfo productInfo = userSnapshot.getValue(ProductInfo.class);
                    mProductInfoList.add(productInfo);
                }
                mProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}