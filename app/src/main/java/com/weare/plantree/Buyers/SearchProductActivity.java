package com.weare.plantree.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weare.plantree.Admin.AdminMaintainProductsActivity;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.R;
import com.weare.plantree.ViewHolder.ProductViewHolder;


public class SearchProductActivity extends AppCompatActivity {

    private Button searchProductBtn;
    private EditText searchEditText;
    private RecyclerView recyclerViewSearch;
    private String searchInput;
    private String typeOfUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        searchProductBtn=findViewById(R.id.search_btn);
        searchEditText=findViewById(R.id.search_product_name);
        recyclerViewSearch=findViewById(R.id.search_recycler_view);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setHasFixedSize(true);

        searchProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput=searchEditText.getText().toString();
                onStart();
            }
        });

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null)
        {
            typeOfUser=bundle.getString("Admin");

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<ProductsModal> options=new FirebaseRecyclerOptions.Builder<ProductsModal>()
                .setQuery(reference.orderByChild("pname").startAt(searchInput), ProductsModal.class)
                .build();
        FirebaseRecyclerAdapter<ProductsModal, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<ProductsModal, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final ProductsModal model) {
                holder.productTitle.setText(model.getPname());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText(String.format("Price Rs%s ", model.getPrice()));
                Glide.with(SearchProductActivity.this).load(model.getImage()).into(holder.productImage);
//                Picasso.get().load(model.getImage()).into(holder.productImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (typeOfUser.equals("Admin"))
                        {

                            String id = model.getPid();
                            Intent intent = new Intent(SearchProductActivity.this, AdminMaintainProductsActivity.class);
                            intent.putExtra("pid", id);
                            startActivity(intent);
                        }
                        else
                            {
                            String id = model.getPid();
                            Intent intent = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", id);
                            startActivity(intent);
                            }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false));
            }

        };
        recyclerViewSearch.setAdapter(adapter);
        adapter.startListening();
       // adapter.
    }
}
