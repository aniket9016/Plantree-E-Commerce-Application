package com.weare.plantree;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.LoudnessEnhancer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.weare.plantree.Buyers.ProductDetailsActivity;
import com.weare.plantree.Buyers.SearchProductActivity;
import com.weare.plantree.Interface.onItemClickListner;
import com.weare.plantree.Model.ProductsModal;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ProductsModal> list = new ArrayList<>();
    Context context;

    public ProductAdapter(ArrayList<ProductsModal> list,Context context){
        this.list = list;
        this.context = context;
    }

    public void searchFilter(ArrayList<ProductsModal> SearchList){
        this.list = SearchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductsModal modal = list.get(position);
        holder.productTitle.setText(modal.getPname());
        holder.productDescription.setText(modal.getDescription());
        holder.productPrice.setText(String.format("Price in Rs.%s ", modal.getPrice()));

        Picasso.get().load(modal.getImage()).into(holder.productImage);

        holder.itemView.setOnClickListener(view->{
            String id = modal.getPid();
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("pid", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productTitle, productDescription,productPrice;
        public ImageView productImage;
        public onItemClickListner mOnItemClickListner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_name_rv);
            productDescription = itemView.findViewById(R.id.product_description_rv);
            productImage = itemView.findViewById(R.id.product_image_rv);
            productPrice=itemView.findViewById(R.id.product_price_rv);
        }

    }
}
