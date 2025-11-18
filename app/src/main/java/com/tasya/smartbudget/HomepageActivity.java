package com.tasya.smartbudget;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.SharedPreferences;
import java.util.List;
import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // === Ambil data user dari SharedPreferences ===
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        String username  = pref.getString("name", "User");
        String balance   = pref.getString("balance", "0"); // simpan angka saja

        // === Tampilkan nama ===
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(username);

        // === Wallet (saldo) ===
        TextView tvBalance = findViewById(R.id.tvBalance);
        ImageView btnEye = findViewById(R.id.btnEye);

        // saldo asli
        final String realBalance = formatRupiah(balance);
        final boolean[] isHidden = { false };

        tvBalance.setText(realBalance);

        // === CLICK: HIDE / SHOW ===
        btnEye.setOnClickListener(v -> {
            if (isHidden[0]) {
                // SHOW saldo
                tvBalance.setText(realBalance);
                btnEye.setImageResource(R.drawable.ic_eye_open);
                isHidden[0] = false;
            } else {
                // HIDE saldo
                tvBalance.setText("Rp••••••••");
                btnEye.setImageResource(R.drawable.ic_eye_close);
                isHidden[0] = true;
            }
        });

        // === RecyclerView Recent Transactions ===
        RecyclerView rv = findViewById(R.id.rvRecent);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);

        List<Transaction> demo = new ArrayList<>();
        demo.add(new Transaction(false, "10", "OCT 2025", "Food & Drink", "Rp50.000,00"));
        demo.add(new Transaction(true, "10", "OCT 2025", "Transfer Income", "Rp1.500.000,00"));

        TransactionAdapter adapter = new TransactionAdapter(demo);
        rv.setAdapter(adapter);
    }

    // === Formatter Rupiah otomatis ===
    private String formatRupiah(String angka) {
        try {
            double value = Double.parseDouble(angka);
            java.text.NumberFormat formatter =
                    java.text.NumberFormat.getInstance(new java.util.Locale("id", "ID"));
            return "Rp" + formatter.format(value) + ",00";
        } catch (Exception e) {
            return "Rp0,00";
        }
    }
}
