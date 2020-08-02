package com.mukhayy.pizzeria.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Adapters.AdapterStatus;
import com.mukhayy.pizzeria.Models.Orders;
import com.mukhayy.pizzeria.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StatusWaitingFragment extends Fragment {

    @BindView(R.id.waitingRecyclerView)
    RecyclerView waitingRecyclerView;

    DatabaseReference databaseReference;
    List<Orders> orders = new ArrayList<Orders>();

    AdapterStatus adapterStatus;

    FirebaseUser user;

    public StatusWaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_waiting, container, false);
        ButterKnife.bind(this, view);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String id = user.getEmail();

        Query query = databaseReference.child("Accepted").orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot waiting: dataSnapshot.getChildren()){

                    Orders waitingOrders = waiting.getValue(Orders.class);
                    final String amount = waitingOrders.getAmount();
                    final String currentDateTime = waitingOrders.getCurrentDateTime();
                    final String pizzaName = waitingOrders.getPizzaName();
                    final String pizzaPrice = waitingOrders.getPrice();
                    final String pizzaPhoto = waitingOrders.getPhoto();

                    Orders list = new Orders();
                    list.setPrice(pizzaPrice);
                    list.setPizzaName(pizzaName);
                    list.setCurrentDateTime(currentDateTime);
                    list.setAmount(amount);
                    list.setPhoto(pizzaPhoto);

                    orders.add(list);

                    adapterStatus = new AdapterStatus(orders, getContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    waitingRecyclerView.setHasFixedSize(true);
                    waitingRecyclerView.setLayoutManager(layoutManager);
                    waitingRecyclerView.setAdapter(adapterStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
