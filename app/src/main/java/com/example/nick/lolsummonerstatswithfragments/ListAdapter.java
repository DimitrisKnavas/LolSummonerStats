package com.example.nick.lolsummonerstatswithfragments;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<SummonerGameStats> {

    private Summoner searchedSummoner;

    public ListAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, ArrayList<SummonerGameStats> items, Summoner searchedSummoner)
    {
        super(context, resource, items);
        this.searchedSummoner = searchedSummoner;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if(v == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.last_matches_list_item,null);
        }

        SummonerGameStats p = getItem(position);


        if(p != null)
        {
            TextView winOrLoseTextview = (TextView) v.findViewById(R.id .win_or_lose);
            TextView scoreTextview = (TextView) v.findViewById(R.id.score);
            TextView creepScoreTextview = (TextView) v.findViewById(R.id.creep_score);
            TextView goldTextview = (TextView) v.findViewById(R.id.gold);
            //TextView champTextview = (TextView) v.findViewById(R.id.champ_name);

            //GetChampionNameFromId getChampionNameFromId = new GetChampionNameFromId(v.getContext(),p,searchedSummoner);
            //getChampionNameFromId.execute(champTextview);

            if(winOrLoseTextview != null)
            {
                if(p.isWin())
                {
                    winOrLoseTextview.setText("Win");
                    winOrLoseTextview.setTextColor(Color.parseColor("#005b96"));
                }
                else
                {
                    winOrLoseTextview.setText("Lose");
                    winOrLoseTextview.setTextColor(Color.parseColor("#ff3c3d"));
                }
            }

            if(scoreTextview != null)
            {
                DisplayImage displayScoreImage = new DisplayImage(scoreTextview);
                displayScoreImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/score.png");
                scoreTextview.setText(String.valueOf(p.getKills()) + " / " + String.valueOf(p.getDeaths())
                        + " / " + String.valueOf(p.getAssists()));
            }

            if(creepScoreTextview != null)
            {
                DisplayImage displayScoreImage = new DisplayImage(creepScoreTextview);
                displayScoreImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/minion.png");
                creepScoreTextview.setText(String.valueOf(p.getTotalMinionsKilled()));
            }

            if(goldTextview != null)
            {
                DisplayImage displayGoldImage = new DisplayImage(goldTextview,v);
                displayGoldImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/gold.png");
                goldTextview.setText(String.valueOf(p.getGoldEarned()));
            }
        }
        return v;
    }
}
