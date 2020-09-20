package com.example.chat.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.splash.SplashScreen;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    //String for save data
    private final static String MY_PREFS = "saves";


    //Element in Activity
    ImageView profile_user_image, profile_change_name, profile_change_about;
    TextView profile_name, profile_about;
    Button log_out;
    FloatingActionButton change_image_button;

    //Firebase part of variable
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    private StorageReference mStorageRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //First etap for using firebase
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        firestore = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("userImage/");



        //Create the link between activity and java class.
        profile_user_image = (ImageView) root.findViewById(R.id.profile_user_image);

        profile_change_name = (ImageView) root.findViewById(R.id.profile_change_name);
        profile_change_about = (ImageView) root.findViewById(R.id.profile_change_about);

        profile_name = (TextView) root.findViewById(R.id.profile_name_user);
        profile_about = (TextView) root.findViewById(R.id.profile_about_user);

        log_out = (Button)  root.findViewById(R.id.go_out_button);

        change_image_button = (FloatingActionButton) root.findViewById(R.id.float_button_change_image);

        //Change the image in Profile
        change_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermision();
            }
        });

        //Change the Name in Profile
        profile_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });

        //Change the About in Profile
        profile_change_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAbout();
            }
        });

        //Log out
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        //Input in Activity the save data
        loadDataFromFirebase();
        
        return root;
    }

    /////////////////////////// Start Load Data from Firebase Section ///////////////////////////////////////////
    private void loadDataFromFirebase() {
        final DocumentReference documentReference = firestore.collection("users").document(userID);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String name = value.getString("name");
                String about = value.getString("about");
                String imageUrl = value.getString("imageURL");

                setDataInProfile(name,about,imageUrl);


            }
        });
    }

    private void setDataInProfile(String name, String about, String imageUrl) {
        profile_name.setText(name);
        profile_about.setText(about);

        if(!"#".equals(imageUrl)) Picasso.get().load(imageUrl).into(profile_user_image);

    }

    ////////// End Load Data from Firebase Section ////////////////////////////

    ////////// Start Image Section ////////////////////////////////////
    private void checkPermision() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
                ///Ad the log of error
            }
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        //Verification of permission, if is permission is deny, previous function call the request is function requestForSpecificPermission
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            //If result is true, is start to pick image
            pickImage();
        } else {
            return false;
        }
        return true;
    }

    //Function for request the missing permission
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    //Method who start the picking and call the other Activity
    //special for Image pick
    private void pickImage() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(getContext(), this);
    }

    //This method is using for got an answer or error
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //If image is pick successfully is transfer Uri of image in special method
                addImageInFirebaseStorege(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Toast for error
                Toast.makeText(getContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //In this method image is upload in Firebase Storage
    private void addImageInFirebaseStorege(Uri uri) {

        //Get the extension of pick image
        String extension = uri.toString().substring(uri.toString().lastIndexOf("."));

        //Create special name for image
        //Name is unique for each user
        final String user_image_profile_file_name = mAuth.getUid() + "_userImage" + extension;

        //Create link in Firebase Storage and add image
        Toast.makeText(getContext(), "Start update " ,Toast.LENGTH_LONG).show();

        mStorageRef.child(user_image_profile_file_name).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //get the Url of image in Firebase Storage
                mStorageRef.child(user_image_profile_file_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        //Update in Firebase CLoud the Url of user Image
                        updateImageUrlInFirestore(uri);
                    }
                });

            }
        });

    }

    //Method for update image URL in Firebase Cloud
    private void updateImageUrlInFirestore(Uri downloadUrl) {

        DocumentReference doReference ;

        doReference = firestore.collection("users").document(userID);
        doReference.update("imageURL", downloadUrl.toString());

        Toast.makeText(getContext(), "Success update " ,Toast.LENGTH_LONG).show();

    }
/////////////////////////////////End Image Section ////////////////////////////////////////////



//////////////////// Start About Section /////////////////////////////////////////
    private void changeAbout() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
            alertDialog.setTitle("Change About");
            alertDialog.setMessage("new about");

            final EditText input = new EditText(this.getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);

            alertDialog.setPositiveButton("Change",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String new_about = input.getText().toString();

                            changeAboutInFirebase(new_about);


                        }
                    });

            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
    }

    private void changeAboutInFirebase(String new_about) {
        DocumentReference doReferens;
        doReferens = firestore.collection("users").document(userID);
        doReferens.update("about",new_about);
    }

    //////////////////////End About Section //////////////////////////////

/////////////////////// Start Name Section ///////////////////////
    private void changeName() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
        alertDialog.setTitle("Change Name");
        alertDialog.setMessage("new name");

        final EditText input = new EditText(this.getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Change",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String new_name = input.getText().toString();
                        changeNameInFirebase(new_name);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void changeNameInFirebase(String new_name) {

        DocumentReference doReferens;
        doReferens = firestore.collection("users").document(userID);
        doReferens.update("name",new_name);

    }
    ///////////////////// End Name Section //////////////////////////////////////////


    //Log Out method
    private void logOut() {
        //Change data in preferences
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean("verification",false);
        editor.apply();

        //go to start Activity
        startActivity(new Intent(this.getActivity() , SplashScreen.class));
    }
}