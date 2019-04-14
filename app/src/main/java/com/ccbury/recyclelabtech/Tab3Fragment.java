//add to package
package com.ccbury.recyclelabtech;

//import required dependencies
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


//begin class
public class Tab3Fragment extends Fragment  implements View.OnClickListener{
    //Declare required variables
    Button myButton;
    View myView;
    private static final int CAMERA_REQUEST = 1888;
    FirebaseVisionImage image;

    DatabaseReference mProductDatabase;
    TextView product_name, product_description, Text_View;
    ImageView product_image;

    String searchText;

    //begin onCreateView method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_three, container, false);
        myButton = myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);

        product_name = myView.findViewById(R.id.name_text);
        product_description = myView.findViewById(R.id.description_text);
        product_image = myView.findViewById(R.id.product_image);
        product_image.setVisibility(View.INVISIBLE);
        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");

        return myView;
    }//End onCreateView

    //Begin scanNow method. opens camera to take photo
    public void scanNow(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }//End scannow

    //Add onClick for button to open camera. Clears previous search if any to prevent errors. Calls scanNow
    public void onClick(View v) {
        product_name.setText("");
        product_description.setText("");
        product_image.setVisibility(View.INVISIBLE);
        scanNow(v);
    }//End onClick

    //Create method to run when camera activity is completed. saves camera input as bitmap, sends bitmap to Firebase for processing
    //Gets highest confidence result. Sets this result to searchText variable. Used later to query database.
    //Calls setResult to query and pull information from database
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       if(resultCode == RESULT_OK && data != null) {
           Bitmap photo = (Bitmap) data.getExtras().get("data");

           Bitmap photo2 = photo.copy(Bitmap.Config.ARGB_8888, true);
           image = FirebaseVisionImage.fromBitmap(photo2);

           FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();

           labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
               @Override
               public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                   ArrayList<String> textArray = new ArrayList<String>();
                   ArrayList<Float> confidenceArray = new ArrayList<Float>();
                   ArrayList<String> confidenceStrArray = new ArrayList<String>();
                   int i = 0;
                   for (FirebaseVisionImageLabel label : labels) {
                       textArray.add(label.getText());
                       confidenceArray.add(label.getConfidence());
                       confidenceStrArray.add(Float.toString(confidenceArray.get(i)));
                       i++;
                   }
                   searchText = textArray.get(0);
                   setResult();

               }
           });
       }else{
           product_name.setText("Error No Photo Received....");
           product_name.setAllCaps(true);
           product_name.setTextSize(30);
       }
    }//End OnActivity result method

    //Create setResult method. Queries database for object previously detected in onActivityResult
    public void setResult() {
        //Queries database
        mProductDatabase.orderByChild("name").equalTo(searchText).addChildEventListener(new ChildEventListener() {
            //Add onChildAdded method. gets entry for query and creates object to store information in. Using products object.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                products prod = dataSnapshot.getValue(products.class);
                product_name.setAllCaps(false);
                product_name.setTextSize(14);
                product_name.setText("Name: "+prod.getName());
                product_description.setText("Description: "+prod.getDescription());
                product_image.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(prod.getImage()).into(product_image);
            }//End onChildAdded

            //Unused methods for adding to database ect. Required for query to function.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
            //End unused methods
        });//Ends query

        //If statement to remove information if query is unsuccessful
        if(product_name.getText().toString().matches("")){
            product_name.setText(searchText);
            product_name.setAllCaps(true);
            product_name.setTextSize(30);
        }//End if statement

    }//End setResult

}//End class
