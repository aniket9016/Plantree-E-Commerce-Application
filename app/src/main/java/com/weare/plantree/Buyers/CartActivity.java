package com.weare.plantree.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weare.plantree.LoginActivity;
import com.weare.plantree.Model.CartModel;
import com.weare.plantree.Model.Users;
import com.weare.plantree.Prevalent.Prevalent;
import com.weare.plantree.R;
import com.weare.plantree.RegisterActivity;
import com.weare.plantree.ViewHolder.CartViewHolder;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    private TextView totalAmountTextView,msg1;
    private Button nextButton;
    private RecyclerView mRecyclerView;
    DatabaseReference cartRef;
    FirebaseAuth auth;
    FirebaseUser user;
    int totaPrice=0;
    Users curentUser;
    boolean firsttime=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalAmountTextView=findViewById(R.id.total_price_cart_text_view);
        nextButton=findViewById(R.id.next_cartList);
        mRecyclerView=findViewById(R.id.cart_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setHasFixedSize(true);
        Paper.init(this);
        curentUser=Paper.book().read(Prevalent.currentOnlineUser);
        msg1=findViewById(R.id.msg1);

        checkOrdersState();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (curentUser == null){
                    Dialog dialog = new Dialog(CartActivity.this);
                    dialog.setContentView(R.layout.loginorregisterdialog);

                    Button login = dialog.findViewById(R.id.gotologin);
                    Button register = dialog.findViewById(R.id.gotoregister);
                    Button cancel = dialog.findViewById(R.id.cancel);
                    dialog.show();

                    login.setOnClickListener(vi->{
                        startActivity(new Intent(CartActivity.this, LoginActivity.class));
                        finish();

                    });

                    register.setOnClickListener(vi->{
                        startActivity(new Intent(CartActivity.this, RegisterActivity.class));
                        finish();
                    });

                    cancel.setOnClickListener(vi -> {
                        dialog.dismiss();
                    });
                }
                else {
                    if (firsttime) {
                        firsttime = false;
                        totalAmountTextView.setText("Total Paying price= Rs " + String.valueOf(totaPrice));

                    } else {

                        Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("total", totaPrice);
                        startActivity(intent);
                    }
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (curentUser == null){
            Log.d("TAG", "onStart: ");
        }
        else {
            cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                    .child(curentUser.getPhone())
                    .child("Products");
            FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>().setQuery(cartRef, CartModel.class).build();

            FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final CartModel model) {
                    holder.titleTextView.setText(model.getPname());
                    holder.quantityTextView.setText("Quantity: " + model.getQuantity());
                    holder.priceTextView.setText("Price: Rs" + model.getPrice());

                    int quantity = Integer.parseInt(model.getQuantity());
                    int p = Integer.parseInt(model.getPrice());
                    int tp = (quantity * p);
                    totaPrice = totaPrice + tp;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence option[] = new CharSequence[]{
                                    "Edit"
                                    , "Delete"
                            };
                            final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                            builder.setTitle("Cart option:");
                            builder.setItems(option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                        intent.putExtra("pid", model.getPid());
                                        startActivity(intent);

                                    } else if (which == 1) {
                                        cartRef.child(model.getPid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(CartActivity.this, "Error occor", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                    }
                                }
                            });
                            builder.setCancelable(true);
                            builder.show();
                        }
                    });

                    if (totaPrice == 0) {
                        nextButton.setVisibility(View.INVISIBLE);
                    } else {
                        nextButton.setVisibility(View.VISIBLE);
                    }

                }

                @NonNull
                @Override
                public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new CartViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.cart_list_item, parent, false));
                }
            };
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

    private void checkOrdersState() {
        if (curentUser == null) {
            Log.d("TAG", "checkOrdersState: ");
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(curentUser.getPhone());
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String state = snapshot.child("state").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        if (state.equals("shipped")) {
                            totalAmountTextView.setText("Shipping state :shipped");
                            msg1.setVisibility(View.VISIBLE);
                            msg1.setText("Dear " + name + " your order has been shipped sucesfully");
                            nextButton.setVisibility(View.INVISIBLE);
                        } else if (state.equals("not shipped")) {
                            totalAmountTextView.setText("Shipping state : not shipped");
                            msg1.setVisibility(View.VISIBLE);
                            nextButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}
