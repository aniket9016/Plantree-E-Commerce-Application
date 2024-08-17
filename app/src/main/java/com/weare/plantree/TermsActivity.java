package com.weare.plantree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class TermsActivity extends AppCompatActivity {

    Button decline, Accept;
    SharedPreferences pref;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        decline = findViewById(R.id.decline);
        Accept = findViewById(R.id.accept);
        pref = getSharedPreferences("Terms",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int i2 = pref.getInt("I",0);

        if (i2==0){
            Toast.makeText(this, "Please Read this Terms And Services and after That Accept It", Toast.LENGTH_LONG).show();
        }
        else {
            startActivity(new Intent(TermsActivity.this,HomeActivity.class));
            finish();
        }

        Accept.setOnClickListener(view->{
            i = i+1;
            editor.putInt("I",i);
            editor.apply();
            startActivity(new Intent(TermsActivity.this,HomeActivity.class));
            finish();
        });

        decline.setOnClickListener(view->{
            this.finish();
        });
    }
}