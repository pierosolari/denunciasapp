package com.solari.denunciasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DenunciaAdapter extends RecyclerView.Adapter<DenunciaAdapter.VH> {

    public interface OnItemClick {
        void onClick(Denuncia d);
    }

    private final List<Denuncia> lista;
    private final OnItemClick listener;

    public DenunciaAdapter(List<Denuncia> lista, OnItemClick listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_denuncia, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Denuncia d = lista.get(position);
        h.tvTipo.setText(d.tipo + " - DNI " + d.dni);
        h.tvDireccion.setText(d.direccion);
        h.tvEstado.setText(d.estado == 1 ? "Pendiente" : "Atendida");
        h.itemView.setOnClickListener(v -> listener.onClick(d));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTipo, tvDireccion, tvEstado;

        VH(View v) {
            super(v);
            tvTipo = v.findViewById(R.id.tvTipo);
            tvDireccion = v.findViewById(R.id.tvDireccion);
            tvEstado = v.findViewById(R.id.tvEstado);
        }
    }
}