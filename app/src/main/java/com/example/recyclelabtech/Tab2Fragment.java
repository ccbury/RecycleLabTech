package com.example.recyclelabtech;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Tab2Fragment extends Fragment {
    EditText mSearchField;
    ImageButton mSearchBtn;
    RecyclerView mResultList;
    DatabaseReference mProductDatabase;
    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_two, container, false);

        mProductDatabase = FirebaseDatabase.getInstance().getReference("Items");
        mSearchField = (EditText) getActivity().findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) getActivity().findViewById(R.id.search_btn);

        mResultList = (RecyclerView) getActivity().findViewById(R.id.result_list);
        //mResultList.hasFixedSize();
        //mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mResultList.setLayoutManager(new LinearLayoutManager(getContext()));


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
        Query firebaseSearchQuery = mProductDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                .setQuery(firebaseSearchQuery

                        , products.class)
                .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<products, ProductViewHolder>(options){
            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_layout, parent, true);

                return new ProductViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ProductViewHolder holder, int position, products model) {
                holder.setDetails(getActivity().getApplicationContext(), model.getName(), model.getDescription(), model.getBarcodefmt(), model.getManufactor(), model.getImage());
            }
        };

        mResultList.setAdapter(adapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//End constructor

        public void setDetails(Context ctx, String name, String description, String manufator, String barcodefmt, String image){
            TextView product_name = (TextView) mView.findViewById(R.id.name_text);
            TextView product_description = (TextView) mView.findViewById(R.id.description_text);
            TextView product_manufactor = (TextView) mView.findViewById(R.id.manufactor_text);
            TextView product_barcode = (TextView) mView.findViewById(R.id.barcode_text);
            ImageView product_image = (ImageView) mView.findViewById(R.id.product_image);

            product_name.setText(name);
            product_description.setText(description);
            product_manufactor.setText(manufator);
            product_barcode.setText(barcodefmt);

            Glide.with(ctx).load(image).into(product_image);
        }


    }//End class
}//End class
