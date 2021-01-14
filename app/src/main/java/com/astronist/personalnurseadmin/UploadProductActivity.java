package com.astronist.personalnurseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.personalnurseadmin.Adapter.CategoryAdapter;
import com.astronist.personalnurseadmin.Adapter.SubcategoryAdapter;
import com.astronist.personalnurseadmin.Model.Category;
import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.astronist.personalnurseadmin.Model.SubCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UploadProductActivity extends AppCompatActivity {
    private Spinner categorySpinner, subCategorySpinner;
    private TextView choosePhoto;
    private EditText title, description, stock, regularPrice, sellingPrice, discount;
    private ExtendedFloatingActionButton uploadNow;
    private Uri imageUri;
    private ArrayList<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<SubCategory> mSubCategoryList1 = new ArrayList<>();
    private ArrayList<SubCategory> mSubCategoryList2 = new ArrayList<>();
    private ArrayList<SubCategory> mSubCategoryList3 = new ArrayList<>();
    private SubcategoryAdapter mSubCategoryAdapter;
    private String categoryName, subCategoryName;
    private ImageView previewImage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressBar progressBar;
    private String pTitle, pDescription, addingTime, addingDate;
    private int stockAvailable, pDiscount;
    private double pRegularPrice, pSellingPrice, pActualPrice, calculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        inItCategoryList();
        inItSubCategoryList1();
        inItSubCategoryList2();
        inItSubCategoryList3();
        inItView();

        mStorageRef = FirebaseStorage.getInstance().getReference("ProductInfo");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ProductInfo");
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });
        uploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProductInfo();
            }
        });


    }

    private void uploadProductInfo() {
        progressBar.setVisibility(View.VISIBLE);
        pTitle = title.getText().toString().trim();
        pDescription = description.getText().toString().trim();
        stockAvailable = Integer.parseInt(stock.getText().toString().trim());
        pRegularPrice = Double.parseDouble(regularPrice.getText().toString().trim());
        pSellingPrice = Double.parseDouble(sellingPrice.getText().toString().trim());
        pDiscount = Integer.parseInt(discount.getText().toString().trim());
        calculation = pSellingPrice - (double) pDiscount;
        pActualPrice = calculation;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        addingTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        addingDate = myDateFormat.format(calendar.getTime());

        if (imageUri != null) {
            StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 500);
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            //Toast.makeText(UploadProductActivity.this, "image Upload Successful!", Toast.LENGTH_SHORT).show();
                            uploadFullInfo(pTitle, pDescription, categoryName, subCategoryName, url.toString(), stockAvailable, pRegularPrice,
                                    pSellingPrice, pActualPrice, pDiscount, addingDate, addingTime);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(UploadProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No file Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFullInfo(String pTitle, String pDescription, String categoryName,
                                String subCategoryName, String url, int stockAvailable,
                                double pRegularPrice, double pSellingPrice, double pActualPrice,
                                int pDiscount, String addingDate, String addingTime) {

        ProductInfo productInfo = new ProductInfo(pTitle, categoryName, subCategoryName,
                url, pDescription, stockAvailable, pRegularPrice, pSellingPrice, pDiscount, pActualPrice,
                addingDate, addingTime);
        String uploadId = mDatabaseRef.push().getKey();
        mDatabaseRef.
                child(uploadId).
                setValue(productInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(UploadProductActivity.this, "Info Upload Successful!", Toast.LENGTH_SHORT).show();
                    Intent infoProduct = new Intent(UploadProductActivity.this, ProductActivity.class);
                    startActivity(infoProduct);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void OpenFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            String path = imageUri.getLastPathSegment();
            Log.d(TAG, "onActivityResult: " + path);
            Picasso.get().load(imageUri).into(previewImage);
            choosePhoto.setText(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                previewImage.setClipToOutline(true);
            }
            //saveImageBtn.setVisibility(View.VISIBLE);
        }
    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void inItCategoryList() {
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new Category("Select Category"));
        mCategoryList.add(new Category("Kids & Mom's"));
        mCategoryList.add(new Category("Medical Accessories"));
        mCategoryList.add(new Category("Nutrition"));

    }

    private void inItSubCategoryList1() {

        mSubCategoryList1.add(new SubCategory("Select Sub Category"));
        mSubCategoryList1.add(new SubCategory("Baby diapers"));
        mSubCategoryList1.add(new SubCategory("Baby wipers"));
        mSubCategoryList1.add(new SubCategory("Women's care"));
    }

    private void inItSubCategoryList2() {
        mSubCategoryList2.add(new SubCategory("Select Sub Category"));
        mSubCategoryList2.add(new SubCategory("Blood pressure machine"));
        mSubCategoryList2.add(new SubCategory("Diabetics machine "));
        mSubCategoryList2.add(new SubCategory("Medical mask"));
        mSubCategoryList2.add(new SubCategory("Hand gloves"));
        mSubCategoryList2.add(new SubCategory("Antibacterial"));
    }

    private void inItSubCategoryList3() {
        mSubCategoryList3.add(new SubCategory("Select Sub Category"));
        mSubCategoryList3.add(new SubCategory("Baby Milk"));
        mSubCategoryList3.add(new SubCategory("Chocolate"));
    }

    private void inItView() {
        choosePhoto = findViewById(R.id.chooseBtn);
        title = findViewById(R.id.titleEt);
        description = findViewById(R.id.descriptionEt);
        stock = findViewById(R.id.stockEt);
        regularPrice = findViewById(R.id.regularPriceEt);
        sellingPrice = findViewById(R.id.sellingPriceEt);
        uploadNow = findViewById(R.id.uploadBtn);
        previewImage = findViewById(R.id.previewImage);
        progressBar = findViewById(R.id.progressBar);
        discount = findViewById(R.id.discountEt);


        /////////Spinner Adapter work/////////
        categorySpinner = findViewById(R.id.categorySpinner);
        subCategorySpinner = findViewById(R.id.subCategorySpinner);
        mCategoryAdapter = new CategoryAdapter(UploadProductActivity.this, mCategoryList);
        categorySpinner.setAdapter(mCategoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedCategory = (Category) parent.getItemAtPosition(position);

                categoryName = clickedCategory.getCategoryName();
                if (position > 0) {
                    subCategorySpinner.setVisibility(View.VISIBLE);
                    Toast.makeText(UploadProductActivity.this, categoryName + " is selected !", Toast.LENGTH_SHORT).show();
                    if (categoryName.equals("Kids & Mom's")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(UploadProductActivity.this, mSubCategoryList1);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                Toast.makeText(UploadProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else if (categoryName.equals("Medical Accessories")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(UploadProductActivity.this, mSubCategoryList2);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                Toast.makeText(UploadProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else if (categoryName.equals("Nutrition")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(UploadProductActivity.this, mSubCategoryList3);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                Toast.makeText(UploadProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}