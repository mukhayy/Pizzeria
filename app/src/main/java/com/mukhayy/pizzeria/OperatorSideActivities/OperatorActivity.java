package com.mukhayy.pizzeria.OperatorSideActivities;


import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Adapters.AdapterOrders;
import com.mukhayy.pizzeria.Models.Orders;
import com.mukhayy.pizzeria.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperatorActivity extends AppCompatActivity {

    @BindView(R.id.passwordContainer)
    LinearLayout passwordContainer;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.passwordConf)
    EditText passwordConf;
    @BindView(R.id.acceptedListBtn)
    FloatingActionButton acceptedListBtn;
    @BindView(R.id.acceptedListsRecyclerView)
    RecyclerView acceptedListRecyclerView;
    @BindView(R.id.ordersList)
    RecyclerView ordersListRecyclerView;

    AdapterOrders adapterOrders, adapterforAccepted;

    DatabaseReference databaseReference;

    List<Orders> lists = new ArrayList<Orders>();



    //password for operator to login into operator UI
    final String password = "operatorPizzeria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        ButterKnife.bind(this);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check the valid password for operator
                 if (passwordConf.getText().toString().equals(password)){
                     passwordContainer.setVisibility(View.GONE);
                     ordersListRecyclerView.setVisibility(View.VISIBLE);

                     //read from database node /Orders, made orders
                     databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
                     databaseReference.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             for (DataSnapshot orders : dataSnapshot.getChildren()){

                                 //getting orders from Orders node and displaying it in a dedicated recyclerView
                                 final Orders ordersFromFireBase = orders.getValue(Orders.class);

                                 final String amount = ordersFromFireBase.getAmount();
                                 final String address = ordersFromFireBase.getAddress();
                                 final String phone = ordersFromFireBase.getPhone();
                                 final String name = ordersFromFireBase.getName();
                                 final String currentDateTime = ordersFromFireBase.getCurrentDateTime();
                                 final String pizzaName = ordersFromFireBase.getPizzaName();
                                 final String currentDateTimeAddress = ordersFromFireBase.getCurrentDateTimeAddress();
                                 final String pizzaPrice = ordersFromFireBase.getPrice();
                                 final String pizzaPhoto = ordersFromFireBase.getPhoto();
                                 final String id = ordersFromFireBase.getId();

                                 Orders ordersList = new Orders();
                                 ordersList.setAmount(amount);
                                 ordersList.setAddress(address);
                                 ordersList.setPhone(phone);
                                 ordersList.setName(name);
                                 ordersList.setCurrentDateTime(currentDateTime);
                                 ordersList.setPizzaName(pizzaName);

                                 lists.add(ordersList);

                                 adapterOrders = new AdapterOrders(lists, OperatorActivity.this);
                                 RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OperatorActivity.this);
                                 ordersListRecyclerView.setHasFixedSize(true);
                                 ordersListRecyclerView.setLayoutManager(layoutManager);
                                 ordersListRecyclerView.setAdapter(adapterOrders);

                                 //click listener for item
                                 adapterOrders.setListener(new AdapterOrders.clickListener() {
                                     @Override
                                     public void onClick(Orders orders) {

                                         //making custom alertDialog for options
                                         new iOSDialogBuilder(OperatorActivity.this)
                                                 .setTitle("Choose an option")
                                                 .setBoldPositiveLabel(true)
                                                 .setCancelable(false)

                                                 .setPositiveListener("Accept",new iOSDialogClickListener() {
                                                     @Override
                                                     public void onClick(iOSDialog dialog) {

                                                         //if accept have chosen first of all delete current order from Orders node in firebase
                                                         databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
                                                         Query ordersQuery = databaseReference.orderByChild("currentDataTimeAddress").equalTo(currentDateTimeAddress);
                                                         ordersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                 for (DataSnapshot order: dataSnapshot.getChildren()){
                                                                     //function call to delete order from orders
                                                                     removeFromFirebase(order);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                                             }
                                                         });


                                                         //make floating button visible
                                                         acceptedListBtn.setVisibility(View.VISIBLE);
                                                         //get reference to firebase node /Accepted
                                                         databaseReference = FirebaseDatabase.getInstance().getReference("Accepted");

                                                         Orders ordersAccepted = new Orders(pizzaName, amount, address, phone, name, currentDateTime,  currentDateTimeAddress, pizzaPrice, pizzaPhoto, id);
                                                         //push values to firebase node /Accepted
                                                         databaseReference.push().setValue(ordersAccepted);
                                                         //make custom info Toast, that it is accepted
                                                         TastyToast.makeText(OperatorActivity.this, "Accepted", Toast.LENGTH_LONG, TastyToast.INFO)
                                                                 .show();
                                                         dialog.dismiss();

                                                         //onClick for floating button
                                                         acceptedListBtn.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View view) {
                                                                 ordersListRecyclerView.setVisibility(View.GONE);
                                                                 acceptedListBtn.setVisibility(View.GONE);
                                                                 acceptedListRecyclerView.setVisibility(View.VISIBLE);
                                                                 //change actionBar title
                                                                 getSupportActionBar().setTitle("Accepted List");

                                                                  //get reference to accepted node in firebase
                                                                  databaseReference = FirebaseDatabase.getInstance().getReference("Accepted");
                                                                  databaseReference.addValueEventListener(new ValueEventListener() {
                                                                      @Override
                                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                          for (DataSnapshot orders : dataSnapshot.getChildren()) {

                                                                              //getting orders from accepted node and displaying it in a dedicated recyclerView
                                                                              final Orders acceptedOrdersFromFireBase = orders.getValue(Orders.class);
                                                                              final String amountAccepted = acceptedOrdersFromFireBase.getAmount();
                                                                              final String addressAccepted = acceptedOrdersFromFireBase.getAddress();
                                                                              final String phoneAccepted = acceptedOrdersFromFireBase.getPhone();
                                                                              final String nameAccepted = acceptedOrdersFromFireBase.getName();
                                                                              final String currentDateTimeAccepted = acceptedOrdersFromFireBase.getCurrentDateTime();
                                                                              final String pizzaNameAccepted = acceptedOrdersFromFireBase.getPizzaName();
                                                                              final String currentDateTimeAddressAccepted = acceptedOrdersFromFireBase.getCurrentDateTimeAddress();
                                                                              final String pizzaPriceAccepted = acceptedOrdersFromFireBase.getPrice();
                                                                              final String pizzaPhotoAccepted = acceptedOrdersFromFireBase.getPhoto();
                                                                              final String userid = acceptedOrdersFromFireBase.getId();



                                                                              Orders ordersList = new Orders();
                                                                              ordersList.setAmount(amountAccepted);
                                                                              ordersList.setAddress(addressAccepted);
                                                                              ordersList.setPhone(phoneAccepted);
                                                                              ordersList.setName(nameAccepted);
                                                                              ordersList.setCurrentDateTime(currentDateTimeAccepted);
                                                                              ordersList.setPizzaName(pizzaNameAccepted);

                                                                              lists.add(ordersList);

                                                                              adapterforAccepted = new AdapterOrders(lists, OperatorActivity.this);
                                                                              RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OperatorActivity.this);
                                                                              acceptedListRecyclerView.setHasFixedSize(true);
                                                                              acceptedListRecyclerView.setLayoutManager(layoutManager);
                                                                              acceptedListRecyclerView.setAdapter(adapterforAccepted);

                                                                              //click listener for accepted item to make it finished
                                                                              adapterforAccepted.setListener(new AdapterOrders.clickListener() {
                                                                              @Override
                                                                              public void onClick(Orders orders) {
                                                                                  new iOSDialogBuilder(OperatorActivity.this)

                                                                                          .setTitle("Click if order is finished")
                                                                                          .setBoldPositiveLabel(true)
                                                                                          .setCancelable(false)
                                                                                          .setPositiveListener("Order Finished",new iOSDialogClickListener() {
                                                                                              @Override
                                                                                              public void onClick(iOSDialog dialog) {
                                                                                                  //get reference to firebase node /Finished
                                                                                                  databaseReference = FirebaseDatabase.getInstance().getReference("Finished");

                                                                                                  Orders ordersFinished = new Orders(pizzaNameAccepted, amountAccepted, addressAccepted, phoneAccepted, nameAccepted, currentDateTimeAccepted,  currentDateTimeAddressAccepted, pizzaPriceAccepted, pizzaPhotoAccepted, userid);
                                                                                                  //push values to firebase node /Finished
                                                                                                  databaseReference.push().setValue(ordersFinished);

                                                                                                  //as accepted order set to finished, it is deleted from accepted node in firebase
                                                                                                  databaseReference = FirebaseDatabase.getInstance().getReference("Accepted");
                                                                                                  //unique id is "currentDateTime + address" which is string
                                                                                                  Query acceptedQuery = databaseReference.orderByChild("currentDateTimeAddress").equalTo(currentDateTimeAddressAccepted);
                                                                                                  acceptedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                      @Override
                                                                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                          for (DataSnapshot finished: dataSnapshot.getChildren()){
                                                                                                              //delete queried value
                                                                                                              removeFromFirebase(finished);
                                                                                                          }
                                                                                                      }

                                                                                                      @Override
                                                                                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                      }
                                                                                                  });

                                                                                                  dialog.dismiss();
                                                                                                  //make custom Toast to inform that accepted order is finished
                                                                                                  TastyToast.makeText(OperatorActivity.this, "OrderFinished", TastyToast.LENGTH_LONG, TastyToast.DEFAULT).show();
                                                                                              }
                                                                                          })
                                                                                          .build().show();

                                                                              }
                                                                          });
                                                                      }
                                                                      }

                                                                      @Override
                                                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                      }
                                                                  });

                                                             }
                                                         });

                                                     }
                                                 })
                                                 .setNegativeListener("Reject", new iOSDialogClickListener() {
                                                     @Override
                                                     public void onClick(iOSDialog dialog) {
                                                         //if reject is pressed in custom alert dialog put order to Rejected node in firebase
                                                         databaseReference = FirebaseDatabase.getInstance().getReference("Rejected");

                                                         Orders ordersRejected = new Orders(pizzaName, amount, address, phone, name, currentDateTime,  currentDateTimeAddress, pizzaPrice, pizzaPhoto,id);
                                                         databaseReference.push().setValue(ordersRejected);

                                                         //as accepted order set to rejected, it is deleted from orders node in firebase
                                                         databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
                                                         //unique id is "currentDateTime + address" which is string
                                                         Query acceptedQuery = databaseReference.orderByChild("currentDateTimeAddress").equalTo(currentDateTimeAddress);
                                                         acceptedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                 for (DataSnapshot rejected: dataSnapshot.getChildren()){
                                                                     //delete queried value
                                                                     removeFromFirebase(rejected);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                                             }
                                                         });


                                                         TastyToast.makeText(OperatorActivity.this, "Rejected", Toast.LENGTH_LONG, TastyToast.INFO)
                                                                 .show();
                                                         dialog.dismiss();
                                                     }
                                                 })
                                                 .build().show();
                                     }
                                 });
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                 }else
                     //if operator password does not matches make custom Toast
                     TastyToast.makeText(view.getContext(), "InvalidPassword", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
            }
        });

    }

    private void removeFromFirebase(DataSnapshot order){
        order.getRef().removeValue();
    }
}
