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

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.HolderData> {
    List<User> listData;
    LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public AdapterUser(Context context, List<User> listData, ItemClickListener itemClickListener){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtNama.setText(listData.get(position).getNama());
        holder.txtEmail.setText(listData.get(position).getEmail());
        if(listData.get(position).getRole().matches("1")){
            holder.txtRole.setText("Administrator");
        }else {
            holder.txtRole.setText("User");
        }
        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(listData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtNama,txtEmail,txtRole;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.tvrNamauser);
            txtEmail = itemView.findViewById(R.id.tvremailuser);
            txtRole = itemView.findViewById(R.id.tvrrole);
        }
    }
    public interface ItemClickListener{
        void onItemClick(User user);
    }
}
