//Add to package
package com.ccbury.recyclelabtech;

//Import dependencies
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//begin class
public class Tab1Fragment extends Fragment implements ScanResultReceiver, View.OnClickListener {
    //Declare required variables
    Button myButton;
    static View myView;
    static DatabaseReference mProductDatabase;

    static TextView product_name, product_description, product_barcode, product_manufactor;
    static ImageView product_image;

    static String searchText;

    //Begin onCreateView method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create button that when pressed creates an instance of ScanFragment for scanning barcodes
        myView = inflater.inflate(R.layout.fragment_one, container, false);
        myButton = (Button) myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);

        //Link objects to XML buttons
        product_name = myView.findViewById(R.id.name_text);
        product_manufactor = myView.findViewById(R.id.manufactor_text);
        product_description = myView.findViewById(R.id.description_text);
        product_barcode = myView.findViewById(R.id.barcode_text);
        product_image = myView.findViewById(R.id.product_image);
        product_image.setVisibility(View.INVISIBLE);

        //Create database reference
        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");

        return myView;
    }//End onCreateView

    //Begin scanNow to create the ScanFragment fragment
    //This launches the camera
    public void scanNow(View view){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScanFragment scanFragment = new ScanFragment();
        fragmentTransaction.add(R.id.scan_fragment,scanFragment, "Barcode1");
        fragmentTransaction.commit();
    }//End scanNow class

    //Add onClick that calls scanNow method
    public void onClick(View v) {
        scanNow(v);
    }

    //begin scanResultData to handle data received from ScanFragment
    //Insure all fields are clear from potential previous search
    //Call setResult to query database and display query result
    public static void scanResultData(String codeFormat, String codeContent){
        searchText = codeContent;
        product_name.setText("");
        product_manufactor.setText("");
        product_description.setText("");
        product_barcode.setText(searchText);
        product_image.setVisibility(View.INVISIBLE);
        if(searchText == null){
            product_name.setText("Error. No Barcode Detected");
        }else {
            setResult();
        }
    }//End scanResultData

    //scanResultData to handle no result from ScanFragment
    public static void scanResultData(NoScanResultException noScanData) { }//End scanResultData

    static products prod;   //Create products object to store database entry obtained from query

    //Begin setResult, queries database using barcode obtained from device camera
    public static void setResult() {
        //Begin query using barcode
        mProductDatabase.orderByChild("barcodecnt").equalTo(searchText).addChildEventListener(new ChildEventListener() {
            //Add onChildAdded to display result when obtained from database
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                prod = dataSnapshot.getValue(products.class);
                product_name.setText("Name: "+prod.getName());
                product_manufactor.setText("Manufactor: "+prod.getManufactor());
                product_description.setText("Description: "+prod.getDescription());
                product_barcode.setText("Barcode: "+prod.getBarcodecnt());
                product_image.setVisibility(View.VISIBLE);
                Glide.with(myView.getContext()).load(prod.getImage()).into(product_image);

                if(prod.getBarcodecnt().isEmpty()){
                    product_barcode.setText(searchText);
                }//End if

            }//End onChildAdded

            //Unused methods for adding to database ect.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
            //End unused methods

        });//End query

    }//End setResult

}//End class