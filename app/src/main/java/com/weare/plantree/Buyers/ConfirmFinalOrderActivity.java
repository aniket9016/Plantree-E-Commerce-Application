package com.weare.plantree.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weare.plantree.HomeActivity;
import com.weare.plantree.Model.Users;
import com.weare.plantree.Prevalent.Prevalent;
import com.weare.plantree.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText phoneEditText,nameEditText,adressEditText,cityEditText,email;
    private Button confirmButton;
    int totalprice;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        phoneEditText=findViewById(R.id.phone_edit_confirm);
        nameEditText=findViewById(R.id.name_edit_confirm);
        adressEditText=findViewById(R.id.adress_edit_confirm);
        cityEditText=findViewById(R.id.city_edit_confirm);
        confirmButton=findViewById(R.id.confirm_button_confirm);
        email = findViewById(R.id.email);
        Paper.init(this);

        totalprice=getIntent().getIntExtra("total",0);
      //  Toast.makeText(this, "total: $" +String.valueOf(totalprice), Toast.LENGTH_SHORT).show();

        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    if (checkEditText())
                    {
                        confirmOrder();
                    }
            }
        });

    }

    private void confirmOrder()
    {
        Intent intent = new Intent(ConfirmFinalOrderActivity.this,PaymentActivity.class);
        intent.putExtra("total",totalprice);
        intent.putExtra("name",nameEditText.getText().toString());
        intent.putExtra("phone",nameEditText.getText().toString());
        intent.putExtra("address",nameEditText.getText().toString());
        intent.putExtra("city",cityEditText.getText().toString());
        intent.putExtra("email",email.getText().toString());
        startActivity(intent);


    }

    private boolean checkEditText() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "enter phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(adressEditText.getText().toString()))
        {
            Toast.makeText(this, "enter adress", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "enter city", Toast.LENGTH_SHORT).show();
            return false;
        }
        else   if (TextUtils.isEmpty(email.getText().toString()))
        {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
