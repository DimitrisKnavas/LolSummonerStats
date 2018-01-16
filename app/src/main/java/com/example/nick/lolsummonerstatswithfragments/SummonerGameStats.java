package com.example.nick.lolsummonerstatswithfragments;

import java.io.Serializable;

/**
 * Created by nicka on 6/1/2018.
 */

public class SummonerGameStats implements Serializable {
    private boolean win;
    private int kills;
    private int deaths;
    private int assists;
    private int totalMinionsKilled;
    private int goldEarned;
    private int champion;
    private long gameId;
    private String championName;



    public SummonerGameStats(boolean win, int kills, int deaths, int assists, int totalMinionsKilled, int goldEarned, int champion, long gameId)
    {
        this.win = win;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.totalMinionsKilled = totalMinionsKilled;
        this.goldEarned = goldEarned;
        this.champion = champion;
        this.gameId = gameId;
    }

    public SummonerGameStats()
    {
        this.win = false;
        this.kills = -1;
        this.deaths = -1;
        this.assists = -1;
        this.totalMinionsKilled = -1;
        this.goldEarned = -1;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public int getChampion() {
        return champion;
    }

    public void setChampion(int champion) {
        this.champion = champion;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }
}
