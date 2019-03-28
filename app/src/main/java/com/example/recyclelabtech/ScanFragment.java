//Add to package
package com.example.recyclelabtech;
//Import dependencies
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//begin class
public class ScanFragment extends Fragment {
    //Declare required variables
    String codeContent, codeFormat;
    final String noResultErrorMsg = "No scan data received!";

    //Method to run when ScanFragment is created
    public void onCreate(Bundle savedInstanceState) {
        //Begin barcode scanner
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        integrator.setCameraId(0);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();

        super.onCreate(savedInstanceState);
    }//End onCreate method

    //When barcode scanner is run
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //have result
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();
            //send received data
            Tab1Fragment.scanResultData(codeFormat,codeContent);
        }else{
            //send exception
            Tab1Fragment.scanResultData(new NoScanResultException(noResultErrorMsg));
        }//End if/else
    }//End onActivityResult
}//End class
