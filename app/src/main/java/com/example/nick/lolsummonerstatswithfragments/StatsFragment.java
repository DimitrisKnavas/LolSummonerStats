package com.example.nick.lolsummonerstatswithfragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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


public class StatsFragment extends Fragment {

    private ListAdapter listAdapter;
    private ListView listView;
    private Summoner searchedSummoner;
    private View rootView;
    private String selectedName,selectedServer;
    private TextView status;
    private ImageView scoreImageView,creepsImageView,goldImageView;
    private ArrayList<SummonerGameStats> lastGames;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.stats_view, container, false);
        selectedName = getArguments().getString("selectedName");
        selectedServer = getArguments().getString("selectedServer");
        status = (TextView) rootView.findViewById(R.id.search_status);
        scoreImageView = (ImageView) rootView.findViewById(R.id.score_icon);
        creepsImageView = (ImageView) rootView.findViewById(R.id.creeps_icon);
        goldImageView = (ImageView) rootView.findViewById(R.id.gold_icon);

        DisplayImage displayScoreImage = new DisplayImage(scoreImageView);
        displayScoreImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/score.png");

        DisplayImage displayCreepsImage = new DisplayImage(creepsImageView);
        displayCreepsImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/minion.png");

        DisplayImage displayGoldImage = new DisplayImage(goldImageView);
        displayGoldImage.execute("http://ddragon.leagueoflegends.com/cdn/5.5.1/img/ui/gold.png");

        listAdapter = new ListAdapter(getActivity(),R.layout.last_matches_list_item,new ArrayList<SummonerGameStats>());
        listView = rootView.findViewById(R.id.last_games_list);

        listView.setAdapter(listAdapter);

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
        GetSummonerData summonerInfoTask = new GetSummonerData();
        summonerInfoTask.execute();
    }

    class GetSummonerData extends AsyncTask<String,String,Summoner> {


        private HttpURLConnection conn;
        private String ranked_flex_sr,ranked_solo_5v5,ranked_flex_tt;
        private ArrayList<Game> gamesList;
        private ArrayList<String> results;
        private int response_code;
        private String apiKey = BuildConfig.LEAGUE_OF_LEGENDS_API_KEY;
        private TextView summName,summLevel,rankedFlexSR,rankedFlexTT,rankedSolo;
        private ImageView summonerImage;



        public GetSummonerData() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            summName = rootView.findViewById(R.id.summoner_name_text_view);
            summLevel = rootView.findViewById(R.id.summoner_level_text_view);
            rankedFlexSR = rootView.findViewById(R.id.RANKED_FLEX_SR);
            rankedFlexTT = rootView.findViewById(R.id.RANKED_FLEX_TT);
            rankedSolo = rootView.findViewById(R.id.RANKED_SOLO_5x5);
        }

        protected Summoner doInBackground(String... strings) {

            URL url;
            try {
                //////////////////////////////// GET SUMMONER DATA

                onProgressUpdate("Loading summoner data ...");
                url = new URL("https://" + selectedServer + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + selectedName + "?api_key=" + apiKey);
                conn = (HttpURLConnection) url.openConnection();
                response_code = conn.getResponseCode();
                Log.i("Response code",String.valueOf(response_code));
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
                    searchedSummoner = new Summoner(profileIconId, name, summonerLevel, revisionDate, id, accountId, selectedServer);
                    Log.i("id", String.valueOf(searchedSummoner.getAccountId()));
                }

                if(searchedSummoner == null)
                {
                    this.cancel(true);
                }
                else {
                    //////////////////////////////// GET LEAGUE DATA

                    onProgressUpdate("Loading league info ...");
                    url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + String.valueOf(searchedSummoner.getId()) + "?api_key=" + apiKey);
                    conn = (HttpURLConnection) url.openConnection();
                    response_code = conn.getResponseCode();
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder leagueData = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            //Log.i("line", line);
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
                            Log.i("league",value);
                            if (queue.equals("RANKED_FLEX_SR")) ranked_flex_sr = value;
                            if (queue.equals("RANKED_SOLO_5x5")) ranked_solo_5v5 = value;
                            if (queue.equals("RANKED_FLEX_TT")) ranked_flex_tt = value;
                        }
                    }


                    //////////////////////////////// GET LAST GAMES

                    onProgressUpdate("Loading last games ...");
                    url = new URL("https://" + searchedSummoner.getRegion() + ".api.riotgames.com/lol/match/v3/matchlists/by-account/" + String.valueOf(searchedSummoner.getAccountId()) + "/recent?api_key=" + apiKey);
                    conn = (HttpURLConnection) url.openConnection();
                    response_code = conn.getResponseCode();
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder lastGamesData = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Log.i("line", line);
                            lastGamesData.append(line);
                        }
                        JSONObject jsonObject = new JSONObject(String.valueOf(lastGamesData));
                        JSONArray matches = jsonObject.getJSONArray("matches");
                        results = new ArrayList<>();
                        gamesList = new ArrayList<>();
                        for (int i = 0; i < matches.length(); i++) {
                            JSONObject match = matches.getJSONObject(i);
                            String lane = match.getString("lane");
                            long gameId = match.getLong("gameId");
                            int champion = match.getInt("champion");
                            String platformId = match.getString("platformId");
                            int season = match.getInt("season");
                            int queue = match.getInt("queue");
                            String role = match.getString("role");
                            long timestamp = match.getLong("timestamp");
                            Game aGame = new Game(lane, gameId, champion, platformId, season, queue, role, timestamp);
                            gamesList.add(aGame);
                            Log.i("match", lane + " " + gameId + " " + champion + " " + season + " " + queue + " " + role);
                        }
                    }

                    //////////////////////////////// GET DATA FOR EVERY GAME

                    if(gamesList.size()>0)
                    {
                        onProgressUpdate("Loading game data ...");
                        lastGames = new ArrayList<>();
                        for (int i = 0; i < gamesList.size(); i++) {
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
                                for (int j = 0; j < 10; j++) {
                                    JSONObject participant = participantIdentities.getJSONObject(j);
                                    JSONObject player = participant.getJSONObject("player");
                                    String summonerName = player.getString("summonerName");
                                    if (searchedSummoner.getName().equals(summonerName)) {
                                        searchedSummonerIndex = participant.getInt("participantId");
                                    }
                                }
                                JSONArray participants = jsonObject.getJSONArray("participants");
                                if (searchedSummonerIndex != -1) {
                                    JSONObject participantInfo = participants.getJSONObject(searchedSummonerIndex - 1);
                                    JSONObject stats = participantInfo.getJSONObject("stats");
                                    boolean win = stats.getBoolean("win");
                                    int kills = stats.getInt("kills");
                                    int deaths = stats.getInt("deaths");
                                    int assists = stats.getInt("assists");
                                    int totalMinionsKilled = stats.getInt("totalMinionsKilled");
                                    int goldEarned = stats.getInt("goldEarned");

                                    SummonerGameStats gameStats = new SummonerGameStats(win, kills, deaths, assists, totalMinionsKilled, goldEarned, gamesList.get(i).getChampion(), gamesList.get(i).getGameId());
                                    lastGames.add(gameStats);
                                }
                            }
                        }
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            super.onPostExecute(summoner);

            if(lastGames.size() > 0)
            {
                listAdapter.clear();
                listAdapter.addAll(lastGames);
            }


            summName.setText(searchedSummoner.getName());
            summLevel.setText(String.valueOf(searchedSummoner.getSummonerLevel()) + " Level");
            rankedFlexSR.setText(ranked_flex_sr);
            rankedFlexTT.setText(ranked_flex_tt);
            rankedSolo.setText(ranked_solo_5v5);

            summonerImage = (ImageView) rootView.findViewById(R.id.summoner_icon);
            final String urlString = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + searchedSummoner.getProfileIconId() + ".png";
            DisplayImage displayImage = new DisplayImage(summonerImage);
            displayImage.execute(urlString);



            rootView.findViewById(R.id.progress_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.last_games_list).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.summoner_data_container).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            rootView.findViewById(R.id.progress_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.summoner_not_found).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            status.setText(values[0]);
        }
    }
}
