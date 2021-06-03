package com.astronist.personalnurseadmin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.personalnurseadmin.Model.ProductInfo;
import com.astronist.personalnurseadmin.ProductActivity;
import com.astronist.personalnurseadmin.ProductDetailsActivity;
import com.astronist.personalnurseadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {
    private ArrayList<ProductInfo> productInfoArrayList;
    private Context context;

    public ProductAdapter(ArrayList<ProductInfo> productInfoArrayList, Context context) {
        this.productInfoArrayList = productInfoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.viewHolder holder, int position) {
        ProductInfo productInfo = productInfoArrayList.get(position);
        String pTitle = productInfo.getTitle();
        int pStock = productInfo.getStockAvailable();
        String proStck = String.valueOf(pStock);
        double pActualPrice = productInfo.getActualSellingPrice();
        String proActualPrice = String.valueOf(pActualPrice);

        holder.title.setText(pTitle);
        holder.stock.setText(proStck);
        holder.actualPrice.setText(proActualPrice);
        Picasso.get().load(productInfo.getImageUrl()).into(holder.proImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product = new Intent(context, ProductDetailsActivity.class);
                product.putExtra("productData", productInfo);
                context.startActivity(product);

                //((Activity)context).finish();
                //Toast.makeText(context, "Please keep patient!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView title, stock, actualPrice;
        private ImageView proImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.proTitle);
            stock = itemView.findViewById(R.id.proStock);
            actualPrice = itemView.findViewById(R.id.proActualPrice);
            proImage = itemView.findViewById(R.id.previewImage);
        }
    }
}
