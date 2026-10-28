package com.solari.denunciasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LIMIT = 5;
    private final List<Denuncia> datos = new ArrayList<>();
    private DenunciaAdapter adapter;
    private Button btnCargarMas;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.recycler);
        btnCargarMas = findViewById(R.id.btnCargarMas);
        Button btnNueva = findViewById(R.id.btnNueva);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        adapter = new DenunciaAdapter(datos, d -> {
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra("id", d.id);
            startActivity(i);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        btnCargarMas.setOnClickListener(v -> cargarPagina());
        btnNueva.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarActivity.class)));
        btnBuscar.setOnClickListener(v -> mostrarDialogoDni());
    }

    @Override
    protected void onResume() {
        super.onResume();
        reiniciar();
    }

    private void reiniciar() {
        datos.clear();
        adapter.notifyDataSetChanged();
        offset = 0;
        btnCargarMas.setVisibility(View.VISIBLE);
        cargarPagina();
    }

    private void cargarPagina() {
        ApiClient.getApi().listar(offset, LIMIT).enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    int inicio = datos.size();
                    datos.addAll(r.body());
                    adapter.notifyItemRangeInserted(inicio, r.body().size());
                    offset += LIMIT;
                    if (r.body().size() < LIMIT) {
                        btnCargarMas.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error " + r.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogoDni() {
        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("DNI (vacío = ver todas)");
        new AlertDialog.Builder(this)
                .setTitle("Buscar por DNI")
                .setView(et)
                .setPositiveButton("Buscar", (d, w) -> {
                    String dni = et.getText().toString().trim();
                    if (dni.isEmpty()) reiniciar();
                    else buscarPorDni(dni);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void buscarPorDni(String dni) {
        ApiClient.getApi().porDni(dni).enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    datos.clear();
                    datos.addAll(r.body());
                    adapter.notifyDataSetChanged();
                    btnCargarMas.setVisibility(View.GONE);
                    if (r.body().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Sin denuncias para ese DNI",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error " + r.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}