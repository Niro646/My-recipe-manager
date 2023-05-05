package com.example.asdasdad;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link show_all_recipes_fregment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class show_all_recipes_fregment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public show_all_recipes_fregment() {
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
    public static show_all_recipes_fregment newInstance(String param1, String param2) {
        show_all_recipes_fregment fragment = new show_all_recipes_fregment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewF = inflater.inflate(R.layout.fragment_show_all_recipes_fregment,container,false);

        Button equalBtn = viewF.findViewById(R.id.add_recipe_btn);

        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Navigation.findNavController(viewF).navigate(R.id.action_show_all_recipes_fregment_to_add_recipe);

            }
        });
        return viewF;
    }
}