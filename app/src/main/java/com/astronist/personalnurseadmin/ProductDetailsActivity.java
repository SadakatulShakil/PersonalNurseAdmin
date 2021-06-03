package com.astronist.personalnurseadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton fabEdit, fabDelete;
    private ImageView productImage;
    private TextView proTitle, proDescription, proStock, proRegularPrice, proSellingPrice;
    private ProductInfo productInfo;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        inItView();
        Intent proIntent = getIntent();
        productInfo = (ProductInfo) proIntent.getSerializableExtra("productData");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(productInfo.getUploadKey());
        Picasso.get().load(productInfo.getImageUrl()).into(productImage);
        proTitle.setText(productInfo.getTitle());
        proDescription.setText(productInfo.getDescription());
        int pStock = productInfo.getStockAvailable();
        String prStock = String.valueOf(pStock);
        proStock.setText(prStock);
        double prPrice = productInfo.getRegularPrice();
        double psPrice = productInfo.getActualSellingPrice();
        String prSPrice = String.valueOf(prPrice);
        String psSPrice = String.valueOf(psPrice);
        proRegularPrice.setText("৳ "+prSPrice);
        proSellingPrice.setText("৳ "+psSPrice);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, EditProductActivity.class);
                intent.putExtra("productData", productInfo);
                startActivity(intent);
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mDatabaseRef.removeValue();
                Toast.makeText(ProductDetailsActivity.this, "Successfully deleted !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inItView() {
        fabEdit = findViewById(R.id.fabEdit);
        fabDelete = findViewById(R.id.fabDelete);

        productImage = findViewById(R.id.productImage);
        proTitle = findViewById(R.id.productTitleTv);
        proDescription = findViewById(R.id.productDescriptionTv);
        proStock = findViewById(R.id.stockTv);
        proSellingPrice = findViewById(R.id.sellingPrice);
        proRegularPrice = findViewById(R.id.regularPrice);
    }
}