package com.example.asdasdad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;



import com.bumptech.glide.Glide;
import com.example.asdasdad.Models.DataClass;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;
    View viewF;
    FragmentManager fragmentManager;

    public MyAdapter(Context context, List<DataClass> dataList, View viewF,FragmentManager fragmentManager) {
        this.context = context;
        this.dataList = dataList;
        this.viewF = viewF;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recipe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recipeImage);
        holder.recipeName.setText(dataList.get(position).getDataName());
        holder.recipeDescription.setText(dataList.get(position).getDataDescription());
        holder.recipeDifficulty.setText(dataList.get(position).getDataDifficultyLevel());
        holder.recipePreparationTime.setText(dataList.get(position).getDataPreparationTime());



        holder.recipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("recipeImage", dataList.get(holder.getAdapterPosition()).getDataImage());
                args.putString("recipeName", dataList.get(holder.getAdapterPosition()).getDataName());
                args.putString("recipeIngredients", dataList.get(holder.getAdapterPosition()).getDataIngredients());
                args.putString("recipeInstructions", dataList.get(holder.getAdapterPosition()).getDataDescription());
                args.putString("recipeDifficulty", dataList.get(holder.getAdapterPosition()).getDataDifficultyLevel());
                args.putString("recipePreparationTime", dataList.get(holder.getAdapterPosition()).getDataPreparationTime());
                args.putString("key", dataList.get(holder.getAdapterPosition()).getKey());


                fragmentManager.setFragmentResult("requestKey", args);

                //fragmentManager.setArguments(args);

                Navigation.findNavController(viewF).navigate(R.id.action_show_all_recipes_fregment_to_detailFragment);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recipeImage;
    TextView recipeName, recipeIngredients, recipeDifficulty, recipeDescription, recipePreparationTime;

    CardView recipeCard;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeImage = itemView.findViewById(R.id.recipe_image);
        recipeDifficulty = itemView.findViewById(R.id.recipe_difficulty_level);
        recipeDescription = itemView.findViewById(R.id.recipe_brief_description);
        recipeName = itemView.findViewById(R.id.recipe_name);
        recipePreparationTime = itemView.findViewById(R.id.recipe_preparation_time);
        recipeCard = itemView.findViewById(R.id.recipe_card);

    }
}