package com.example.asdasdad.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asdasdad.Models.ApiObject;
import com.example.asdasdad.Models.DataClass;
import com.example.asdasdad.R;

import java.util.ArrayList;

public class MyAdapterFavorite extends RecyclerView.Adapter<MyViewHolderFavorite>{
    private Context context;
    private ArrayList<DataClass> dataList;

    View viewF;
    FragmentManager fragmentManager;

    public MyAdapterFavorite(Context context, ArrayList<DataClass> dataList, View viewF, FragmentManager fragmentManager) {
        this.context = context;
        this.dataList = dataList;
        this.viewF = viewF;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recipe, parent, false);
        return new MyViewHolderFavorite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFavorite holder, int position) {
        DataClass currentRecipe = dataList.get(position);

        holder.recipeName.setText(currentRecipe.getDataName());
        holder.recipeDifficulty.setText(currentRecipe.getDataDifficultyLevel());
        holder.recipeDescription.setText(currentRecipe.getDataDescription());
        holder.recipePreparationTime.setText(currentRecipe.getDataPreparationTime());
        Glide.with(context).load(currentRecipe.getDataImage()).into(holder.recipeImage);

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
                //args.putString("Language", dataList.get(holder.getAdapterPosition()).getDataL);


                fragmentManager.setFragmentResult("requestKey", args);
                Navigation.findNavController(viewF).navigate(R.id.action_show_all_recipes_fregment_to_detailFragment);

            }
        });
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolderFavorite extends RecyclerView.ViewHolder{
    ImageView recipeImage;
    TextView recipeName, recipeIngredients, recipeDifficulty, recipeDescription, recipePreparationTime;

    CardView recipeCard;

    public MyViewHolderFavorite(@NonNull View itemView) {
        super(itemView);

        recipeImage = itemView.findViewById(R.id.recipe_image);
        recipeDifficulty = itemView.findViewById(R.id.recipe_difficulty_level);
        recipeDescription = itemView.findViewById(R.id.recipe_brief_description);
        recipeName = itemView.findViewById(R.id.recipe_name);
        recipePreparationTime = itemView.findViewById(R.id.recipe_preparation_time);
        recipeCard = itemView.findViewById(R.id.recipe_card);
    }
}
