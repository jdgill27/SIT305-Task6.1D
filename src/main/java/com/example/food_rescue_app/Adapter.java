package com.example.food_rescue_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    List<Food_Item> list;

    public Adapter(Context context, List<Food_Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_list_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
        Food_Item food_item = list.get(position);
        holder.name.setText(food_item.title);
        holder.details.setText(food_item.desc);
        holder.qty.setText(food_item.quantity);
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        //holder.image.setImageResource(Integer.parseInt(food_item.image));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,details,qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_display);
            name = itemView.findViewById(R.id.title_display);
            details = itemView.findViewById(R.id.desc_display);
            qty = itemView.findViewById(R.id.qty_display);

        }
    }
}
