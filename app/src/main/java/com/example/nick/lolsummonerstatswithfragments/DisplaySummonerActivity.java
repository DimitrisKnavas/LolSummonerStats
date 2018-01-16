package com.example.nick.lolsummonerstatswithfragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class DisplaySummonerActivity extends FragmentActivity {

    private StatsFragment statsFragment;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_summoner);

        Intent intent = getIntent();
        String selectedName = intent.getStringExtra("selectedName");
        String selectedServer = intent.getStringExtra("selectedServer");

        statsFragment = new StatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedName",selectedName);
        bundle.putString("selectedServer",selectedServer);
        statsFragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if(fragment == null)
        {
            ft.add(R.id.container, statsFragment);
        } else {
            ft.replace(R.id.container, statsFragment);
        }
        ft.addToBackStack(null);
        ft.commit();


    }

}
