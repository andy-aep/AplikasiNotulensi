package com.example.aplikasinotulensidanabsensirapat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterNotulensi extends RecyclerView.Adapter<AdapterNotulensi.HolderData> {
    List<Notulensi> listData;
    LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public AdapterNotulensi(Context context, List<Notulensi> listData, ItemClickListener itemClickListener){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notulensi_item,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtPembahasan.setText(listData.get(position).getPembahasan());
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(listData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtPembahasan;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtPembahasan = itemView.findViewById(R.id.tvrPembahasan);
        }
    }
    public interface ItemClickListener{
        void onItemClick(Notulensi notulensi);
    }
}
