package com.example.dognutrition;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Cart_Item_Adapter extends RecyclerView.Adapter<Cart_Item_Adapter.CartViewHolder> {

    private Context context;
    private List<Cart_Item> cartItemList;
    private DatabaseReference cartDatabaseReference;

    public Cart_Item_Adapter(Context context, List<Cart_Item> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList != null ? cartItemList : new ArrayList<>();
        this.cartDatabaseReference = FirebaseDatabase.getInstance().getReference("carts");
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position >= 0 && position < cartItemList.size()) {
            Cart_Item cartItem = cartItemList.get(position);

            holder.nameTextView.setText(cartItem.getProductName());
            holder.descriptionTextView.setText(cartItem.getProductDescription());
            holder.priceTextView.setText(String.format("$%.2f", cartItem.getProductPrice()));
            holder.quantityTextView.setText(String.format("Quantity: %d", cartItem.getQuantity()));

            Picasso.get()
                    .load(cartItem.getProductImageUrl())
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(holder.imageView);

            holder.removeButton.setOnClickListener(v -> removeFromCart(cartItem.getId(), position));
            holder.updateQuantityButton.setOnClickListener(v -> showUpdateQuantityDialog(cartItem));
        } else {
            Log.e("CartItemAdapter", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    private void removeFromCart(String cartItemId, int position) {
        if (position >= 0 && position < cartItemList.size()) {
            cartDatabaseReference.child(cartItemId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        if (position >= 0 && position < cartItemList.size()) { // Check again after async call
                            cartItemList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, cartItemList.size());
                            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("CartItemAdapter", "Invalid position after removal: " + position);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CartItemAdapter", "Failed to remove item from cart", e);
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("CartItemAdapter", "Invalid position: " + position);
        }
    }

    private void showUpdateQuantityDialog(Cart_Item cartItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_quantity, null);
        builder.setView(dialogView);

        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        AlertDialog dialog = builder.create();

        updateButton.setOnClickListener(v -> {
            String quantityString = quantityEditText.getText().toString().trim();
            if (!quantityString.isEmpty()) {
                int newQuantity = Integer.parseInt(quantityString);
                if (newQuantity > 0) {
                    updateCartItemQuantity(cartItem.getId(), newQuantity);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Quantity must be greater than zero", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void updateCartItemQuantity(String cartItemId, int newQuantity) {
        cartDatabaseReference.child(cartItemId).child("quantity").setValue(newQuantity)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("CartItemAdapter", "Failed to update quantity", e);
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button removeButton;
        Button updateQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cartProductImageView);
            nameTextView = itemView.findViewById(R.id.cartProductNameTextView);
            descriptionTextView = itemView.findViewById(R.id.cartProductDescriptionTextView);
            priceTextView = itemView.findViewById(R.id.cartProductPriceTextView);
            quantityTextView = itemView.findViewById(R.id.cartProductQuantityTextView);
            removeButton = itemView.findViewById(R.id.cartRemoveButton);
            updateQuantityButton = itemView.findViewById(R.id.cartUpdateQuantityButton);
        }
    }

    // Method to update the cart items list and notify the adapter
    public void updateCartItems(List<Cart_Item> newCartItems) {
        this.cartItemList = newCartItems != null ? newCartItems : new ArrayList<>();
        notifyDataSetChanged();
    }
}
