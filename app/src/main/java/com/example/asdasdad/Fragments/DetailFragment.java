package com.example.asdasdad.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asdasdad.Models.DataClass;
import com.example.asdasdad.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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


    ImageView detailImage;
    TextView detailName, detailIngredients, detailInstructions;
    TextView detailPreparation, detailDifficulty;
    FloatingActionMenu menuBtn;
    FloatingActionButton deleteButton, editButton, favoriteButton;
    String key = "";
    String imageUrl = "";
    Boolean currentRecipeIsFavorite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewF = inflater.inflate(R.layout.fragment_detail,container,false);

        detailName = viewF.findViewById(R.id.detaile_name);
        detailIngredients = viewF.findViewById(R.id.detail_ingredients);
        detailInstructions = viewF.findViewById(R.id.detail_instructions);
        detailPreparation = viewF.findViewById(R.id.detail_preparation_time);
        detailDifficulty = viewF.findViewById(R.id.detail_difficulty_level);
        detailImage = viewF.findViewById(R.id.detail_image);

        menuBtn = viewF.findViewById(R.id.menuButton);
        deleteButton = viewF.findViewById(R.id.deleteButton);
        editButton = viewF.findViewById(R.id.editButton);
        favoriteButton = viewF.findViewById(R.id.favoriteButton);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);





        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Log.d("result" , "yes");
                Navigation.findNavController(viewF).navigate(R.id.action_detailFragment_to_show_all_recipes_fregment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);



        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                detailName.setText(result.getString("recipeName"));
                detailIngredients.setText(result.getString("recipeIngredients"));
                detailInstructions.setText(result.getString("recipeInstructions"));
                detailPreparation.setText(result.getString("recipePreparationTime"));
                detailDifficulty.setText(result.getString("recipeDifficulty"));
                key = result.getString("key");
                imageUrl = result.getString("recipeImage");
                Glide.with(getContext()).load(result.getString("recipeImage")).into(detailImage);

                currentRecipeIsFavorite = isFavorite(detailName.getText().toString(),sharedPref);
                if(currentRecipeIsFavorite){
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_favorite_24));
                }
            }
        });



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("recipe");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(viewF).navigate(R.id.action_detailFragment_to_show_all_recipes_fregment);
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("recipeImage", imageUrl);
                args.putString("recipeName", detailName.getText().toString());
                args.putString("recipeIngredients", detailIngredients.getText().toString());
                args.putString("recipeInstructions", detailInstructions.getText().toString());
                args.putString("recipeDifficulty", detailDifficulty.getText().toString());
                args.putString("recipePreparationTime", detailPreparation.getText().toString());
                args.putString("key", key);
                //args.putString("Language", dataList.get(holder.getAdapterPosition()).getDataL);


                getParentFragmentManager().setFragmentResult("updateRequestKey", args);

                //fragmentManager.setArguments(args);

                Navigation.findNavController(viewF).navigate(R.id.action_detailFragment_to_updateFragment);
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBtn.close(true);
                SharedPreferences.Editor editor = sharedPref.edit();
                Map<String, ?> allEntries = sharedPref.getAll();

                if (currentRecipeIsFavorite){
                    currentRecipeIsFavorite = false;
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_favorite_border_red_24));

                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        entry.getValue();
                        try {
                            JSONObject jsonObj = new JSONObject(entry.getValue().toString());
                            if (jsonObj.getString("recipeName").equals(detailName.getText().toString())){
                                editor.remove(entry.getKey());
                                editor.apply();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else {
                    currentRecipeIsFavorite = true;
                    favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_favorite_24));

                    int allEntriesSize = allEntries.size();

                    String jasonString = "{\"recipeName\":" + "\"" + detailName.getText().toString() + "\"" +
                            ",\"recipeImage\":" + "\"" + imageUrl + "\"" +
                            ",\"recipeIngredients\":" + "\"" + detailIngredients.getText().toString() + "\"" +
                            ",\"recipeInstructions\":" + "\"" + detailInstructions.getText().toString() + "\"" +
                            ",\"recipeDifficulty\":" + "\"" + detailDifficulty.getText().toString() + "\"" +
                            ",\"recipePreparationTime\":" + "\"" + detailPreparation.getText().toString() + "\"" + "}";

                    editor.putString(Integer.toString(allEntriesSize + 1), jasonString);
                    editor.apply();
                }
            }
        });

        return viewF;
    }

    public boolean isFavorite(String recipeName, @NonNull SharedPreferences sharedPref){
        Map<String, ?> allEntries = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            entry.getValue();
            try {
                JSONObject jsonObj = new JSONObject(entry.getValue().toString());
                if (jsonObj.getString("recipeName").equals(recipeName)){
                    return true;
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "screan click", Toast.LENGTH_SHORT).show();

    }
}