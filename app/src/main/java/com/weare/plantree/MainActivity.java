package com.weare.plantree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weare.plantree.Model.Users;
import com.weare.plantree.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginNow,signupNow;
    private ProgressDialog loadingbar;
    TextView guestContinue;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingbar = new ProgressDialog(this);
        loginNow=findViewById(R.id.main_login_now_btn);
        signupNow=findViewById(R.id.main_join_now_btn);
        guestContinue = findViewById(R.id.guestLogin);
        prefs = getSharedPreferences("TypeUser",MODE_PRIVATE);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Type","LoginUser");
                editor.apply();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        });
        signupNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Type","LoginUser");
                editor.apply();
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        guestContinue.setOnClickListener(view->{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Type","GuestUser");
            editor.apply();
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        });


        Paper.init(this);
        String phone=Paper.book().read(Prevalent.UserphoneKey,"");
        String password=Paper.book().read(Prevalent.UserPasswordKey,"");

        if (!phone.equals("") && !password.equals(""))
        {
            if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password))
            {

            AllowAscessToAccount(phone,password);
            }
        }

    }

    private void AllowAscessToAccount(final String phone, final String password) {
        loadingbar.setTitle("Already Logged in...");
        loadingbar.setMessage("please wait..processing");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String parentDbName="Users";
                if (snapshot.child(parentDbName).child(phone).exists()){
                    Users users=snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    assert users != null;
                    if (users.getPhone().equals(phone)){
                        if (users.getPassword().equals(password)){
                            Paper.book().write(Prevalent.currentOnlineUser,users);

                            Toast.makeText(MainActivity.this, "you are  already logged in ..please wait ", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent=new Intent(MainActivity.this, TermsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        loadingbar.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "account with this phonr number "+phone+" does not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
