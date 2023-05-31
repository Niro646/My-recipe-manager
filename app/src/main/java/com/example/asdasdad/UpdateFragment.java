package com.example.asdasdad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asdasdad.Models.DataClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
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

    ImageView updateImage;
    Button updateButton;
    EditText updateName, updateIngredients, updateDescription;
    TextView updateDifficulty;
    NumberPicker updateHour, updatedMint;
    String name, ingredients, description, difficulty, preparationTime;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    View viewF;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewF = inflater.inflate(R.layout.fragment_update,container,false);

        updateImage = viewF.findViewById(R.id.updateImage);
        updateButton = viewF.findViewById(R.id.updateButton);
        updateName = viewF.findViewById(R.id.updateName);
        updateIngredients = viewF.findViewById(R.id.updateIngredients);
        updateDescription = viewF.findViewById(R.id.updateDescription);
        updateDifficulty = viewF.findViewById(R.id.updateDifficulty);
        updateHour = viewF.findViewById(R.id.updateHour);
        updatedMint = viewF.findViewById(R.id.updateMint);
        //updateDescription = viewF.findViewById(R.id.update);


        updateHour.setMaxValue(12);
        updateHour.setMinValue(0);
        updateHour.setValue(0);
        updateHour.setTextSize(50);
        updateHour.setTextColor(R.color.lavender);

        updatedMint.setMaxValue(59);
        updatedMint.setMinValue(0);
        updatedMint.setValue(0);
        updatedMint.setTextSize(50);
        updatedMint.setTextColor(R.color.lavender);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Log.d("result" , "yes");
                Navigation.findNavController(viewF).navigate(R.id.action_updateFragment_to_show_all_recipes_fregment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


        updateDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(viewF.getContext(), updateDifficulty);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_difficult_level, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        updateDifficulty.setText(menuItem.getTitle());
                        // Toast message on menu item clicked
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        }else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        getParentFragmentManager().setFragmentResultListener("updateRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                updateName.setText(result.getString("recipeName"));
                updateIngredients.setText(result.getString("recipeIngredients"));
                updateDescription.setText(result.getString("recipeInstructions"));
                //detailPreparation.setText(result.getString("recipePreparationTime"));
                updateDifficulty.setText(result.getString("recipeDifficulty"));
                key = result.getString("key");
                imageUrl = result.getString("recipeImage");
                oldImageURL = result.getString("recipeImage");
                Glide.with(getContext()).load(result.getString("recipeImage")).into(updateImage);
                updateImage.setTag(result.getString("recipeImage"));
//                Glide.with(getContext()).load(result.getString("recipeImage")).into(updateImage);

                databaseReference = FirebaseDatabase.getInstance().getReference("recipe").child(key);

            }
        });

        //name = updateName.getText().toString();
//        databaseReference = FirebaseDatabase.getInstance().getReference("recipe").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                //I
            }
        });



        return viewF;
    }

    public void  saveData(){

        if(uri != null){
            storageReference = FirebaseStorage.getInstance().getReference("Android Images").child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog =builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }

    //  name, ingredients, description, difficulty
    public  void updateData(){

        name = updateName.getText().toString();
        ingredients = updateIngredients.getText().toString();
        description = updateDescription.getText().toString();
        difficulty = updateDifficulty.getText().toString();
        preparationTime = updateHour.getValue() + "h & " + updatedMint.getValue() + "m";

        DataClass dataClass = new DataClass(imageUrl,name,ingredients,description,difficulty,preparationTime);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (uri != null) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                    }
                    Toast.makeText(getContext(),"Updated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(viewF).navigate(R.id.action_updateFragment_to_show_all_recipes_fregment);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}











