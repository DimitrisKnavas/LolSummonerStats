package com.example.nick.lolsummonerstatswithfragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nicka on 15/1/2018.
 */

public class StatsFragment extends Fragment {

    private Summoner searchedSummoner;
    private View rootView;
    private String selectedName,selectedServer;
    private TextView status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("test","test");
        rootView = inflater.inflate(R.layout.stats_view, container, false);
        selectedName = getArguments().getString("selectedName");
        selectedServer = getArguments().getString("selectedServer");
        status = (TextView) rootView.findViewById(R.id.search_status);

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetSummonerData summonerInfoTask = new GetSummonerData(getContext(),rootView,searchedSummoner);
        summonerInfoTask.execute(selectedName,selectedServer);

//        while(summonerInfoTask.getStatus() != AsyncTask.Status.FINISHED)
//        {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        searchedSummoner = summonerInfoTask.getTheSummoner();
//
    }
}
