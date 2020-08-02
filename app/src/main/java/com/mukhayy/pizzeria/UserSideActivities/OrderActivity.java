package com.mukhayy.pizzeria.UserSideActivities;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mukhayy.pizzeria.Fragments.OrderPizzaFragment;
import com.mukhayy.pizzeria.R;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.filling)
    TextView filling;
    @BindView(R.id.pizzaPhoto)
    ImageView photo;
    @BindView(R.id.orderBtn)
    FloatingActionButton orderBtn;
    @BindView(R.id.infoLayout)
    LinearLayout infoLayout;

    OrderPizzaFragment orderPizzaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);

        final String pizzaName = getIntent().getStringExtra("name");
        final String pizzaPrice = getIntent().getStringExtra("price");
        String pizzaFilling = getIntent().getStringExtra("filling");
        final String pizzaPhoto = getIntent().getStringExtra("photo");

        //put pizza name to action bar title
        getSupportActionBar().setTitle(pizzaName);

        price.setText(pizzaPrice);
        filling.setText(pizzaFilling);
        Picasso.get()
                .load(pizzaPhoto)
                .placeholder(R.drawable.pizza_placeholder)
                .fit()
                .centerCrop()
                .into(photo);




        orderBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                 //make info layout which contains price and description of pizza invisible
                 infoLayout.setVisibility(View.GONE);
                 orderBtn.setVisibility(View.GONE);

                 orderPizzaFragment = new OrderPizzaFragment();
                 //send to fragment the name of the selected pizza
                 Bundle bundle = new Bundle();
                 bundle.putString("pizzaName",pizzaName);
                 bundle.putString("pizzaPrice",pizzaPrice);
                 bundle.putString("pizzaPhoto", pizzaPhoto);
                 orderPizzaFragment.setArguments(bundle);

                 //put order fragment into frame layout
                 getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.fragmentContainer, orderPizzaFragment)
                         .commit();

            }
        });

    }

}
