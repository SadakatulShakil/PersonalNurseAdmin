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
import com.astronist.personalnurseadmin.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> categoryArrayList;
    private Context context;

    public CategoryAdapter(@NonNull Context context, ArrayList<Category> categoryArrayList) {
        super(context, 0, categoryArrayList);
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

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        Category category = getItem(position);

        if (position == 0) {
            categoryName.setTextColor(Color.GRAY);
        } else {
            categoryName.setTextColor(Color.BLACK);

        }
        if (category != null) {
            categoryName.setText(category.getCategoryName());
        }

        return convertView;
    }
}
