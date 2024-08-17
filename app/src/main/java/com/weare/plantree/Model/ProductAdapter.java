package com.weare.plantree.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weare.plantree.Interface.onItemClickListner;
import com.weare.plantree.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ORDERMODEL> list = new ArrayList<>();
    Context context;

    public ProductAdapter(ArrayList<ORDERMODEL> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void searchFilter(ArrayList<ORDERMODEL> SearchList){
        this.list = SearchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders_layout_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ORDERMODEL modal = list.get(position);
        holder.user_name_admin.setText(modal.getName());
        holder.phone_number_admin.setText(modal.getPhone());
        holder.total_price_admin.setText(modal.getTotalamt());
        holder.adress.setText(modal.getAdress());
        holder.order_date_time_admin_new.setText(modal.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name_admin, phone_number_admin,total_price_admin,adress,order_date_time_admin_new;
        public ImageView productImage;
        public onItemClickListner mOnItemClickListner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name_admin = itemView.findViewById(R.id.user_name_admin);
            phone_number_admin = itemView.findViewById(R.id.phone_number_admin);
            total_price_admin = itemView.findViewById(R.id.total_price_admin);
            adress=itemView.findViewById(R.id.adress);
            order_date_time_admin_new = itemView.findViewById(R.id.order_date_time_admin_new);
        }

    }
}
