package com.example.mybankapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class BankAccountActivity extends AppCompatActivity {

    AccountsRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);
        try {
            String infos = new APIConnection().execute().get();
            if(infos!=null){
                Toast.makeText(this, "Connected to the Internet!", Toast.LENGTH_SHORT).show();
                encryptBankAccountsData(infos);
            }
            else{
                Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                infos = decryptBankAccountsData();
            }
            JSONArray json = new JSONArray(infos);
            ArrayList<String> accounts = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonobject = json.getJSONObject(i);
                String accountName = jsonobject.getString("accountName");
                String amount = jsonobject.getString("amount");
                String iban = jsonobject.getString("iban");
                String currency = jsonobject.getString("currency");
                accounts.add(accountName+"\n"+"IBAN "+iban+"\n"+amount+" "+currency);
            }
            RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AccountsRecyclerViewAdapter(this, accounts);
            recyclerView.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuRefresh:
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.menuRetour:
                Intent mainActivity = new Intent(BankAccountActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
                break;
        }
        return true;
    }
    private void encryptBankAccountsData(String data){
        Context context = getApplicationContext();
        MasterKey mainKey = null;
        try {
            mainKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileToWrite = "recovered_data.txt";
        EncryptedFile encryptedFile = null;
        try {
            encryptedFile = new EncryptedFile.Builder(context,
                    new File(context.getFilesDir(), fileToWrite),
                    mainKey,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] fileContent = data
                .getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = null;
        try {
            outputStream = encryptedFile.openFileOutput();
            outputStream.write(fileContent);
            outputStream.flush();
            outputStream.close();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String decryptBankAccountsData(){
        Context context = getApplicationContext();
        MasterKey mainKey = null;
        try{
            mainKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            String fileToRead = "recovered_data.txt";
            EncryptedFile encryptedFile = new EncryptedFile.Builder(context,
                    new File(context.getFilesDir(), fileToRead),
                    mainKey,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();
            InputStream inputStream = encryptedFile.openFileInput();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int nextByte = inputStream.read();
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte);
                nextByte = inputStream.read();
            }

            byte[] plaintext = byteArrayOutputStream.toByteArray();
            String plaindata = new String(plaintext,StandardCharsets.UTF_8);
            return plaindata;
        }
        catch(Exception e){
           return null;
        }
    }

    class APIConnection extends AsyncTask<Void, String, String> {
        
        byte[] var5 = Base64.decode(BuildConfig.Var5, Base64.DEFAULT);
        String var8 = new String(var5);
        protected String doInBackground(Void... a){
            try {
                URL url = new URL(var8);
                HttpsURLConnection urlConnection = null;
                String result = "";
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    var5=null;
                    var8=null;
                    in.close();
                }

                return result;
            }
            catch (Exception e) {

            }
            return null;
        }
    }
}