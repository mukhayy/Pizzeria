package com.mukhayy.pizzeria.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Models.User;
import com.mukhayy.pizzeria.UserSideActivities.MainActivity;
import com.mukhayy.pizzeria.Models.Orders;
import com.mukhayy.pizzeria.R;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderPizzaFragment extends Fragment {

    @BindView(R.id.order_Btn)
    FloatingActionButton orderButon;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.address)
    EditText address;

    DatabaseReference databaseReference;
    Orders orders;

    FirebaseUser user;

    public OrderPizzaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_pizza, container, false);
        ButterKnife.bind(this, view);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String id= user.getEmail();

        //get Reference to User node in Firebase
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        //query by email equals to id
        Query query = databaseReference.orderByChild("email").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userEntity: dataSnapshot.getChildren()){
                    //get userName and userPhone from query result
                    User user_ = userEntity.getValue(User.class);
                    final String userName = user_.getName();
                    final String userPhone = user_.getPhone();

                    // taking current time when order is made
                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    //onClick for order button
                    orderButon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //getting from OrderActivity name, price, photo of the selected pizza
                            Bundle bundle = getArguments();
                            final String pizzaName = bundle.getString("pizzaName");
                            final String pizzaPrice = bundle.getString("pizzaPrice");
                            final String pizzaPhoto = bundle.getString("pizzaPhoto");

                            //getting data from editTexts
                            final String amountPizza = amount.getText().toString();
                            final String addressPizza = address.getText().toString();

                            //making unique id for order which "currentDataTime + address"
                            String currentDateTimeAddress = addressPizza+currentDateTimeString;

                            //put required error if any editText is empty
                            if(amountPizza.isEmpty()){
                                amount.setError("Required");
                                return;
                            }
                            if(addressPizza.isEmpty()){
                                address.setError("Required");
                                return;
                            }

                            //push to firebase node /orders
                            databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
                            orders = new Orders(pizzaName, amountPizza, addressPizza,  userPhone,userName, currentDateTimeString, currentDateTimeAddress, pizzaPrice, pizzaPhoto, id);
                            databaseReference.push().setValue(orders);

                            //send to OrderActivity name of the ordered pizza for making Toast to notify the user that order accepted
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("orderName", pizzaName);
                            startActivity(new Intent(getActivity(), MainActivity.class).putExtras(bundle1));

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
