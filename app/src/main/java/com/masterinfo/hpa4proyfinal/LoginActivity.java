package com.masterinfo.hpa4proyfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "success";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private EditText txtUsuario, txtContrasena;
    private Dialog espera;

    private String usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUser);
        txtContrasena = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegistro = findViewById(R.id.btnRegistro);

        espera = new Dialog(LoginActivity.this);
        espera.setCancelable(false);
        espera.setContentView(R.layout.dialog_progress);

        btnLogin.setOnClickListener(view -> {
            usuario = txtUsuario.getText().toString().toLowerCase().trim();
            contrasena = txtContrasena.getText().toString().trim();
            if (validarInputs()) {
                login();
            }
        });

        btnRegistro.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }

    private void login() {
        espera.show();
        JSONObject peticion = new JSONObject();
        try {
            peticion.put(KEY_USERNAME, usuario);
            peticion.put(KEY_PASSWORD, contrasena);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String loginURL = "http://app.ggsxcloud.website/login.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, loginURL, peticion, response -> {
                    espera.dismiss();
                    try {
                        if (response.getInt(KEY_STATUS) == 1) {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(this, response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    espera.dismiss();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private boolean validarInputs() {
        if (KEY_EMPTY.equals(usuario)) {
            txtUsuario.setError("Campo obligatorio");
            txtUsuario.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(contrasena)) {
            txtContrasena.setError("Campo obligatorio");
            txtContrasena.requestFocus();
            return false;
        }
        return true;
    }

}