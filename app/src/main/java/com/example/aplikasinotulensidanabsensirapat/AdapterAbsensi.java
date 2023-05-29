package com.example.aplikasinotulensidanabsensirapat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAbsensi extends RecyclerView.Adapter<AdapterAbsensi.HolderData> {
    List<Absensi> listData;
    LayoutInflater inflater;

    public AdapterAbsensi(Context context, List<Absensi> listData){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.absensi_item,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtNama.setText(listData.get(position).getNama());
        holder.txtwaktu.setText(listData.get(position).getWaktu());
        holder.txtNip.setText(listData.get(position).getNip());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtNama,txtwaktu,txtNip;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.tvrNamaabsn);
            txtwaktu = itemView.findViewById(R.id.tvrwaktuabsn);
            txtNip = itemView.findViewById(R.id.tvrNip);
        }
    }
}
