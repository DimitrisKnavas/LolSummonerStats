package com.example.nick.lolsummonerstatswithfragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class StatsFragment extends Fragment {

    private ListAdapter listAdapter;
    private ListView listView;
    private Summoner searchedSummoner;
    private View rootView;
    private String selectedName,selectedServer;
    private TextView status;
    private ArrayList<SummonerGameStats> lastGames;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.stats_view, container, false);
        selectedName = getArguments().getString("selectedName");
        selectedServer = getArguments().getString("selectedServer");
        status = (TextView) rootView.findViewById(R.id.search_status);

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        searchSummoner();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void searchSummoner()
    {
        GetSummonerData summonerInfoTask = new GetSummonerData(getContext(),rootView,searchedSummoner,lastGames);
        summonerInfoTask.execute(selectedName,selectedServer);
    }


}
