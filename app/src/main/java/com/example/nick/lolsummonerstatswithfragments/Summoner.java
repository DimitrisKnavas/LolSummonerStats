package com.example.nick.lolsummonerstatswithfragments;

import android.util.Log;

import java.io.Serializable;

public class Summoner implements Serializable{

    private int profileIconId;
    private String name;
    private long summonerLevel;
    private long revisionDate;
    private long id;
    private long accountId;
    private String region;

    public Summoner(int profileIconId, String name, long summonerLevel, long revisionDate, long id, long accountId, String region)
    {
        this.profileIconId = profileIconId;
        this.name = name;
        this.summonerLevel = summonerLevel;
        this.revisionDate = revisionDate;
        this.id = id;
        this.accountId = accountId;
        this.region = region;
        Log.i("Summoner id: ", String.valueOf(this.id));
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getRegion() {
        return region;
    }

}
