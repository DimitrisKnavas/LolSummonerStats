package com.example.nick.lolsummonerstatswithfragments;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.nick.lolsummonerstatswithfragments.data.LoLStatsContract;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner serverOptions;
    private EditText searchNameText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverOptions = findViewById(R.id.serverSpinner);
        searchNameText = findViewById(R.id.summonerNameText);
        searchButton = findViewById(R.id.searchButton);

        List<String> servers = new ArrayList<>();
        servers.add("Select Server");

        final String[] projection = {
                LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME
        };

        Cursor cursor = getContentResolver().query(LoLStatsContract.ServerEntry.CONTENT_URI,projection,null,null, LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME + " ASC");
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String temp_server_name = cursor.getString(cursor.getColumnIndex(LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME));
                servers.add(temp_server_name);
                cursor.moveToNext();
            }
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,servers);
        serverOptions.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(serverOptions.getSelectedItemPosition() != 0)
                {
                    String selectedServer=null;
                    String[] mProjection =
                            {
                                    LoLStatsContract.ServerEntry.COLUMN_SERVER_ID
                            };

                    String mSelection = LoLStatsContract.ServerEntry.COLUMN_SERVER_NAME + " =? ";
                    String[] mSelectionArgs = {serverOptions.getSelectedItem().toString()};
                    Cursor cursor1 = getContentResolver().query(LoLStatsContract.ServerEntry.CONTENT_URI,mProjection, mSelection,mSelectionArgs, null);
                    if( cursor1 != null && cursor1.moveToFirst() ){
                        selectedServer = cursor1.getString(cursor1.getColumnIndex(LoLStatsContract.ServerEntry.COLUMN_SERVER_ID));
                        cursor1.close();
                    }

                    Intent intent = new Intent(MainActivity.this,DisplaySummonerActivity.class);
                    intent.putExtra("selectedName",String.valueOf(searchNameText.getText()));
                    Log.i("test",String.valueOf(searchNameText.getText()));
                    intent.putExtra("selectedServer",selectedServer);
                    startActivity(intent);

                }
                else
                {
                    Toast toast = Toast.makeText(MainActivity.this
                            , "Select server and try again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
