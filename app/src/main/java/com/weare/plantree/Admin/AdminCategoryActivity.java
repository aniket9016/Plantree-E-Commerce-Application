package com.weare.plantree.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.weare.plantree.Buyers.SearchProductActivity;
import com.weare.plantree.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView plants, seeds, fertilizer, pots;

    private List<String> mStringList = new ArrayList<String>(Arrays.asList("Plants"
            , "Seeds", "Fertilizer", "Pots"));

    public static   List<ImageView> mImageViewArrayList;

    private Button logoutBtn,checkNewProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        plants=findViewById(R.id.plantsImg);
        seeds=findViewById(R.id.seedsImg);
        fertilizer=findViewById(R.id.fertilizerImg);
        pots=findViewById(R.id.potsImg);

      mImageViewArrayList = new ArrayList<>(Arrays.asList(plants, seeds, fertilizer, pots));

        for (int i = 0; i<mImageViewArrayList.size(); i++){
            final int currentItem=i;
            mImageViewArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                int c=currentItem;
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category",mStringList.get(currentItem));
                    startActivity(intent);
                }
            });
        }

        logoutBtn=findViewById(R.id.admin_logout_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        checkNewProductBtn=findViewById(R.id.check_new_orders_button);
        checkNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class));
            }
        });

        Button maintain_products_btn=findViewById(R.id.maintain_products_admin);
        maintain_products_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this, SearchProductActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

    }
}
