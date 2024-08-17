package com.weare.plantree.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.weare.plantree.HomeActivity;
import com.weare.plantree.Model.Users;
import com.weare.plantree.Prevalent.Prevalent;
import com.weare.plantree.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button onlinePayment, cashOnDelhivery;
    Intent intent;
    String name, phone, address, city,email;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        onlinePayment = findViewById(R.id.onlineBtn);
        cashOnDelhivery = findViewById(R.id.cashondelhiveryBtn);
        intent = getIntent();
        Paper.init(this);
        Checkout.preload(this);

        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        city = intent.getStringExtra("city");
        email = intent.getStringExtra("email");
        price = intent.getIntExtra("total",0);

        cashOnDelhivery.setOnClickListener(view-> {
                    String saveCurrentTime, saveCurrentDate;
                    Calendar calendarForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
                    saveCurrentDate = currentDate.format(calendarForDate.getTime());

                    Calendar calendarForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    saveCurrentTime = currentTime.format(calendarForTime.getTime());
                    final Users users = Paper.book().read(Prevalent.currentOnlineUser);

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child("Orders").child(users.getPhone());

                    final HashMap<String, Object> orderFinalHashMap = new HashMap<>();
                    orderFinalHashMap.put("totalAmount", String.valueOf(price));
                    orderFinalHashMap.put("name", name);
                    orderFinalHashMap.put("phone", phone);
                    //  productHashMap.put("description",description);
                    orderFinalHashMap.put("adress", address);
                    orderFinalHashMap.put("city", city);
                    orderFinalHashMap.put("date", saveCurrentDate);
                    orderFinalHashMap.put("email",email);
                    orderFinalHashMap.put("time", saveCurrentTime);
                    orderFinalHashMap.put("paymentType","Cash On Delhivery");
                    orderFinalHashMap.put("state", "not shipped");
                    reference.updateChildren(orderFinalHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                                        .child(users.getPhone())
                                        .child("Products");
                                removeRef.removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(PaymentActivity.this, "Your final order has been confiremed", Toast.LENGTH_SHORT).show();
                                                    Intent intent2 = new Intent(PaymentActivity.this, HomeActivity.class);
                                                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent2);
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });

        onlinePayment.setOnClickListener(view->{
            final Users users = Paper.book().read(Prevalent.currentOnlineUser);
            Activity activity = PaymentActivity.this;
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_02ycCysq3qM3m3");
            checkout.setImage(R.drawable.plantree);
            double amt = Float.parseFloat(String.valueOf(price))*100;
            try {
                JSONObject object = new JSONObject();
                object.put("name","Bhavik AshokBhai Suthar");
                object.put("description","Reference No. "+String.valueOf(System.currentTimeMillis()));
                object.put("theme.color","#F00000");
                object.put("currency","INR");
                object.put("amount",amt+"");
                object.put("prefill.email",email);
                object.put("prefill.contact",users.getPhone());

                checkout.open(activity,object);
            }
            catch (Exception e){
                Toast.makeText(activity, "Payment Failed "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onPaymentSuccess(String s) {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendarForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        Calendar calendarForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendarForTime.getTime());
        final Users users = Paper.book().read(Prevalent.currentOnlineUser);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(users.getPhone());

        final HashMap<String, Object> orderFinalHashMap = new HashMap<>();
        orderFinalHashMap.put("totalAmount", String.valueOf(price));
        orderFinalHashMap.put("name", name);
        orderFinalHashMap.put("phone", phone);
        //  productHashMap.put("description",description);
        orderFinalHashMap.put("adress", address);
        orderFinalHashMap.put("city", city);
        orderFinalHashMap.put("email",email);
        orderFinalHashMap.put("date", saveCurrentDate);
        orderFinalHashMap.put("time", saveCurrentTime);
        orderFinalHashMap.put("paymentType","Online Payment");
        orderFinalHashMap.put("state", "not shipped");
        reference.updateChildren(orderFinalHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(users.getPhone())
                            .child("Products");
                    removeRef.removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        Toast.makeText(PaymentActivity.this, "Your final order has been confiremed", Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(PaymentActivity.this, HomeActivity.class);
                                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent2);
                                        finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(this, "Payment Successfull", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}