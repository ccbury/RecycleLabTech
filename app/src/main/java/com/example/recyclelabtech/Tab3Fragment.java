//add to package
package com.example.recyclelabtech;

//import required dependencies
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

//begin class
public class Tab3Fragment extends Fragment  implements View.OnClickListener{
    Button myButton;
    View myView;
    private static final int CAMERA_REQUEST = 1888;
    static TextView formatTxt, contentTxt;
    FirebaseVisionImage image;
    String text;
    String confidencestr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_three, container, false);
        myButton = (Button) myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);
        formatTxt = (TextView) myView.findViewById(R.id.scan_format);
        contentTxt = (TextView) myView.findViewById(R.id.scan_content);

        return myView;
    }

    public void scanNow(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }//Scan now used to open camera

    public void onClick(View v) {
        scanNow(v);
    }//Add onClick for button to open camera

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                        int i=0;
                        for (FirebaseVisionImageLabel label: labels) {
                            textArray.add(label.getText());
                            confidenceArray.add(label.getConfidence());
                            confidenceStrArray.add(Float.toString(confidenceArray.get(i)));
                            i++;
                        }
                        formatTxt.setText(textArray.get(0));
                        contentTxt.setText(confidenceStrArray.get(0));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        formatTxt.setText("nope");
                        contentTxt.setText("nope");
                    }
                });
    }//End OnActivity result method



}//End class