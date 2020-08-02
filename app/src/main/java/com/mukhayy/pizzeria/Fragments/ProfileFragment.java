package com.mukhayy.pizzeria.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukhayy.pizzeria.Models.User;
import com.mukhayy.pizzeria.R;
import com.mukhayy.pizzeria.UserSideActivities.OrderStatusActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    @BindView(R.id.fullname)
    TextView fullname;
    @BindView(R.id.phone)
    TextView phoneEd;
    @BindView(R.id.orderLayout)
    LinearLayout orderLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    DatabaseReference databaseReference;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        //getting data from SharedPreferences which saved in RegisterActivity
        //SharedPreferences preferences = getContext().getSharedPreferences("preferences", 0);
        //final String saved_name = preferences.getString("saved_name", "missing");
        //final String saved_phoneNumber = preferences.getString("saved_phoneNumber", "missing");

        //get current authorized user
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        //get reference to /User in firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        //find email which is equal to current user email
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userEntity: dataSnapshot.getChildren()){

                    User user = userEntity.getValue(User.class);
                    final String name = user.getName();
                    final String phone = user.getPhone();
                    //final String email = user.getEmail();

                    fullname.setText(name);
                    phoneEd.setText(phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderStatusActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
