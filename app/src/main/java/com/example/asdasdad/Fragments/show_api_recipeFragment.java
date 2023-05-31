package com.example.asdasdad.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.asdasdad.Adapters.MyAdapterApi;
import com.example.asdasdad.Models.ApiObject;
import com.example.asdasdad.Models.DataClass;
import com.example.asdasdad.MyAdapter;
import com.example.asdasdad.R;
import com.example.asdasdad.Services.DataService;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link show_api_recipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class show_api_recipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public show_api_recipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment show_api_recipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static show_api_recipeFragment newInstance(String param1, String param2) {
        show_api_recipeFragment fragment = new show_api_recipeFragment();
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


    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<ApiObject> dataList;
    ValueEventListener eventListener;
    String url;
    MyAdapterApi adapterApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewF = inflater.inflate(R.layout.fragment_show_api_recipe,container,false);



        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Log.d("result" , "yes");
                Navigation.findNavController(viewF).navigate(R.id.action_show_api_recipeFragment_to_show_all_recipes_fregment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


        recyclerView = viewF.findViewById(R.id.recyclerViewApi);
        searchView = viewF.findViewById(R.id.searchApi);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        DataService dataService = new DataService();



        String str = "bcdefghijklmnopqrstuvwxyz";
        dataList = dataService.getRecipesFromApi('a');
        for (char ch : str.toCharArray()) {
            dataList.addAll(dataService.getRecipesFromApi(ch));
        }




        adapterApi =new MyAdapterApi(getContext(),dataList, viewF, getParentFragmentManager());
        recyclerView.setAdapter(adapterApi);

        adapterApi.notifyDataSetChanged();
        dialog.dismiss();


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

        return viewF;
    }

    public void searchList(String text){
        ArrayList<ApiObject> searchList = new ArrayList<>();
        for (ApiObject dataClass:dataList){
            if(dataClass.getDataName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapterApi.searchDataList(searchList);
    }
}