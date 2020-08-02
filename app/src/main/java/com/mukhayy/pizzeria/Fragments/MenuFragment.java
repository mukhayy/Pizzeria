package com.mukhayy.pizzeria.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.UserSideActivities.OrderActivity;
import com.mukhayy.pizzeria.Adapters.AdapterMenu;
import com.mukhayy.pizzeria.Models.PizzaList;
import com.mukhayy.pizzeria.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MenuFragment extends Fragment {

    Activity context;
    AdapterMenu adapterMenu;

    @BindView(R.id.recyclerMenu)
    RecyclerView recyclerMenu;

    private DatabaseReference databaseReference;
    List<PizzaList> lists = new ArrayList<PizzaList>();

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();

        databaseReference = FirebaseDatabase.getInstance().getReference("pizzas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    final PizzaList pizzas = dataSnapshot1.getValue(PizzaList.class);
                    PizzaList pizzaList = new PizzaList();

                    final String name = pizzas.getName();
                    final String price = pizzas.getPrice();
                    final String photo = pizzas.getPhoto();
                    final String filling = pizzas.getFilling();
                    final String id = pizzas.getId();

                    pizzaList.setName(name);
                    pizzaList.setPrice(price);
                    pizzaList.setPhoto(photo);
                    pizzaList.setId(id);
                    pizzaList.setFilling(filling);

                    lists.add(pizzaList);

                    adapterMenu = new AdapterMenu(lists, getContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerMenu.setHasFixedSize(true);
                    recyclerMenu.setLayoutManager(layoutManager);
                    recyclerMenu.setAdapter(adapterMenu);

                    //click listener for item
                    adapterMenu.setListener(new AdapterMenu.ClickListener() {
                        @Override
                        public void onClick(PizzaList pizzaList) {
                            Intent intent = new Intent(context, OrderActivity.class);
                            intent.putExtra("name", pizzaList.getName());
                            intent.putExtra("photo", pizzaList.getPhoto());
                            intent.putExtra("price", pizzaList.getPrice());
                            intent.putExtra("filling", pizzaList.getFilling());
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }






}
