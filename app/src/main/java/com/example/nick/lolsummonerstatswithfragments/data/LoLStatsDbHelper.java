package com.example.nick.lolsummonerstatswithfragments.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoLStatsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "LoLStats.db";

    public LoLStatsDbHelper(Context context){
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_SERVER_TABLE = "CREATE TABLE IF NOT EXISTS " + LoLStatsContract.ServerEntry.TABLE_NAME + " ( "+ LoLStatsContract.ServerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LoLStatsContract.ServerEntry.COLUMN_SERVER_ID + " VARCHAR, " + LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME + " VARCHAR);";
        sqLiteDatabase.execSQL(CREATE_SERVER_TABLE);
        sqLiteDatabase.execSQL(insertToTable("ru","Russia"));
        sqLiteDatabase.execSQL(insertToTable("kr","Korea"));
        sqLiteDatabase.execSQL(insertToTable("br1","Brazil"));
        sqLiteDatabase.execSQL(insertToTable("oc1","Oceania"));
        sqLiteDatabase.execSQL(insertToTable("jp1","Japan"));
        sqLiteDatabase.execSQL(insertToTable("na1","North America"));
        sqLiteDatabase.execSQL(insertToTable("eun1","Europe Nordic & East"));
        sqLiteDatabase.execSQL(insertToTable("euw1","Europe West"));
        sqLiteDatabase.execSQL(insertToTable("tr","Turkey"));
        sqLiteDatabase.execSQL(insertToTable("la1","South America North"));
        sqLiteDatabase.execSQL(insertToTable("la2","South America South"));

        final String CREATE_SUMMONER_TABLE = "CREATE TABLE IF NOT EXISTS " + LoLStatsContract.SummonerEntry.TABLE_NAME + " ( " + LoLStatsContract.SummonerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME + " VARCHAR, " + LoLStatsContract.SummonerEntry.COLUMN_SERVER_ID + " VARCHAR);";
        sqLiteDatabase.execSQL(CREATE_SUMMONER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LoLStatsContract.ServerEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LoLStatsContract.SummonerEntry.TABLE_NAME+ ";");
        onCreate(sqLiteDatabase);

    }

    public String insertToTable(String serverID, String serverName ){
        return "INSERT INTO " + LoLStatsContract.ServerEntry.TABLE_NAME + "( " + LoLStatsContract.ServerEntry.COLUMN_SERVER_ID + ", " + LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME + ") VALUES('" + serverID + "', '" + serverName + "')";
    }

}
