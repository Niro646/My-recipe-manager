package com.example.asdasdad;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadRecipe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadRecipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_recipe.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadRecipe newInstance(String param1, String param2) {
        UploadRecipe fragment = new UploadRecipe();
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

    TextView recipeDifficultyTextView;
    NumberPicker hourPicker, mintPicker;


    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewF = inflater.inflate(R.layout.fragment_upload_recipe,container,false);

        recipeDifficultyTextView = viewF.findViewById(R.id.upload_recipe_difficulty_level);

        hourPicker = viewF.findViewById(R.id.hour_picker);
        mintPicker = viewF.findViewById(R.id.mint_picker);

        hourPicker.setMaxValue(12);
        hourPicker.setMinValue(0);
        hourPicker.setValue(0);
        hourPicker.setTextSize(50);
        hourPicker.setTextColor(R.color.lavender);

        mintPicker.setMaxValue(59);
        mintPicker.setMinValue(0);
        mintPicker.setValue(0);
        hourPicker.setTextSize(50);
        mintPicker.setTextColor(R.color.lavender);




        recipeDifficultyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(viewF.getContext(), recipeDifficultyTextView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_difficult_level, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        recipeDifficultyTextView.setText(menuItem.getTitle());
                        // Toast message on menu item clicked
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();

            }
        });
        return viewF;
    }
}