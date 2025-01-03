package com.example.dognutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Orders_Adapter extends RecyclerView.Adapter<Orders_Adapter.OrderViewHolder> {

    private List<Order> orders;

    public Orders_Adapter(List<Order> orders) {
        // Ensure the orders list is not null
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (orders != null && !orders.isEmpty()) {
            Order order = orders.get(position);
            holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
            holder.orderStatusTextView.setText("Status: " + order.getStatus());
            holder.orderTotalTextView.setText("Total: $" + order.getTotalAmount());
            holder.orderPaymentMethodTextView.setText("Payment Method: " + order.getPaymentMethod());

            // Clear previous views
            holder.orderItemsLayout.removeAllViews();

            // Check if order items are not null before iterating
            if (order.getOrderItems() != null) {
                for (Cart_Item item : order.getOrderItems()) {
                    TextView productNameTextView = new TextView(holder.itemView.getContext());
                    productNameTextView.setText(item.getProductName());
                    holder.orderItemsLayout.addView(productNameTextView);
                }
            }
        } else {
            // Handle the case where the orders list is null or empty
            holder.orderIdTextView.setText("No orders available");
            holder.orderStatusTextView.setText("");
            holder.orderTotalTextView.setText("");
            holder.orderPaymentMethodTextView.setText("");
            holder.orderItemsLayout.removeAllViews();
        }
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderStatusTextView;
        TextView orderTotalTextView;
        TextView orderPaymentMethodTextView;
        LinearLayout orderItemsLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
            orderPaymentMethodTextView = itemView.findViewById(R.id.orderPaymentMethodTextView);
            orderItemsLayout = itemView.findViewById(R.id.orderItemsLayout);
        }
    }

    // Method to update the orders list and notify the adapter
    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders != null ? newOrders : new ArrayList<>();
        notifyDataSetChanged();
    }
}
