package com.example.nick.lolsummonerstatswithfragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nick.lolsummonerstatswithfragments.data.LoLStatsContract;


public class DisplaySummonerActivity extends AppCompatActivity {

    private StatsFragment statsFragment;
    private static final String LOG_TAG = DisplaySummonerActivity.class.getSimpleName();
    private static final String LOLSTATS_HASHTAG = " #LOLSUMMONERSTATS";
    private String shareString;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_summoner);

        Intent intent = getIntent();
        String selectedName = intent.getStringExtra("selectedName");
        String selectedServer = intent.getStringExtra("selectedServer");

        statsFragment = new StatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedName",selectedName);
        bundle.putString("selectedServer",selectedServer);
        statsFragment.setArguments(bundle);

        setActionBar(selectedName,selectedServer);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if(fragment == null)
        {
            ft.add(R.id.container, statsFragment);
        } else {
            ft.replace(R.id.container, statsFragment);
        }
        ft.addToBackStack(null);
        ft.commit();


    }

    public void setActionBar(String heading,String subheading) {
        // TODO Auto-generated method stub

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        actionBar.setTitle(heading);
        actionBar.setSubtitle(subheading);
        actionBar.show();

    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        //clear activity and not put it on the activity stack.
        //If this flag is not present, then after sharing the screen will return to the sharing app
        //and not in our app!!!
        //FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET is DEPRECATED, FLAG_ACTIVITY_NEW_DOCUMENT should be used instead
        // shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString + LOLSTATS_HASHTAG);
        Log.e(LOG_TAG, "share intent created");
        Log.e(LOG_TAG, "string extra = "+shareIntent.getStringExtra(Intent.EXTRA_TEXT));

        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(shareActionProvider != null){
            shareActionProvider.setShareIntent(createShareIntent());
            Log.e(LOG_TAG, "intent set to share action provider ");
        }
        else{
            Log.e(LOG_TAG, "Share action provider is null?");
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void insertSummoner(String name, String server){

        boolean alreadyExists = false;
        String savedServer = null;

        String[] mProjection =
                {
                        LoLStatsContract.SummonerEntry.COLUMN_SERVER_ID
                };

        String mSelection = LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME + " =? ";
        String[] mSelectionArgs = {name};
        Cursor cursor1 = getContentResolver().query(LoLStatsContract.SummonerEntry.CONTENT_URI,mProjection, mSelection,mSelectionArgs, null);
        if(cursor1 != null && cursor1.moveToFirst()){
            savedServer = cursor1.getString(cursor1.getColumnIndex(LoLStatsContract.SummonerEntry.COLUMN_SERVER_ID));
            alreadyExists = true;

        }
        cursor1.close();

        ContentValues values = new ContentValues();

        if(alreadyExists){
            if(!savedServer.equalsIgnoreCase(server)){
                values.put(LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME,name);
                values.put(LoLStatsContract.SummonerEntry.COLUMN_SERVER_ID,server);
                String updateSelection = null;
                String[] updateSelectionArgs = null;
                int numOfRowsUpdated = getContentResolver().update(LoLStatsContract.SummonerEntry.CONTENT_URI,values, updateSelection,updateSelectionArgs);


            }

        }
        else{

            values.put(LoLStatsContract.SummonerEntry.COLUMN_SUMMONER_NAME,name);
            values.put(LoLStatsContract.SummonerEntry.COLUMN_SERVER_ID,server);

            Uri newUri = getContentResolver().insert(LoLStatsContract.SummonerEntry.CONTENT_URI,values);

        }
    }

}
