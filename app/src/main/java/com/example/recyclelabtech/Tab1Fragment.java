//Add to package
package com.example.recyclelabtech;

//Import dependencies
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//begin class
public class Tab1Fragment extends Fragment implements ScanResultReceiver, View.OnClickListener {
    //Declare required variables
    Button myButton;
    View myView;
    static TextView formatTxt, contentTxt;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //Begin onCreateView method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create button that when pressed creates an instance of ScanFragment for scanning barcodes
        myView = inflater.inflate(R.layout.fragment_one, container, false);
        myButton = (Button) myView.findViewById(R.id.btn_scan_now);
        myButton.setOnClickListener(this);
        formatTxt = (TextView) myView.findViewById(R.id.scan_format);
        contentTxt = (TextView) myView.findViewById(R.id.scan_content);
        return myView;
    }//End onCreateView

    //Begin scanNow to create the ScanFragment fragment
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
    public static void scanResultData(String codeFormat, String codeContent){
        // display it on screen
        formatTxt.setText("FORMAT: " + codeFormat);
        contentTxt.setText("CONTENT: " + codeContent);
    }//End scanResultData

    //scanResultData to handle no result from ScanFragment
    public static void scanResultData(NoScanResultException noScanData) {
        formatTxt.setText("FORMAT: unknown");
        contentTxt.setText("CONTENT: _error_");
    }//End scanResultData

}//End class