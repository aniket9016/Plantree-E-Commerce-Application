package com.weare.plantree.Admin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private EditText nameEditText,descriptionEditText,priceEditText;
    private Button applyButton,deleteBtn;
    private String id;
    private DatabaseReference mDatabaseReference;
    private ProductsModal modal;
    private StorageReference imageref;
    private String imageUrl;
    ImageView img;
    private Uri imageUri;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        id=getIntent().getStringExtra("pid");
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Products").child(id);

        nameEditText=findViewById(R.id.product_name_detail_maintain);
        descriptionEditText=findViewById(R.id.product_description_detail_maintain);
        priceEditText=findViewById(R.id.product_price_detail_maintain);
        applyButton=findViewById(R.id.apply_btn_maintain);
        deleteBtn=findViewById(R.id.delete_product);
        dialog = new ProgressDialog(this);
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        img = findViewById(R.id.imageView_detail_maintain);
        imageref= FirebaseStorage.getInstance().getReference();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        displayProductInfo();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()){
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
                   String saveCurrentDate = currentDate.format(calendar.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                   String saveCurrentTime = currentTime.format(calendar.getTime());
                    HashMap<String ,Object> productHashMap=new HashMap<>();
                 //   HashMap<String,Object> productHashMap=new HashMap<>();
                    productHashMap.put("pid",id);
                    productHashMap.put("date",saveCurrentDate);
                    productHashMap.put("time",saveCurrentTime);
                    productHashMap.put("description",descriptionEditText.getText().toString());
                    productHashMap.put("image",modal.getImage());
                    productHashMap.put("category",modal.getCategory());
                    productHashMap.put("price",priceEditText.getText().toString());
                    productHashMap.put("pname",nameEditText.getText().toString());

                    mDatabaseReference.updateChildren(productHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(AdminMaintainProductsActivity.this, "product updated succesfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(AdminMaintainProductsActivity.this, "Something went wrong!! try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }

    private void delete() {
     mDatabaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 imageref.child(imageUrl).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful())
                         {
                             Toast.makeText(AdminMaintainProductsActivity.this, "Image deleted succesfully", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
                 Toast.makeText(AdminMaintainProductsActivity.this, "Product deleted succesfully", Toast.LENGTH_SHORT).show();
                 finish();
             }
             else {
                 Toast.makeText(AdminMaintainProductsActivity.this, "Error occored", Toast.LENGTH_SHORT).show();
             }
         }
     });

    }

//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, 3450);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 3450 && resultCode == RESULT_OK) {
//            if (data != null) {
//                imageUri = data.getData();
//                img.setImageURI(imageUri);
//
//            }
//        }
//    }


    private void displayProductInfo()
    {
        dialog.setMessage("Loading");
        dialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: Displaying");
                if (snapshot.exists()){
                    modal=snapshot.getValue(ProductsModal.class);
                    if (modal!=null)
                    {

                        nameEditText.setText(modal.getPname());
                        descriptionEditText.setText(modal.getDescription());
                        priceEditText.setText(modal.getPrice());
                        imageUrl=modal.getImage();
                        Glide.with(AdminMaintainProductsActivity.this).load(imageUrl).into(img);
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onDataChange: not Displaying"+ error.getMessage());
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private boolean checkEditText() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(descriptionEditText.getText().toString()))
        {
            Toast.makeText(this, "enter description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(priceEditText.getText().toString()))
        {
            Toast.makeText(this, "enter price", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
