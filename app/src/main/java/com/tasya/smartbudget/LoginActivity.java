package com.tasya.smartbudget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    LinearLayout btnLogin;
    TextView txtSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        txtSignup = findViewById(R.id.txt_signup);

        txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        btnLogin.setOnClickListener(v -> validate());
    }

    private void validate() {
        String email = inputEmail.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Format email salah");
            return;
        }

        if (pass.length() < 6) {
            inputPassword.setError("Minimal 6 karakter");
            return;
        }

        sendToAPI(email, pass);
    }

    private void sendToAPI(String email, String pass) {

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/smartbudget/users/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("email", email);
                body.put("password", pass);

                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(body.toString().getBytes());
                os.flush();
                os.close();

                int code = conn.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) sb.append(line);
                br.close();
                conn.disconnect();

                runOnUiThread(() -> handleResponse(code, sb.toString()));

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Gagal koneksi server", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void handleResponse(int code, String resp) {
        try {
            JSONObject json = new JSONObject(resp);

            if (json.getString("status").equals("success")) {

                JSONObject user = json.getJSONObject("user");

                // === SIMPAN DATA USER KE SHAREDPREFERENCES ===
                SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("id", user.getString("id"));
                editor.putString("name", user.getString("name"));
                editor.putString("email", user.getString("email"));

                // ⬇⬇⬇ TAMBAHKAN INI untuk menyimpan SALDO DARI API
                if (user.has("balance")) {
                    editor.putString("balance", user.getString("balance"));
                } else {
                    editor.putString("balance", "0");
                }
                // ⬆⬆⬆

                editor.apply();

                Toast.makeText(this,
                        "Login berhasil! Selamat datang " + user.getString("name"),
                        Toast.LENGTH_SHORT).show();

                // Pindah ke Homepage
                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this,
                    "Server error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }


}
