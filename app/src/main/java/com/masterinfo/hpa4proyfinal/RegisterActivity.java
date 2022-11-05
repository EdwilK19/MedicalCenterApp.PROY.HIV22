package com.masterinfo.hpa4proyfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "success";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRSTNAME = "first_name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_ID = "identification";
    private static final String KEY_NATION = "nationality";
    private static final String KEY_BIRTHDAY = "birth";
    private static final String KEY_PHONE = "phone";

    private LinearLayout layoutCed;
    private EditText txtNombre, txtApellido, txtDateBirth, txtNacionalidad, txtTel, txtFolio, txtTomo,
            txtPasaporte, txtUsuario, txtContra, txtCorreo, txtContraConfirm;
    private Spinner spProvincia;
    private Dialog espera;

    private String nombre, apellido, fechaNacimiento, nacionalidad, telefono, tomo, folio, pasaporte,
            usuario, contrasena, confirmContra, correo, cedula;
    private Boolean swId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtDateBirth = findViewById(R.id.txtNacimiento);
        txtNacionalidad = findViewById(R.id.txtNacionalidad);
        txtTel = findViewById(R.id.txtTelefono);
        txtFolio = findViewById(R.id.txtFolioCed);
        txtTomo = findViewById(R.id.txtTomoCed);
        layoutCed = findViewById(R.id.layoutCed);
        txtPasaporte = findViewById(R.id.txtPasaporte);
        spProvincia = findViewById(R.id.spProvinciaCed);
        Spinner spIdentificacion = findViewById(R.id.spIdentificacion);
        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnSiguiente = findViewById(R.id.btnSiguiente);

        txtDateBirth.setOnClickListener(view -> {
            txtDateBirth.setError(null);
            mostrarDatePickerDialog();
        });

        ArrayAdapter<CharSequence> adapterId = ArrayAdapter.createFromResource(this,
                R.array.identificacion_array, android.R.layout.simple_spinner_item);
        adapterId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIdentificacion.setAdapter(adapterId);

        ArrayAdapter<CharSequence> adapterProvCed = ArrayAdapter.createFromResource(this,
                R.array.provincia_array, android.R.layout.simple_spinner_item);
        adapterProvCed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapterProvCed);

        spIdentificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    txtPasaporte.setVisibility(View.GONE);
                    layoutCed.setVisibility(View.VISIBLE);
                    swId = false;
                } else {
                    layoutCed.setVisibility(View.GONE);
                    txtPasaporte.setVisibility(View.VISIBLE);
                    swId = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnVolver.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        espera = new Dialog(RegisterActivity.this);
        espera.setCancelable(false);
        espera.setContentView(R.layout.dialog_progress);

        TextView mensajeEspera = espera.findViewById(R.id.txtAvisoEspera);
        mensajeEspera.setText(R.string.progress_register);

        btnSiguiente.setOnClickListener(view -> {
            nombre = txtNombre.getText().toString().trim();
            apellido = txtApellido.getText().toString().trim();
            fechaNacimiento = txtDateBirth.getText().toString();
            nacionalidad = txtNacionalidad.getText().toString().trim();
            telefono = txtTel.getText().toString().trim();
            if (!swId) {
                tomo = txtTomo.getText().toString().trim();
                folio = txtFolio.getText().toString().trim();
                if (validarInputs(1)) {
                    cedula = spProvincia.getSelectedItem().toString() + "-" + tomo + "-" + folio;
                    terminarRegistro();
                }
            } else {
                pasaporte = txtPasaporte.getText().toString().trim();
                if (validarInputs(2)) {
                    terminarRegistro();
                }
            }
        });

    }

    private void terminarRegistro() {
        setContentView(R.layout.activity_register2);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContra = findViewById(R.id.txtContra);
        txtContraConfirm = findViewById(R.id.txtContraConfirmar);
        Button btnRegresar = findViewById(R.id.btnRegresar);
        Button btnRegistro = findViewById(R.id.btnRegistrarse);

        btnRegresar.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        btnRegistro.setOnClickListener(view -> {
            usuario = txtUsuario.getText().toString().toLowerCase().trim();
            correo = txtCorreo.getText().toString().toLowerCase().trim();
            contrasena = txtContra.getText().toString().trim();
            confirmContra = txtContraConfirm.getText().toString().trim();
            if (validarInputs(3)) {
                registro();
            }
        });
    }

    private void registro() {
        espera.show();
        JSONObject peticion = new JSONObject();
        try {
            peticion.put(KEY_USERNAME, usuario);
            peticion.put(KEY_PASSWORD, contrasena);
            peticion.put(KEY_EMAIL, correo);
            peticion.put(KEY_FIRSTNAME, nombre);
            peticion.put(KEY_LASTNAME, apellido);
            if (!swId) peticion.put(KEY_ID, cedula);
            else peticion.put(KEY_ID, pasaporte);
            peticion.put(KEY_NATION, nacionalidad);
            peticion.put(KEY_BIRTHDAY, fechaNacimiento);
            peticion.put(KEY_PHONE, Integer.parseInt(telefono));
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
        }
        String registerURL = "http://app.ggsxcloud.website/register.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, registerURL, peticion, response -> {
                    espera.dismiss();
                    try {
                        int estado = response.getInt(KEY_STATUS);
                        switch(estado) {
                            case 0: {
                                txtUsuario.setError("Nombre de Usuario ya existente");
                                txtUsuario.requestFocus();
                                break;
                            }
                            case 1: {
                                Toast.makeText(this, response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                break;
                            }
                            case 2: {
                                txtCorreo.setError("Correo Electrónico ya existente");
                                txtCorreo.requestFocus();
                                break;
                            }
                            case 3: {
                                txtCorreo.setError("Correo Electrónico no válido");
                                txtCorreo.requestFocus();
                                break;
                            }
                            default: Toast.makeText(this, response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    espera.dismiss();
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void mostrarDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 porque Enero es cero
            final String fechaSeleccionada = year + "-" + (month+1) + "-" + day;
            txtDateBirth.setText(fechaSeleccionada);
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private boolean validarInputs(int opcion) {
        if (opcion == 3) {
            if (KEY_EMPTY.equals(usuario)) {
                txtUsuario.setError("Campo obligatorio");
                txtUsuario.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(correo)) {
                txtCorreo.setError("Campo obligatorio");
                txtCorreo.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(contrasena)) {
                txtContra.setError("Campo obligatorio");
                txtContra.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(confirmContra)) {
                txtContraConfirm.setError("Campo obligatorio");
                txtContraConfirm.requestFocus();
                return false;
            }
            if (!contrasena.equals(confirmContra)) {
                txtContraConfirm.setError("La confirmación de contraseña no coincide");
                txtContraConfirm.requestFocus();
                return false;
            }
        } else {
            if (KEY_EMPTY.equals(nombre)) {
                txtNombre.setError("Campo obligatorio");
                txtNombre.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(apellido)) {
                txtApellido.setError("Campo obligatorio");
                txtApellido.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(fechaNacimiento)) {
                txtDateBirth.setError("Campo obligatorio");
                return false;
            }
            if (KEY_EMPTY.equals(nacionalidad)) {
                txtNacionalidad.setError("Campo obligatorio");
                txtNacionalidad.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(telefono)) {
                txtTel.setError("Campo obligatorio");
                txtTel.requestFocus();
                return false;
            }
            if (opcion == 1) {
                if (KEY_EMPTY.equals(tomo)) {
                    txtTomo.setError("Campo obligatorio");
                    txtTomo.requestFocus();
                    return false;
                }
                if (KEY_EMPTY.equals(folio)) {
                    txtFolio.setError("Campo obligatorio");
                    txtFolio.requestFocus();
                    return false;
                }
            } else {
                if (KEY_EMPTY.equals(pasaporte)) {
                    txtPasaporte.setError("Campo obligatorio");
                    txtPasaporte.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

}