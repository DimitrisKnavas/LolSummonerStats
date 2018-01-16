package com.example.nick.lolsummonerstatswithfragments.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class StatsProvider extends ContentProvider {

    private static final int SERVERS = 100;
    private static final int SERVER_ID = 101;
    static final int SUMMONERS = 200;
    static final int SUMMONER_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(LoLStatsContract.CONTENT_AUTHORITY,LoLStatsContract.PATH_SERVERS, SERVERS);
        sUriMatcher.addURI(LoLStatsContract.CONTENT_AUTHORITY,LoLStatsContract.PATH_SERVERS + "/#", SERVER_ID);
        sUriMatcher.addURI(LoLStatsContract.CONTENT_AUTHORITY,LoLStatsContract.PATH_SUMMONERS, SUMMONERS);
        sUriMatcher.addURI(LoLStatsContract.CONTENT_AUTHORITY,LoLStatsContract.PATH_SUMMONERS + "/#", SUMMONER_ID);
    }

    private LoLStatsDbHelper helper;
    public static final String LOG_TAG = StatsProvider.class.getSimpleName();


    @Override
    public boolean onCreate() {
        helper = new LoLStatsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match) {
            case SERVERS:
                cursor = database.query(LoLStatsContract.ServerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SERVER_ID:
                selection = LoLStatsContract.ServerEntry.COLUMN_SERVER_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(LoLStatsContract.ServerEntry.TABLE_NAME,projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SUMMONERS:
                cursor = database.query(LoLStatsContract.SummonerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SUMMONER_ID:
                selection = LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(LoLStatsContract.SummonerEntry.TABLE_NAME,projection,selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case SUMMONERS:
                return insertSummoner(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion failed for " + uri);
        }
    }

    private Uri insertSummoner(Uri uri, ContentValues values){
        SQLiteDatabase database = helper.getWritableDatabase();

        long id = database.insert(LoLStatsContract.SummonerEntry.TABLE_NAME,null,values);
        if(id==-1){
            Log.e(LOG_TAG,"Failed to insert row for " + uri);
            return null;
        }
        return  ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case SUMMONERS:
                return updateSummoner(uri,contentValues,selection,selectionArgs);
            case SUMMONER_ID:
                selection = LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return  updateSummoner(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateSummoner(Uri uri,ContentValues values,String selection,String[] selectionArgs){
        if(values.size()==0){
            return 0;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        return database.update(LoLStatsContract.SummonerEntry.TABLE_NAME,values,selection,selectionArgs);
    }
}
