package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astronist.personalnurseadmin.Model.Category;
import com.astronist.personalnurseadmin.Model.SubCategory;
import com.astronist.personalnurseadmin.R;

import java.util.ArrayList;

public class SubcategoryAdapter extends ArrayAdapter<SubCategory> {
    private ArrayList<SubCategory> subCategoryArrayList;
    private Context context;

    public SubcategoryAdapter(@NonNull Context context, ArrayList<SubCategory> subCategoryArrayList) {
        super(context, 0, subCategoryArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public boolean isEnabled(int position) {
        return position == 0 ? false : true;
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_name, parent, false);

        }

        TextView subCategoryName = convertView.findViewById(R.id.categoryName);
        SubCategory subCategory = getItem(position);

        if (position == 0) {
            subCategoryName.setTextColor(Color.GRAY);
        } else {
            subCategoryName.setTextColor(Color.BLACK);

        }
        if (subCategory != null) {
            subCategoryName.setText(subCategory.getSubCategoryName());
        }

        return convertView;
    }
}
