package com.weare.plantree.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.ProductAdapter;
import com.weare.plantree.R;
import com.weare.plantree.ViewHolder.ProductViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlantsActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    ArrayList<ProductsModal> list = new ArrayList<>();
    ProductAdapter adapter;
    EditText search;
    Button search_btn;
    TextView plantsTV;
    String input;
    ArrayList<ProductsModal> getList = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);

        mRecyclerView = findViewById(R.id.plants_recycler_view);
        mReference= FirebaseDatabase.getInstance().getReference().child("Products");
        search = findViewById(R.id.search_plants);
        search_btn = findViewById(R.id.search_plants_btn);
        plantsTV = findViewById(R.id.plantTV);
        progressBar = findViewById(R.id.progress);

        getData();
        progressBar.setVisibility(View.VISIBLE);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    progressBar.setVisibility(View.VISIBLE);
                    SharedPreferences preferences = getSharedPreferences("Search1", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String listData = preferences.getString("List1", null);
                    Type type = new TypeToken<ArrayList<ProductsModal>>() {
                    }.getType();
                    getList = gson.fromJson(listData, type);
                    ArrayList<ProductsModal> searchList = new ArrayList<>();

                    if (getList == null) {
                        plantsTV.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        plantsTV.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        for (ProductsModal modal : getList) {
                            Log.d("TAG", modal.getPname().toString());
                            if (modal.getPname().contains(s)) {
                                searchList.add(modal);
                            }
                        }
                        adapter = new ProductAdapter(searchList, PlantsActivity.this);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        mRecyclerView.setAdapter(adapter);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getData(){
        Query query = mReference.orderByChild("category").equalTo("Plants");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    progressBar.setVisibility(View.VISIBLE);
                    String pid = data.child("pid").getValue().toString();
                    String name = data.child("pname").getValue().toString();
                    String descr = data.child("description").getValue().toString();
                    String img = data.child("image").getValue().toString();
                    String price = data.child("price").getValue().toString();

                    ProductsModal modal = new ProductsModal();
                    modal.setPid(pid);
                    modal.setPname(name);
                    modal.setPrice(price);
                    modal.setDescription(descr);
                    modal.setImage(img);

                    list.add(modal);
                }

                if (!list.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    SharedPreferences preferences = getSharedPreferences("Search1", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    editor.putString("List1", json);
                    editor.apply();

                    adapter = new ProductAdapter(list, PlantsActivity.this);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                    mRecyclerView.setAdapter(adapter);
                }
                else {
                    plantsTV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}