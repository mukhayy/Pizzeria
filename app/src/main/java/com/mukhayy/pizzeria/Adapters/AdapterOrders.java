package com.mukhayy.pizzeria.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mukhayy.pizzeria.Models.Orders;
import com.mukhayy.pizzeria.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.OrdersViewHolder>{


    List<Orders> orders;

    //make recycler view clickable
    private clickListener listener;

    Context context;

    public AdapterOrders(List<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;

    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //inflate layout with cardView
        View view = inflater.inflate(R.layout.card_view_orders, viewGroup, false);
        return new AdapterOrders.OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder ordersViewHolder, int position) {

        ordersViewHolder.orderName.setText(orders.get(position).getPizzaName());
        ordersViewHolder.orderAddress.setText(orders.get(position).getAddress());
        ordersViewHolder.orderAmount.setText(orders.get(position).getAmount());
        ordersViewHolder.orderTime.setText(orders.get(position).getCurrentDateTime());
        ordersViewHolder.userName.setText(orders.get(position).getName());
        ordersViewHolder.userPhone.setText(orders.get(position).getPhone());


        //click listener for whole item
        final Orders ordersList = orders.get(position);
        ordersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(ordersList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setListener(clickListener listener) {
        this.listener = listener;
    }

    //interface for click to implement it in Operator activity
    public interface clickListener{
        void onClick(Orders orders);
    }


    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderName)
        TextView orderName;
        @BindView(R.id.orderAmount)
        TextView orderAmount;
        @BindView(R.id.orderAddress)
        TextView orderAddress;
        @BindView(R.id.orderTime)
        TextView orderTime;
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userPhone)
        TextView userPhone;


        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
