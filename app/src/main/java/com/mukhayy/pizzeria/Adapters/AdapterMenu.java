package com.mukhayy.pizzeria.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Models.ModelFavourite;
import com.mukhayy.pizzeria.Models.PizzaList;
import com.mukhayy.pizzeria.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MenuViewHolder> {

    private Context context;

    //model PizzaList
    private List<PizzaList> pizzas;

    //model ModelFavourite
    private ModelFavourite favourite;

    //make recycler view clickable
    private ClickListener listener;

    //firebase
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;


    public AdapterMenu(List<PizzaList> orders, Context context) {
        this.pizzas = orders;
        this.context = context;
        //get current auth. user
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //inflate layout with cardView
        View view = inflater.inflate(R.layout.card_view_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, final int position) {

        //placing photo of pizza to ImageView in the CardView through Picasso
        Picasso.get()
                .load(pizzas.get(position).getPhoto())
                .placeholder(R.drawable.pizza_placeholder) //placeholder when loading
                .fit()
                .centerCrop()
                .into(menuViewHolder.pizzaImage);


        menuViewHolder.pizzaName.setText(pizzas.get(position).getName());
        menuViewHolder.pizzaPrice.setText(pizzas.get(position).getPrice());
        menuViewHolder.pizzaFillings.setText(pizzas.get(position).getFilling());



        //click listener for starbutton
        menuViewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuViewHolder.favButton.setImageResource(R.drawable.like);
                //push to firebase node /favourites id and email
                final String foodId = pizzas.get(position).getId();
                favourite = new ModelFavourite(user.getEmail(),foodId);
                //get reference to node /favourites in firebase realtime database
                firebaseDatabase = FirebaseDatabase.getInstance().getReference("favourites");
                firebaseDatabase.push().setValue(favourite);

            }
        });

        //click listener to whole item
        final PizzaList pizza = pizzas.get(position);
        menuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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


    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    //interface for click to implement it in MenuFragment
    public interface ClickListener{
        void onClick(PizzaList pizzaList);
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pizzaImage)
        ImageView pizzaImage;
        @BindView(R.id.pizzaName)
        TextView pizzaName;
        @BindView(R.id.pizzaPrice)
        TextView pizzaPrice;
        @BindView(R.id.filings)
        TextView pizzaFillings;
        @BindView(R.id.favButton)
        ImageView favButton;

        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
