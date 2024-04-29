package com.example.scannercollege.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scannercollege.Domain.Product;
import com.example.scannercollege.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> itemList;
    private Context context;

    public ProductAdapter(List<Product> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_recyler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product item = itemList.get(position);
        holder.productName.setText(item.getName());
        holder.productPrice.setText("₹ "+item.getPrice());
        holder.productQuantity.setText(item.getQuantity());
        int subtotal=Integer.parseInt(item.getQuantity())* Integer.parseInt(item.getPrice());
        holder.productSubtotal.setText("₹ "+subtotal);
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        TextView productSubtotal;
        LinearLayout productDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.productName);
            productPrice=itemView.findViewById(R.id.price);
            productQuantity=itemView.findViewById(R.id.quantity);
            productSubtotal=itemView.findViewById(R.id.subtotal);
            productDelete=itemView.findViewById(R.id.DeleteButton);

        }

    }
    public void addOrUpdateProduct(Product newProduct) {
        boolean found = false;
        for (Product existingProduct : itemList) {
            if (existingProduct.getBarcode().equals(newProduct.getBarcode())) {
                // If barcode already exists, increase quantity and notify the change
                existingProduct.setQuantity((Integer.parseInt(existingProduct.getQuantity()) + Integer.parseInt(newProduct.getQuantity()))+"");
                notifyDataSetChanged(); // Notify RecyclerView that data has changed
                found = true;
                break;
            }
        }
        if (!found) {
            // If barcode doesn't exist, add the new product to the list and notify insertion
            itemList.add(newProduct);
            notifyItemInserted(itemList.size() - 1); // Notify RecyclerView of item insertion
        }
    }
}
