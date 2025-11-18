package com.tasya.smartbudget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    LinearLayout layoutBtnSignUp, layoutBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);

        layoutBtnSignUp = findViewById(R.id.layoutBtnSignUp);
        layoutBackLogin = findViewById(R.id.layoutBackLogin);

        layoutBackLogin.setOnClickListener(v -> finish());

        layoutBtnSignUp.setOnClickListener(v -> validate());
    }

    private void validate() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();
        String confirm = inputConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            inputName.setError("Name required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Format email salah");
            return;
        }

        if (pass.length() < 6) {
            inputPassword.setError("Minimal 6 karakter");
            return;
        }

        if (!pass.equals(confirm)) {
            inputConfirmPassword.setError("Password tidak sama");
            return;
        }

        sendToAPI(name, email, pass);
    }

    private void sendToAPI(String name, String email, String pass) {

        new Thread(() -> {

            try {
                URL url = new URL("http://10.0.2.2/smartbudget/users/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("name", name);
                body.put("email", email);
                body.put("password", pass);

                byte[] outputBytes = body.toString().getBytes("UTF-8");
                conn.setRequestProperty("Content-Length", String.valueOf(outputBytes.length));

                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(outputBytes);
                os.flush();
                os.close();

                int code = conn.getResponseCode();

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                code >= 400 ? conn.getErrorStream() : conn.getInputStream()
                        )
                );

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) sb.append(line);
                br.close();
                conn.disconnect();

                runOnUiThread(() -> handleResponse(code, sb.toString()));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Gagal koneksi server: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }

        }).start();
    }


    private void handleResponse(int code, String resp) {
        try {
            JSONObject json = new JSONObject(resp);

            if (json.getString("status").equals("success")) {

                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();

            } else {
                Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            runOnUiThread(() ->
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
            );
        }

    }}

