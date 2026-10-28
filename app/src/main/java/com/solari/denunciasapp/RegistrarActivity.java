package com.solari.denunciasapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        EditText etDni = findViewById(R.id.etDni);
        EditText etDireccion = findViewById(R.id.etDireccion);
        EditText etDescripcion = findViewById(R.id.etDescripcion);
        Spinner spTipo = findViewById(R.id.spTipo);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        spTipo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"ROBO", "ASALTO", "VANDALISMO", "OTRO"}));

        btnGuardar.setOnClickListener(v -> {
            String dni = etDni.getText().toString().trim();
            String dir = etDireccion.getText().toString().trim();
            String desc = etDescripcion.getText().toString().trim();

            if (dni.length() != 8 || dir.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos (DNI de 8 dígitos)",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Denuncia d = new Denuncia();
            d.dni = dni;
            d.tipo = spTipo.getSelectedItem().toString();
            d.direccion = dir;
            d.descripcion = desc;
            d.estado = 1;
            d.fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    .format(new Date());

            btnGuardar.setEnabled(false);
            ApiClient.getApi().registrar(d).enqueue(new Callback<Denuncia>() {
                @Override
                public void onResponse(Call<Denuncia> call, Response<Denuncia> r) {
                    if (r.isSuccessful()) {
                        Toast.makeText(RegistrarActivity.this, "Denuncia registrada",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        btnGuardar.setEnabled(true);
                        Toast.makeText(RegistrarActivity.this, "Error " + r.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Denuncia> call, Throwable t) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(RegistrarActivity.this, "Fallo: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}