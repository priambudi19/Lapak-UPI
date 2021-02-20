package com.tugasbesar.lapakupi.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tugasbesar.lapakupi.Config;
import com.tugasbesar.lapakupi.model.Barang;
import com.tugasbesar.lapakupi.R;

import java.util.ArrayList;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder> {
    ArrayList<Barang> listBarang;
    Class tujuan;

    public BarangAdapter(ArrayList<Barang> barangList, Class dituju) {
        this.listBarang = barangList;
        this.tujuan = dituju;
    }

    @NonNull
    @Override
    public BarangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarangAdapter.MyViewHolder holder, final int position) {
        holder.txtNama.setText(listBarang.get(position).getNama_barang());
        holder.txtHarga.setText(listBarang.get(position).getHarga());
        holder.txtDeskripsi.setText(listBarang.get(position).getDeskripsi());
        Glide.with(holder.itemView.getContext())
                .load(Config.IMAGE_URL+"foto_barang/"+listBarang.get(position).getFoto_barang())
                .apply(new RequestOptions().override(200, 300))
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.imgBarang);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), tujuan);
                intent.putExtra("idBarang", listBarang.get(position).getId_barang());
                intent.putExtra("idAkun", listBarang.get(position).getId_akun());
                intent.putExtra("namaBarang", listBarang.get(position).getNama_barang());
                intent.putExtra("harga", listBarang.get(position).getHarga());
                intent.putExtra("id_kategori", listBarang.get(position).getId_kategori());
                intent.putExtra("deskripsi", listBarang.get(position).getDeskripsi());
                intent.putExtra("tanggal", listBarang.get(position).getTgl_dibuat());
                intent.putExtra("fotoBarang", listBarang.get(position).getFoto_barang());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNama, txtHarga,txtDeskripsi;
        public ImageView imgBarang;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBarang = (ImageView) itemView.findViewById(R.id.img_barang);
            txtNama = (TextView) itemView.findViewById(R.id.txt_namabarang);
            txtHarga = (TextView) itemView.findViewById(R.id.txt_hargabarang);
            txtDeskripsi = (TextView) itemView.findViewById(R.id.txt_deskripsibarang);
        }
    }
}
