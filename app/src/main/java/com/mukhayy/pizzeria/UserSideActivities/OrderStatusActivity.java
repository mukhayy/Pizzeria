package com.mukhayy.pizzeria.UserSideActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.mukhayy.pizzeria.Fragments.StatusFinishedFragment;
import com.mukhayy.pizzeria.Fragments.StatusRejectedFragment;
import com.mukhayy.pizzeria.Fragments.StatusWaitingFragment;
import com.mukhayy.pizzeria.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends AppCompatActivity {

    @BindView(R.id.waiting)
    Button waiting;
    @BindView(R.id.finished)
    Button finished;
    @BindView(R.id.rejected)
    Button rejected;

    StatusWaitingFragment statusWaitingFragment;
    StatusFinishedFragment statusFinishedFragment;
    StatusRejectedFragment statusRejectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        ButterKnife.bind(this);



        statusWaitingFragment = new StatusWaitingFragment();
        //put waiting fragment into frame layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.statusContainer, statusWaitingFragment)
                .commit();

        //listener which button is clicked
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){

                    case R.id.waiting:
                        statusWaitingFragment = new StatusWaitingFragment();
                        //put waiting fragment into frame layout
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.statusContainer, statusWaitingFragment)
                                .commit();
                        break;
                    case R.id.finished:
                        statusFinishedFragment = new StatusFinishedFragment();
                        //put finished fragment into frame layout
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.statusContainer, statusFinishedFragment)
                                .commit();
                        break;
                    case R.id.rejected:
                        statusRejectedFragment = new StatusRejectedFragment();
                        //put rejected fragment into frame layout
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.statusContainer, statusRejectedFragment)
                                .commit();
                        break;

                }
            }
        };
        waiting.setOnClickListener(clickListener);
        finished.setOnClickListener(clickListener);
        rejected.setOnClickListener(clickListener);


    }
}
