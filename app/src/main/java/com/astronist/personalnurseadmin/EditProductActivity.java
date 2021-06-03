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

public class EditProductActivity extends AppCompatActivity {
    private Spinner categorySpinner, subCategorySpinner;
    private TextView choosePhoto;
    private EditText title, description, stock, regularPrice, sellingPrice, discount;
    private ExtendedFloatingActionButton uploadNow;
    private ArrayList<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<SubCategory> mSubCategoryList1 = new ArrayList<>();
    private ArrayList<SubCategory> mSubCategoryList2 = new ArrayList<>();
    private ArrayList<SubCategory> mSubCategoryList3 = new ArrayList<>();
    private ArrayList<SubCategory> mSubCategoryList4 = new ArrayList<>();
    private SubcategoryAdapter mSubCategoryAdapter;
    private String categoryName, subCategoryName, uploadKey;
    private ImageView previewImage;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String pTitle, pDescription, addingTime, addingDate;
    private int stockAvailable, pDiscount, eDiscount, eStockAvailable;
    private double pRegularPrice, pSellingPrice, pActualPrice, calculation, eRegularPrice, eSellingPrice;
    private ProductInfo productInfo;
    public static final String TAG = "edit";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        inItCategoryList();
        inItSubCategoryList1();
        inItSubCategoryList2();
        inItSubCategoryList3();
        inItSubCategoryList4();
        inItView();
        Intent intent = getIntent();
        productInfo = (ProductInfo) intent.getSerializableExtra("productData");
        mStorageRef = FirebaseStorage.getInstance().getReference("ProductInfo");
        title.setText(productInfo.getTitle());
        description.setText(productInfo.getDescription());
        stockAvailable = productInfo.getStockAvailable();
        pDiscount = productInfo.getDiscountPercentage();
        pRegularPrice = productInfo.getRegularPrice();
        pSellingPrice = productInfo.getSellingPrice();
        stock.setText(String.valueOf(stockAvailable));
        discount.setText(String.valueOf(pDiscount));
        regularPrice.setText(String.valueOf(pRegularPrice));
        sellingPrice.setText(String.valueOf(pSellingPrice));
        Picasso.get().load(productInfo.getImageUrl()).into(previewImage);
        categorySpinner.setSelection(getIndex(categorySpinner, productInfo.getCategory()));
        uploadKey =productInfo.getUploadKey();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(uploadKey);

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
        eStockAvailable = Integer.parseInt(stock.getText().toString().trim());
        eRegularPrice = Double.parseDouble(regularPrice.getText().toString().trim());
        eSellingPrice = Double.parseDouble(sellingPrice.getText().toString().trim());
        eDiscount = Integer.parseInt(discount.getText().toString().trim());
        calculation = pSellingPrice - (double) eDiscount;
        pActualPrice = calculation;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        addingTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        addingDate = myDateFormat.format(calendar.getTime());

        if (imageUri == null ) {
            if(subCategoryName.equals(null)){
                Toast.makeText(EditProductActivity.this, "Please select a Sub Category", Toast.LENGTH_SHORT).show();
            }else{
                uploadFullInfo(pTitle, pDescription, categoryName, subCategoryName, productInfo.getImageUrl(), eStockAvailable, eRegularPrice,
                        eSellingPrice, pActualPrice, eDiscount, addingDate, addingTime);
            }

        } else {
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
                            if(subCategoryName.equals(null)){
                                Toast.makeText(EditProductActivity.this, "Please select a Sub Category", Toast.LENGTH_SHORT).show();
                            }else{

                                //Toast.makeText(UploadProductActivity.this, "image Upload Successful!", Toast.LENGTH_SHORT).show();
                                uploadFullInfo(pTitle, pDescription, categoryName, subCategoryName, url.toString(), eStockAvailable, eRegularPrice,
                                        eSellingPrice, pActualPrice, eDiscount, addingDate, addingTime);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(EditProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    private void uploadFullInfo(String pTitle, String pDescription, String categoryName, String subCategoryName,
                                String url, int eStockAvailable, double eRegularPrice, double eSellingPrice,
                                double pActualPrice, int eDiscount, String addingDate, String addingTime) {


        ProductInfo productInfo = new ProductInfo(uploadKey, pTitle, categoryName, subCategoryName,
                url, pDescription, eStockAvailable, eRegularPrice, eSellingPrice, eDiscount, pActualPrice,
                addingDate, addingTime);

        mDatabaseRef.setValue(productInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProductActivity.this, "Successfully Edited product", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditProductActivity.this, ProductActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        mCategoryList.add(new Category("Daily Drug"));

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

    private void inItSubCategoryList4() {
        mSubCategoryList4.add(new SubCategory("Select Sub Category"));
        mSubCategoryList4.add(new SubCategory("Normal"));
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (mCategoryList.get(i).toString().equalsIgnoreCase(myString)){
                return i;

            }
            //Log.d(TAG, "getIndex: "+ mUnitList.get(i).toString());
        }

        return 0;
    }
   /* private int getIndexS1(Spinner spinner1, String myString1){
        for (int j=0;j<spinner1.getCount();j++){
            if (mSubCategoryList1.get(j).toString().equalsIgnoreCase(myString1)){
                return j;

            }
            Log.d(TAG, "getIndex1: "+ mSubCategoryList1.get(j).toString());
        }

        return 0;
    }
    private int getIndexS2(Spinner spinner2, String myString2){
        for (int m=0;m<spinner2.getCount();m++){
            if (mSubCategoryList2.get(m).toString().equalsIgnoreCase(myString2)){
                return m;

            }
            Log.d(TAG, "getIndex2: "+ mSubCategoryList2.get(m).toString());
        }

        return 0;
    }
    private int getIndexS3(Spinner spinner3, String myString3){
        for (int k=0;k<spinner3.getCount();k++){
            if (mSubCategoryList3.get(k).toString().equalsIgnoreCase(myString3)){
                return k;

            }
            Log.d(TAG, "getIndex3: "+ mSubCategoryList3.get(k).toString());
        }

        return 0;
    }
    private int getIndexS4(Spinner spinner4, String myString4){
        for (int l=0;l<spinner4.getCount();l++){
            if (mSubCategoryList4.get(l).toString().equalsIgnoreCase(myString4)){
                return l;

            }
            Log.d(TAG, "getIndex: "+ mSubCategoryList4.get(l).toString());
        }

        return 0;
    }
*/

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

        categorySpinner = findViewById(R.id.categorySpinner);
        subCategorySpinner = findViewById(R.id.subCategorySpinner);
        mCategoryAdapter = new CategoryAdapter(EditProductActivity.this, mCategoryList);
        categorySpinner.setAdapter(mCategoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedCategory = (Category) parent.getItemAtPosition(position);

                categoryName = clickedCategory.getCategoryName();
                if (position > 0) {
                    subCategorySpinner.setVisibility(View.VISIBLE);
                    Toast.makeText(EditProductActivity.this, categoryName + " is selected !", Toast.LENGTH_SHORT).show();
                    if (categoryName.equals("Kids & Mom's")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(EditProductActivity.this, mSubCategoryList1);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                //Toast.makeText(EditProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else if (categoryName.equals("Medical Accessories")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(EditProductActivity.this, mSubCategoryList2);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                //Toast.makeText(EditProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else if (categoryName.equals("Nutrition")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(EditProductActivity.this, mSubCategoryList3);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                //Toast.makeText(EditProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else if (categoryName.equals("Daily Drug")) {
                        mSubCategoryAdapter = new SubcategoryAdapter(EditProductActivity.this, mSubCategoryList4);
                        subCategorySpinner.setAdapter(mSubCategoryAdapter);
                        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SubCategory clickedSubCategory = (SubCategory) parent.getItemAtPosition(position);

                                subCategoryName = clickedSubCategory.getSubCategoryName();

                                //Toast.makeText(EditProductActivity.this, subCategoryName + " is selected !", Toast.LENGTH_SHORT).show();
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