package com.example.nick.lolsummonerstatswithfragments.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class LoLStatsContract {

    private LoLStatsContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.nick.lolsummonerstatswithfragments";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SERVERS = LoLStatsContract.ServerEntry.TABLE_NAME;
    public static final String PATH_SUMMONERS = LoLStatsContract.SummonerEntry.TABLE_NAME;

    public static final class ServerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SERVERS);

        public static final String TABLE_NAME = "server";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_SERVER_ID = "server_id";

        public static final String COLUMN_SERVER_NAME = "server_name";
    }

    public static final class SummonerEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SUMMONERS);

        public static final String TABLE_NAME = "summoner";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_SUMMONER_NAME = "summoner_name";

        public static final String COLUMN_SERVER_ID = "server_id";
    }

}
