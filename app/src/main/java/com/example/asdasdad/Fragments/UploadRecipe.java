package com.example.asdasdad.Fragments;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asdasdad.Models.DataClass;
import com.example.asdasdad.R;
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
    ImageView uploadImage;
    EditText uploadName, uploadIngredients, uploadDescription;
    TextView uploadDifficulty;
    NumberPicker uploadHour, uploadMint;
    RadioButton uploadVegan, uploadVegetarian;
    Button saveButton;
    String imageURL;
    Uri uri;
    View viewF;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewF = inflater.inflate(R.layout.fragment_upload_recipe,container,false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Log.d("result" , "yes");
                Navigation.findNavController(viewF).navigate(R.id.action_add_recipe_to_show_all_recipes_fregment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


        uploadImage = viewF.findViewById(R.id.uploadImage);
        uploadName = viewF.findViewById(R.id.uploadName);
        uploadIngredients = viewF.findViewById(R.id.uploadIngredients);
        uploadDescription =viewF.findViewById(R.id.uploadDescription);
        uploadDifficulty = viewF.findViewById(R.id.uploadDifficulty);
        uploadVegan = viewF.findViewById(R.id.uploadIsVegan);
        uploadVegetarian = viewF.findViewById(R.id.uploadIsVegetarian);
        saveButton = viewF.findViewById(R.id.saveButton);

        uploadHour = viewF.findViewById(R.id.uploadHour);
        uploadMint = viewF.findViewById(R.id.uploadMint);

        uploadHour.setMaxValue(12);
        uploadHour.setMinValue(0);
        uploadHour.setValue(0);
        uploadHour.setTextSize(50);
        uploadHour.setTextColor(R.color.lavender);

        uploadMint.setMaxValue(59);
        uploadMint.setMinValue(0);
        uploadMint.setValue(0);
        uploadHour.setTextSize(50);
        uploadMint.setTextColor(R.color.lavender);

        uploadDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(viewF.getContext(), uploadDifficulty);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_difficult_level, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        uploadDifficulty.setText(menuItem.getTitle());
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
                            uploadImage.setImageURI(uri);
                        }else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {

           // Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  saveData();
              }
          });

        return viewF;
    }

    public void saveData(){
        if(uri != null && !uploadName.getText().toString().equals("") && !uploadIngredients.getText().toString().equals("")
                && !uploadDescription.getText().toString().equals("") && !uploadDifficulty.getText().toString().equals("")){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog =builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {//זה לא נכנס לכאן למרות שבדיבאג הוא מגיע לשורה הזו
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                    Navigation.findNavController(viewF).navigate(R.id.action_add_recipe_to_show_all_recipes_fregment);
                }
            }).addOnFailureListener(new OnFailureListener() {                                                  //וגם לא נכנס לכאן
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();       //ולא לכאן אז אני גם לא יכול לראות את הדעת שגיעה

                }
            });
        }
        else {
            Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadData(){

        String name = uploadName.getText().toString();
        String ingredients = uploadIngredients.getText().toString();
        String description = uploadDescription.getText().toString();
        String difficulty = uploadDifficulty.getText().toString();
        String preparationTime = uploadHour.getValue() + "h & " + uploadMint.getValue() + "m";
        Boolean vegan = uploadVegan.isChecked();
        Boolean vegetarian = uploadVegetarian.isChecked();

        DataClass dataClass = new DataClass(imageURL,name,ingredients,description,difficulty,preparationTime);

        FirebaseDatabase.getInstance().getReference("recipe").child(name)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Save", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();                    }
                });
    }
}
























