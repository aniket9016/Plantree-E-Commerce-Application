package com.weare.plantree;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.weare.plantree.Admin.AdminMaintainProductsActivity;
import com.weare.plantree.Buyers.CartActivity;
import com.weare.plantree.Buyers.FertilizerActivity;
import com.weare.plantree.Buyers.PlantsActivity;
import com.weare.plantree.Buyers.PotsActivity;
import com.weare.plantree.Buyers.ProductDetailsActivity;
import com.weare.plantree.Buyers.SearchProductActivity;
import com.weare.plantree.Buyers.SeedsActivity;
import com.weare.plantree.Buyers.SettingsActivity;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.Model.Users;
import com.weare.plantree.Prevalent.Prevalent;
import com.weare.plantree.ViewHolder.ProductViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  //  private Toolbar toolbar;
    private String typeOfUser="";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    FloatingActionButton fab;
    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    SharedPreferences prefs;
    LinearLayout plantsCategory, seedsCategory, fertil_category, pots_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Plantree");
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("TypeUser",MODE_PRIVATE);
        plantsCategory = findViewById(R.id.plantcontainer);
        seedsCategory = findViewById(R.id.seedscontainer);
        fertil_category = findViewById(R.id.fertilizercontainer);
        pots_category = findViewById(R.id.potscontainer);

        //firebase
        mReference= FirebaseDatabase.getInstance().getReference().child("Products");


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null)
        {
           typeOfUser=bundle.getString("Admin");

        }

        //recyclerview
//        mRecyclerView=findViewById(R.id.recycler_view_home);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        //drawer
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        //Paper
        Paper.init(this);

        plantsCategory.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, PlantsActivity.class));

        });

        seedsCategory.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, SeedsActivity.class));

        });

        fertil_category.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, FertilizerActivity.class));

        });

        pots_category.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, PotsActivity.class));

        });


        //fab button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        if (typeOfUser.equals("Admin")){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            navigationView.setVisibility(View.GONE);
           // toolbar.getMenu().clear();
            fab.setVisibility(View.GONE);
        }

        //header view

        if (!typeOfUser.equals("Admin"))
        {
            try {

                View headerview = navigationView.getHeaderView(0);
                TextView userProfileName = headerview.findViewById(R.id.user_profile_name);
                CircleImageView userProfileimage = headerview.findViewById(R.id.user_profile_image);
                Users users = Paper.book().read(Prevalent.currentOnlineUser);
                String user = prefs.getString("Type","");
                if (user.equals("GuestUser")){
                    userProfileName.setText("Guest User");
                }
                else if (user.equals("LoginUser")){
                    userProfileName.setText(users.getName());
                    if (Paper.book().read(Prevalent.hasImageKey, false)) {
                        Picasso.get().load(users.getImage()).placeholder(R.drawable.profile).into(userProfileimage);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerOptions<ProductsModal> options=new FirebaseRecyclerOptions.Builder<ProductsModal>().
//                setQuery(mReference,ProductsModal.class)
//                .build();
//        FirebaseRecyclerAdapter<ProductsModal, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<ProductsModal, ProductViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final ProductsModal model) {
//                holder.productTitle.setText(model.getPname());
//                holder.productDescription.setText(model.getDescription());
//                holder.productPrice.setText(String.format("Price $%s ", model.getPrice()));
//                Picasso.get().load(model.getImage()).into(holder.productImage);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (typeOfUser.equals("Admin"))
//                        {
//
//                            String id = model.getPid();
//                            Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
//                            intent.putExtra("pid", id);
//                            startActivity(intent);
//                        }
//                        else
//                            {
//                            String id = model.getPid();
//                            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
//                            intent.putExtra("pid", id);
//                            startActivity(intent);
//                             }
//                    }
//                });
//
//               // holder.itemView.
//            }
//
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                return new ProductViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.product_item_layout,parent,false));
//            }
//        };
//        mRecyclerView.setAdapter(adapter);
//        adapter.startListening();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (!typeOfUser.equals("Admin")) {
            switch (id) {
                case R.id.nav_cart:
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    break;
//                case R.id.nav_category:

//                    break;
                case R.id.nav_search:
                    startActivity(new Intent(HomeActivity.this, SearchProductActivity.class));

                    break;
                case R.id.nav_setting:
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                    break;
                case R.id.nav_logout:
                    Paper.book().delete(Prevalent.UserPasswordKey);
                    Paper.book().delete(Prevalent.UserphoneKey);
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.nav_terms:
                    showtermsDialog();
                    break;

                case R.id.aboutus:
                    startActivity(new Intent(HomeActivity.this,AboutActivity.class));

            }

        }
        return false;
    }

    private void showtermsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.termslayout);

        dialog.show();
        drawer.closeDrawer(GravityCompat.START);

        Button btn = dialog.findViewById(R.id.ok);

        btn.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (typeOfUser.equals("Admin")){
            super.onBackPressed();
        }
        else {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    protected void onDestroy() {
        Paper.book().delete(Prevalent.currentOnlineUser);
        super.onDestroy();
    }
}
