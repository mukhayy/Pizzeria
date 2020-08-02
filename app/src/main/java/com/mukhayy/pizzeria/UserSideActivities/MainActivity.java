package com.mukhayy.pizzeria.UserSideActivities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mukhayy.pizzeria.Fragments.FavouriteFragment;
import com.mukhayy.pizzeria.Fragments.MenuFragment;
import com.mukhayy.pizzeria.Fragments.ProfileFragment;
import com.mukhayy.pizzeria.R;
import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (this.getIntent().getStringExtra("orderName") != null && !this.getIntent().getStringExtra("orderName").isEmpty()) {
            //make custom Toast when orderPizza fragment makes order and comes back
             TastyToast.makeText(this, "Ваш заказ " + this.getIntent().getExtras().getString("orderName") + " принят", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
        }


        //bottom navigation binding
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);  //original color

        loadFragment(new MenuFragment());
    }


    //loading fragments to frame layout by selected item
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()){
                case R.id.navigation_menu:
                    fragment = new MenuFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_favourite:
                    fragment = new FavouriteFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    //loading function which is called in onNavigationItemSelected
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
