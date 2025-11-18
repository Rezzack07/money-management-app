package com.tasya.smartbudget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transaction t = list.get(position);

        // DATE
        holder.tvDate.setText(t.day);
        holder.tvMonth.setText(t.month);

        // CATEGORY
        holder.tvCategory.setText(t.category);

        // TYPE
        holder.tvType.setText(t.isIncome ? "Income" : "Spend");

        // AMOUNT color
        holder.tvAmount.setText(t.amount);
        if (t.isIncome) {
            holder.tvAmount.setTextColor(0xFF2E7D32); // Green
            holder.imgStatus.setImageResource(R.drawable.ic_income);
        } else {
            holder.tvAmount.setTextColor(0xFFC62828); // Red
            holder.imgStatus.setImageResource(R.drawable.ic_spend);
        }

        // BIG ICON (kategori)
        if (t.category.equalsIgnoreCase("Food & Drink")) {
            holder.imgIcon.setImageResource(R.drawable.ic_food);
        } else if (t.category.contains("Transfer")) {
            holder.imgIcon.setImageResource(R.drawable.ic_money);
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_category_default);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon, imgStatus;
        TextView tvDate, tvMonth, tvType, tvCategory, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            imgStatus = itemView.findViewById(R.id.imgStatus);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);

            tvType = itemView.findViewById(R.id.tvType);
            tvCategory = itemView.findViewById(R.id.tvCategory);

            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
