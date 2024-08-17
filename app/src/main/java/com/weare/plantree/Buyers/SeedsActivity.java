package com.weare.plantree.Buyers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.ProductAdapter;
import com.weare.plantree.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SeedsActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    ArrayList<ProductsModal> list = new ArrayList<>();
    ProductAdapter adapter;
    EditText search;
    TextView seedsTV;
    ProgressBar progressBar;
    Button search_btn;
    String input;
    ArrayList<ProductsModal> getList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeds);

        mRecyclerView = findViewById(R.id.seeds_recycler_view);
        mReference= FirebaseDatabase.getInstance().getReference().child("Products");
        search = findViewById(R.id.search_seeds);
        seedsTV = findViewById(R.id.seedsTV);
        progressBar = findViewById(R.id.progress2);
        search_btn = findViewById(R.id.search_seeds_btn);

        getData();
        progressBar.setVisibility(View.VISIBLE);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressBar.setVisibility(View.VISIBLE);
                SharedPreferences preferences = getSharedPreferences("Search2",MODE_PRIVATE);
                Gson gson = new Gson();
                String listData = preferences.getString("List2",null);
                Type type = new TypeToken<ArrayList<ProductsModal>>(){}.getType();
                getList = gson.fromJson(listData,type);
                ArrayList<ProductsModal> searchList = new ArrayList<>();


                if (getList == null){
                    progressBar.setVisibility(View.GONE);
                    seedsTV.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    seedsTV.setVisibility(View.GONE);
                    for (ProductsModal modal : getList) {
                        Log.d("TAG", modal.getPname().toString());
                        if (modal.getPname().contains(s)) {
                            searchList.add(modal);
                        }
                    }
                    adapter = new ProductAdapter(searchList, SeedsActivity.this);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getData(){
        Query query = mReference.orderByChild("category").equalTo("Seeds");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot data : snapshot.getChildren()){
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
                    SharedPreferences preferences = getSharedPreferences("Search2", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    editor.putString("List2", json);
                    editor.apply();

                    adapter = new ProductAdapter(list, SeedsActivity.this);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                    mRecyclerView.setAdapter(adapter);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    seedsTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}