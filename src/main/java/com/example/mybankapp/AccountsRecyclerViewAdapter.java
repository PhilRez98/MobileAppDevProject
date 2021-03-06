package com.example.mybankapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountsRecyclerViewAdapter extends RecyclerView.Adapter<AccountsRecyclerViewAdapter.ViewHolder> {

    private List<String> accounts;
    private LayoutInflater inflater;

    // data is passed into the constructor
    AccountsRecyclerViewAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.accounts = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String account = accounts.get(position);
        holder.accountsTV.setText(account);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return accounts.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView accountsTV;

        ViewHolder(View itemView) {
            super(itemView);
            accountsTV = itemView.findViewById(R.id.accountTextView);
        }

    }
}
