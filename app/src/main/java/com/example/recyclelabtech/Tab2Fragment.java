//Add to package
package com.example.recyclelabtech;
//Import required dependencies
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//Begin class
public class Tab2Fragment extends Fragment {
    //Declare required variables
    DatabaseReference mProductDatabase;
    RecyclerView mResultList;
    View myView;
    ImageButton mSearchBtn;
    EditText mSearchField;
    int i;
    //Begin onCreateView method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_two, container, false);

        mSearchBtn = (ImageButton) myView.findViewById(R.id.search_btn);
        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");    //get instance of database
        mSearchField =  myView.findViewById(R.id.search_field);
        mResultList = myView.findViewById(R.id.result_list);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Add listener to allow enter button on keyboard function as search button
        mSearchField.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = mSearchField.getText().toString();
                    firebaseProductSearch(searchText);
                    return true;
                }
                return false;
            }
        });//End onKeyListener

        //Add listener for dedicated search button
        mSearchBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               String searchText = mSearchField.getText().toString();
               firebaseProductSearch(searchText);

           }
        });//End onClickListener

        return myView;
    }//End onCreateView method

    //Add method to query database for search
    private void firebaseProductSearch(String searchText) { ;
        //Add query to search database for entries that match users search
        Query firebaseSearchQuery = mProductDatabase.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");
        //Create Recycler Adapter to hold all appropriate entries
        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<products, ProductViewHolder>(
                products.class,
                R.layout.list_layout,
                ProductViewHolder.class,
                firebaseSearchQuery
        ){
            //Create populateViewHolder to store database entries
            protected void populateViewHolder(ProductViewHolder holder, products model, int position) {
                i++;
                holder.setDetails(myView.getContext(), model.getName(), model.getDescription(), model.getBarcodefmt(), model.getManufactor(), model.getImage(), model.getBarcodecnt());
            }


        };//End recycler
        mResultList.setAdapter(adapter);
    }//End firebaseProductSearch

    //Create productViewHolder to display database entries in list
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        //Used to ensure correct view is used
        View mView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//End constructor

        //Create setDetails to add entry to results list. Displayed to user using list_layout.xml template
        public void setDetails(Context ctx, String name, String description, String barcodefmt, String manufactor, String image, String barcodecnt) {
            TextView product_name = mView.findViewById(R.id.name_text);
            TextView product_description = mView.findViewById(R.id.description_text);
            TextView product_manufactor = mView.findViewById(R.id.manufactor_text);
            TextView product_barcode = mView.findViewById(R.id.barcode_text);
            ImageView product_image = mView.findViewById(R.id.product_image);
            if(manufactor == null){
                manufactor = "Not Available";
            }
            if(barcodecnt == null){
                barcodecnt = "Not Available";
            }
            if(description == null){
                description ="Sorry. A description for this item is not currently available. We will remedy this issue as soon as possible. Please check back later!";
            }
            product_name.setText("Name: " + name);
            product_description.setText("Description: "+description);
            product_manufactor.setText("Manufactor: "+manufactor);
            product_barcode.setText("Barcode: "+barcodecnt);
            Glide.with(ctx).load(image).into(product_image);
        }//End setDetails
    }//End productViewHolder

}//End class
