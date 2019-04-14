//Add to package
package com.ccbury.recyclelabtech;

//Import required dependencies
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

//Begin MainActivity class
public class MainActivity extends AppCompatActivity{
    //Declare required variables
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //Begin onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request camera permissions
        int MY_PERMISSIONS_REQUEST_CAMERA=0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
            }
        }//Obtained camera permissions

        //Create tabbed layout using fragments
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Barcode");
        adapter.addFragment(new Tab2Fragment(), "Search");
        adapter.addFragment(new Tab3Fragment(), "Image");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);


    }//End onCreate method

}//End class