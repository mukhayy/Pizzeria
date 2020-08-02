package com.mukhayy.pizzeria.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Adapters.AdapterFavourites;
import com.mukhayy.pizzeria.Models.ModelFavourite;
import com.mukhayy.pizzeria.Models.PizzaList;
import com.mukhayy.pizzeria.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteFragment extends Fragment {

    @BindView(R.id.recyclerFavourite)
    RecyclerView recyclerView;

    private DatabaseReference databaseReference;
    private FirebaseUser user;

    List<PizzaList> pizzaLists = new ArrayList<>();

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);


            databaseReference = FirebaseDatabase.getInstance().getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            String email = user.getEmail();

            //querying using email in the path /favourites
            Query query = databaseReference.child("favourites").orderByChild("userid").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the /favourite node with all children with id email
                        for (DataSnapshot favourite : dataSnapshot.getChildren()) {

                             ModelFavourite favouriteModel = favourite.getValue(ModelFavourite.class);
                             final String foodId = favouriteModel.getFoodid();

                             //querying with obtained food id in the path /pizzas
                             Query query1 =  databaseReference.child("pizzas").orderByChild("id").equalTo(foodId);
                             query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     for (DataSnapshot favouriteList : dataSnapshot.getChildren()){
                                         PizzaList pizzas = favouriteList.getValue(PizzaList.class);
                                         PizzaList list = new PizzaList();

                                         String name = pizzas.getName();
                                         String price = pizzas.getPrice();
                                         String photo = pizzas.getPhoto();

                                         list.setName(name);
                                         list.setPrice(price);
                                         list.setPhoto(photo);

                                         pizzaLists.add(list);

                                         AdapterFavourites adapterFavourites = new AdapterFavourites(pizzaLists, getContext());
                                         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                         recyclerView.setLayoutManager(layoutManager);
                                         recyclerView.setItemAnimator( new DefaultItemAnimator());
                                         recyclerView.setAdapter(adapterFavourites);

                                         adapterFavourites.setListener(new AdapterFavourites.ClickListenerFavourite() {
                                             @Override
                                             public void onClick(PizzaList pizzaList) {

                                                 new iOSDialogBuilder(getContext())
                                                         .setTitle("Click to delete")
                                                         .setBoldPositiveLabel(true)
                                                         .setCancelable(false)
                                                         .setPositiveListener("No more favourite",new iOSDialogClickListener() {
                                                             @Override
                                                             public void onClick(iOSDialog dialog) {
                                                                 databaseReference = FirebaseDatabase.getInstance().getReference();
                                                                 Query query1 =  databaseReference.child("favourites").orderByChild("foodid").equalTo(foodId);
                                                                 query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                         for (DataSnapshot favourites : dataSnapshot.getChildren()){
                                                                             favourites.getRef().removeValue();
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                     }
                                                                 });

                                                                 dialog.dismiss();
                                                                 //make custom Toast to inform that accepted order is finished
                                                                 TastyToast.makeText(getContext(), "Deleted", TastyToast.LENGTH_LONG, TastyToast.DEFAULT).show();
                                                             }
                                                         })
                                                         .build().show();


                                             }
                                         });
                                     }
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {
                                     Log.w("query on pizzas path", "Failed to read value.", databaseError.toException());
                                 }
                             });

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("query on favourite path", "Failed to read value.", databaseError.toException());

                }
            });

        return view;
    }

}
