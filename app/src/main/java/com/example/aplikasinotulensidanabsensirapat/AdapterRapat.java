package com.example.aplikasinotulensidanabsensirapat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRapat extends RecyclerView.Adapter<AdapterRapat.HolderData> {
    List<Rapat> listData;
    LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public AdapterRapat(Context context, List<Rapat> listData, ItemClickListener itemClickListener){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rapat_item,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtJudul.setText(listData.get(position).getNama_rapat());
        holder.txtwaktu.setText(listData.get(position).getWaktu_mulai());
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(listData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtJudul,txtwaktu;
        CardView cardView;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardrapat);
            txtJudul = itemView.findViewById(R.id.tvrjudul);
            txtwaktu = itemView.findViewById(R.id.tvrwaktu);
        }
    }
    public interface ItemClickListener{
        void onItemClick(Rapat rapat);
    }
}
