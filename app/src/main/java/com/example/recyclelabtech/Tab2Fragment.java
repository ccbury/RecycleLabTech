package com.example.recyclelabtech;

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


public class Tab2Fragment extends Fragment {

    DatabaseReference mProductDatabase;
    RecyclerView mResultList;
    View myView;
    ImageButton mSearchBtn;
    EditText mSearchField;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_two, container, false);

        mSearchBtn = (ImageButton) myView.findViewById(R.id.search_btn);
        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");
        mSearchField =  myView.findViewById(R.id.search_field);
        mResultList = myView.findViewById(R.id.result_list);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               String searchText = mSearchField.getText().toString();
               firebaseProductSearch(searchText);
           }
        });

        return myView;
    }//End onCreateView method

    private void firebaseProductSearch(String searchText) {
        Query firebaseSearchQuery = mProductDatabase.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");
        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<products, ProductViewHolder>(
                products.class,
                R.layout.list_layout,
                ProductViewHolder.class,
                firebaseSearchQuery
        ){
            protected void populateViewHolder(ProductViewHolder holder, products model, int position) {
                holder.setDetails(myView.getContext(), model.getName(), model.getDescription(), model.getBarcodefmt(), model.getManufactor(), model.getImage());
            }
        };

        mResultList.setAdapter(adapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//End constructor

        public void setDetails(Context ctx, String name, String description, String manufator, String barcodefmt, String image) {
            TextView product_name = mView.findViewById(R.id.name_text);
            TextView product_description = mView.findViewById(R.id.description_text);
            TextView product_manufactor = mView.findViewById(R.id.manufactor_text);
            TextView product_barcode = mView.findViewById(R.id.barcode_text);
            ImageView product_image = mView.findViewById(R.id.product_image);

            product_name.setText(name);
            product_description.setText(description);
            product_manufactor.setText(manufator);
            product_barcode.setText(barcodefmt);

            Glide.with(ctx).load(image).into(product_image);
        }
    }

}//End class
