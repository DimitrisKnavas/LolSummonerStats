package com.example.nick.lolsummonerstatswithfragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
import java.util.ArrayList;
import java.util.zip.Inflater;

public class GetSummonerData extends AsyncTask<String,Integer,Summoner>{

    private Context context;
    private View rootView;
    private TextView status;
    private HttpURLConnection conn;
    private String region,ranked_flex_sr,ranked_solo_5v5,ranked_flex_tt;
    private ArrayList<Game> gamesList;
    private ArrayList<String> results;
    private ArrayList<SummonerGameStats> summonerGameStatsArrayList;
    private Summoner searchedSummoner;
    private int response_code;
    private String apiKey = BuildConfig.LEAGUE_OF_LEGENDS_API_KEY;



    public GetSummonerData(Context context,View rootView, Summoner searchedSummoner)
    {
        this.context = context;
        this.searchedSummoner = searchedSummoner;
        this.rootView = rootView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Summoner doInBackground(String... strings) {

        status = (TextView) rootView.findViewById(R.id.search_status);

        URL url;
        region = strings[1];
        try {
            //////////////////////////////// GET SUMMONER DATA

            status.setText(R.string.searching_summoner);
            url = new URL("https://" + region + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + strings[0] + "?api_key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
            response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder summonerData = new StringBuilder();
                String line;
                line = reader.readLine();
                summonerData.append(line);
                JsonElement jElement = new JsonParser().parse(summonerData.toString());
                JsonObject json_data = jElement.getAsJsonObject();
                int profileIconId = json_data.get("profileIconId").getAsInt();
                String name = json_data.get("name").getAsString();
                long summonerLevel = json_data.get("summonerLevel").getAsLong();
                long revisionDate = json_data.get("revisionDate").getAsLong();
                long id = json_data.get("id").getAsLong();
                long accountId = json_data.get("accountId").getAsLong();
                searchedSummoner = new Summoner(profileIconId, name, summonerLevel, revisionDate, id, accountId, region);
                Log.i("id", String.valueOf(searchedSummoner.getAccountId()));
            }
            conn.disconnect();

            //////////////////////////////// GET LEAGUE DATA

            url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + String.valueOf(searchedSummoner.getId()) + "?api_key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
            response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder leagueData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("line", line);
                    leagueData.append(line);
                }
                JSONArray leagues = new JSONArray(String.valueOf(leagueData));
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
            }
            conn.disconnect();

            //////////////////////////////// GET LAST GAMES

            url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/match/v3/matchlists/by-account/" + String.valueOf(searchedSummoner.getAccountId()) + "/recent?api_key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
            response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder lastGamesData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("line",line);
                    lastGamesData.append(line);
                }
                JSONObject jsonObject = new JSONObject(String.valueOf(lastGamesData));
                JSONArray matches = jsonObject.getJSONArray("matches");
                results = new ArrayList<>();
                gamesList = new ArrayList<>();
                for(int i=0; i<matches.length(); i++)
                {
                    JSONObject match = matches.getJSONObject(i);
                    String lane = match.getString("lane");
                    long gameId = match.getLong("gameId");
                    int champion = match.getInt("champion");
                    String platformId = match.getString("platformId");
                    int season = match.getInt("season");
                    int queue = match.getInt("queue");
                    String role = match.getString("role");
                    long timestamp = match.getLong("timestamp");
                    Game aGame = new Game(lane,gameId,champion,platformId,season,queue,role,timestamp);
                    gamesList.add(aGame);
                    Log.i("match",lane+" "+gameId+" "+champion+" "+season+" "+queue+" "+role);
                }
            }
            conn.disconnect();

            //////////////////////////////// GET DATA FOR EVERY GAME

            summonerGameStatsArrayList = new ArrayList<>();
            for(int i=0; i < gamesList.size(); i++) {
                onProgressUpdate();
                url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/match/v3/matches/" + gamesList.get(i).getGameId() + "?api_key=" + apiKey);
                conn = (HttpURLConnection) url.openConnection();
                response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i("line", line);
                        result.append(line);
                    }
                    results.add(String.valueOf(result));
                    int searchedSummonerIndex = -1;
                    JSONObject jsonObject = new JSONObject(results.get(i));
                    JSONArray participantIdentities = jsonObject.getJSONArray("participantIdentities");
                    for(int j=0; j<10;j++)
                    {
                        JSONObject participant = participantIdentities.getJSONObject(j);
                        JSONObject player = participant.getJSONObject("player");
                        String summonerName = player.getString("summonerName");
                        if(searchedSummoner.getName().equals(summonerName))
                        {
                            searchedSummonerIndex = participant.getInt("participantId");
                        }
                    }
                    JSONArray participants = jsonObject.getJSONArray("participants");
                    if(searchedSummonerIndex != -1)
                    {
                        JSONObject participantInfo = participants.getJSONObject(searchedSummonerIndex - 1);
                        JSONObject stats = participantInfo.getJSONObject("stats");
                        boolean win = stats.getBoolean("win");
                        int kills = stats.getInt("kills");
                        int deaths = stats.getInt("deaths");
                        int assists = stats.getInt("assists");
                        int totalMinionsKilled = stats.getInt("totalMinionsKilled");
                        int goldEarned = stats.getInt("goldEarned");

                        SummonerGameStats gameStats = new SummonerGameStats(win,kills,deaths,assists,totalMinionsKilled,goldEarned,gamesList.get(i).getChampion(),gamesList.get(i).getGameId());
                        summonerGameStatsArrayList.add(gameStats);
                    }
                }
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Summoner summoner) {
        super.onPostExecute(summoner);
    }

    public Summoner getTheSummoner()
    {
        return searchedSummoner;
    }
}
