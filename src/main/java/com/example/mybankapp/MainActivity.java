package com.example.mybankapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        EditText var1Text = findViewById(R.id.var1Text);
        String var1 = var1Text.getText().toString();
        EditText var2Text = findViewById(R.id.var2Text);
        String var2 = var2Text.getText().toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(BuildConfig.Var6);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] var3 = digest.digest(var1.getBytes(StandardCharsets.UTF_8));
        var1 = String.format("%064x", new BigInteger(1, var3));
        byte[] var4 = digest.digest(var2.getBytes(StandardCharsets.UTF_8));
        var2 = String.format("%064x", new BigInteger(1, var4));
        if(var1.equals(BuildConfig.Var1) && var2.equals(BuildConfig.Var2)){
            Intent bankAccountActivity = new Intent(MainActivity.this, BankAccountActivity.class);
            startActivity(bankAccountActivity);
            var1Text.setText(null);
            var2Text.setText(null);
            finish();
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}