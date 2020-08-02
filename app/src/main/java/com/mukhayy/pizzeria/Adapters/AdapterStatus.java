package com.mukhayy.pizzeria.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukhayy.pizzeria.Models.Orders;
import com.mukhayy.pizzeria.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterStatus extends RecyclerView.Adapter<AdapterStatus.StatusViewHolder> {

    List<Orders> ordersStatus;
    Context context;

    public AdapterStatus(List<Orders> orders, Context context) {
        this.ordersStatus = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //inflate layout with cardView
        View view = inflater.inflate(R.layout.card_view_order_status, viewGroup, false);

        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int position) {

        statusViewHolder.statusPizzaName.setText(ordersStatus.get(position).getPizzaName());
        statusViewHolder.statusPizzaAmount.setText(ordersStatus.get(position).getAmount());
        statusViewHolder.statusPizzaDate.setText(ordersStatus.get(position).getCurrentDateTime());
        Picasso.get().load(ordersStatus.get(position).getPhoto())
                .placeholder(R.drawable.pizza_placeholder) //placeholder when loading
                .fit()
                .centerCrop()
                .into(statusViewHolder.pizzaImageStatus);
        statusViewHolder.statusPizzaPrice.setText(ordersStatus.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return ordersStatus.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.statusPizzaName)
        TextView statusPizzaName;
        @BindView(R.id.statusPizzaPrice)
        TextView statusPizzaPrice;
        @BindView(R.id.statusPizzaAmount)
        TextView statusPizzaAmount;
        @BindView(R.id.statusPizzaDate)
        TextView statusPizzaDate;
        @BindView(R.id.pizzaImageStatus)
        ImageView pizzaImageStatus;



        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
