package io.example.peanutbutter.prototype;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ImageView discoverImageView = (ImageView)findViewById(R.id.discover_ImageView);
        discoverImageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        discoverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Upon clicking DISCOVER, goes to activity listing discover tabs.
                Intent i = new Intent(NavigationActivity.this,DiscoverActivity.class);
                startActivity(i);
            }
        });

        ImageView groupImageView = (ImageView)findViewById(R.id.plan_ImageView);
        groupImageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        groupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Upon clicking PLAN, goes to planning stage.
                Intent i = new Intent(NavigationActivity.this,PlanActivity.class);
                startActivity(i);
            }
        });

        ImageView soloImageView = (ImageView)findViewById(R.id.solo_ImageView);
        soloImageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        soloImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ImageView profileImageView = (ImageView)findViewById(R.id.profile_ImageView);
        profileImageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
