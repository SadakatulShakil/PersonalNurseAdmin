package com.astronist.personalnurseadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astronist.personalnurseadmin.Model.Slider;
import com.astronist.personalnurseadmin.R;
import com.astronist.personalnurseadmin.SliderUpdateActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.viewHolder> {
    private ArrayList<Slider> sliderImageArrayList;
    private Context context;

    public SliderAdapter(ArrayList<Slider> sliderImageArrayList, Context context) {
        this.sliderImageArrayList = sliderImageArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_view, parent, false);
        return new SliderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.viewHolder holder, int position) {
        Slider slider = sliderImageArrayList.get(position);
        Picasso.get().load(slider.getSliderImageUrl()).into(holder.sliderImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SliderUpdateActivity.class);
                intent.putExtra("sliderInfo", slider);
                intent.putExtra("position", String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderImageArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView sliderImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.sliderImage);
        }
    }
}
