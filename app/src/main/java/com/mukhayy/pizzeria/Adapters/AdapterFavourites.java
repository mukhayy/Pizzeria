package com.mukhayy.pizzeria.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Models.PizzaList;
import com.mukhayy.pizzeria.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterFavourites extends RecyclerView.Adapter<AdapterFavourites.MyHolder>{

    List<PizzaList> pizzas;
    Context context;

    //make recycler view clickable
    private ClickListenerFavourite listener;


    public AdapterFavourites(List<PizzaList> pizzaLists, Context context) {
        this.pizzas = pizzaLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_menu, viewGroup,false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
          myHolder.pizzaName.setText(pizzas.get(position).getName());
          myHolder.pizzaPrice.setText(pizzas.get(position).getPrice());
          Picasso.get()
                  .load(pizzas.get(position).getPhoto())
                  .fit()
                  .placeholder(R.drawable.pizza_placeholder)
                  .centerCrop()
                  .into(myHolder.pizzaImage);

          myHolder.starButton.setVisibility(View.GONE);

        //click listener to whole item
        final PizzaList pizza = pizzas.get(position);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(pizza);

            }
        });

    }




    @Override
    public int getItemCount() {
        return pizzas.size();
    }


    public void setListener(ClickListenerFavourite listener) {
        this.listener = listener;
    }

    //interface for click to implement it in MenuFragment
    public interface ClickListenerFavourite{
        void onClick(PizzaList pizzaList);
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pizzaImage)
        ImageView pizzaImage;
        @BindView(R.id.pizzaName)
        TextView pizzaName;
        @BindView(R.id.pizzaPrice)
        TextView pizzaPrice;
        @BindView(R.id.favButton)
        ImageView starButton;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
