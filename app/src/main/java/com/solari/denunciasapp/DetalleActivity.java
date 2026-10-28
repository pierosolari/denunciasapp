package com.solari.denunciasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleActivity extends AppCompatActivity {

    private TextView tvDetalle;
    private Button btnAtender;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvDetalle = findViewById(R.id.tvDetalle);
        btnAtender = findViewById(R.id.btnAtender);
        id = getIntent().getIntExtra("id", -1);

        cargar();

        btnAtender.setOnClickListener(v -> {
            btnAtender.setEnabled(false);
            ApiClient.getApi().atender(id).enqueue(new Callback<Denuncia>() {
                @Override
                public void onResponse(Call<Denuncia> call, Response<Denuncia> r) {
                    if (r.isSuccessful()) {
                        Toast.makeText(DetalleActivity.this, "Denuncia atendida",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        btnAtender.setEnabled(true);
                        Toast.makeText(DetalleActivity.this, "Error " + r.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Denuncia> call, Throwable t) {
                    btnAtender.setEnabled(true);
                    Toast.makeText(DetalleActivity.this, "Fallo: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void cargar() {
        ApiClient.getApi().obtener(id).enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(Call<Denuncia> call, Response<Denuncia> r) {
                if (r.isSuccessful() && r.body() != null) {
                    Denuncia d = r.body();
                    tvDetalle.setText("Código: " + d.id
                            + "\nDNI: " + d.dni
                            + "\nFecha: " + d.fecha
                            + "\nTipo: " + d.tipo
                            + "\nDirección: " + d.direccion
                            + "\nDescripción: " + d.descripcion
                            + "\nEstado: " + (d.estado == 1 ? "Pendiente" : "Atendida"));
                    btnAtender.setEnabled(d.estado == 1);
                } else {
                    Toast.makeText(DetalleActivity.this, "Error " + r.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Denuncia> call, Throwable t) {
                Toast.makeText(DetalleActivity.this, "Fallo: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}