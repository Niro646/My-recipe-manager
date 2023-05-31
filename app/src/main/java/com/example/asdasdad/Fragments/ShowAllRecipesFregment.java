package com.example.asdasdad.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.asdasdad.Adapters.MyAdapterFavorite;
import com.example.asdasdad.Models.ApiObject;
import com.example.asdasdad.Models.DataClass;
import com.example.asdasdad.MyAdapter;
import com.example.asdasdad.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllRecipesFregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllRecipesFregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment turgetFragment;


    public ShowAllRecipesFregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment show_all_recipes_fregment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowAllRecipesFregment newInstance(String param1, String param2) {
        ShowAllRecipesFregment fragment = new ShowAllRecipesFregment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    FloatingActionButton addRecipeBtn;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter adapter;
    MyAdapterFavorite favoriteAdapter;

    ImageButton apiBtn, favoriteBtn;
    Boolean favoriteBtnIsPressed = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewF = inflater.inflate(R.layout.fragment_show_all_recipes,container,false);

        addRecipeBtn = viewF.findViewById(R.id.add_recipe_btn);
        recyclerView = viewF.findViewById(R.id.recyclerView);
        searchView = viewF.findViewById(R.id.search);
        searchView.clearFocus();

        apiBtn =viewF.findViewById(R.id.api_btn);
        favoriteBtn =viewF.findViewById(R.id.favorite);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter =new MyAdapter(getContext(),dataList, viewF, getParentFragmentManager());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("recipe");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass dataClass =itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });




        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Navigation.findNavController(viewF).navigate(R.id.action_show_all_recipes_fregment_to_add_recipe);

            }
        });

        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Navigation.findNavController(viewF).navigate(R.id.action_show_all_recipes_fregment_to_show_api_recipeFragment);

            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteBtnIsPressed){
                    favoriteBtnIsPressed = false;
                    favoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_favorite_border_24));
                    recyclerView.setAdapter(adapter);
                }
                else {
                    favoriteBtnIsPressed =true;
                    favoriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_home_24));


                    ArrayList<DataClass> favoriteDataList = new ArrayList<>();

                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

                    // Get all key-value pairs from SharedPreferences
                    Map<String, ?> allEntries = sharedPref.getAll();

                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        entry.getValue();
                        try {
                            JSONObject jsonObj = new JSONObject(entry.getValue().toString());
                            String recipeName = jsonObj.getString("recipeName");
                            String recipeImage = jsonObj.getString("recipeImage");
                            String recipeIngredients = jsonObj.getString("recipeIngredients");
                            String recipeInstructions = jsonObj.getString("recipeInstructions");
                            String recipeDifficulty = jsonObj.getString("recipeDifficulty");
                            String recipePreparationTime = jsonObj.getString("recipePreparationTime");

                            DataClass dataClass = new DataClass(recipeImage,recipeName,recipeIngredients,recipeInstructions,recipeDifficulty,recipePreparationTime);
                            favoriteDataList.add(dataClass);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    favoriteAdapter =new MyAdapterFavorite(getContext(),favoriteDataList, viewF, getParentFragmentManager());
                    recyclerView.setAdapter(favoriteAdapter);
                }
            }
        });

        return viewF;
    }

    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass:dataList){
            if(dataClass.getDataIngredients().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        if (favoriteBtnIsPressed){
            favoriteAdapter.searchDataList(searchList);
        }
        else {
            adapter.searchDataList(searchList);
        }

    }

}