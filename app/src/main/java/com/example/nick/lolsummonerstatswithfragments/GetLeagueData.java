package com.example.nick.lolsummonerstatswithfragments;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLeagueData extends AsyncTask<String,String,String> {

    private HttpURLConnection conn;
    Context context;
    String region;
    int response_code;
    Summoner searchedSummoner;
    String apiKey = BuildConfig.LEAGUE_OF_LEGENDS_API_KEY;
    TextView RANKED_FLEX_SR,RANKED_SOLO_5x5,RANKED_FLEX_TT;
    String ranked_flex_sr,ranked_solo_5v5,ranked_flex_tt;

    public GetLeagueData(Context context, Summoner searchedSummoner)
    {
        this.searchedSummoner = searchedSummoner;
        this.context = context;
    }

    protected String doInBackground(String... strings) {

        URL url;
        try {
            url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + String.valueOf(searchedSummoner.getId()) + "?api_key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("line",line);
                    result.append(line);
                }
                return (result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }
        return null;
    }

    protected void onPostExecute(String result)
    {
        if(result!=null)
        {
            try {
                JSONArray leagues = new JSONArray(result);
                for (int i = 0; i < leagues.length(); i++) {
                    JSONObject c = leagues.getJSONObject(i);
                    String tier = c.getString("tier");
                    String rank = c.getString("rank");
                    int points = c.getInt("leaguePoints");
                    String queue = c.getString("queueType");
                    String value = tier + " " + rank + "\n" + String.valueOf(points) + " lp";
                    if(queue.equals("RANKED_FLEX_SR")) ranked_flex_sr = value;
                    if(queue.equals("RANKED_SOLO_5x5")) ranked_solo_5v5 = value;
                    if(queue.equals("RANKED_FLEX_TT")) ranked_flex_tt = value;
                }
                //GetLastGames getLastGames = new GetLastGames(context,searchedSummoner,ranked_flex_sr,ranked_solo_5v5,ranked_flex_tt);
                //getLastGames.execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}