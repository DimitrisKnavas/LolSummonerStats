package com.example.nick.lolsummonerstatswithfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class DisplayImage extends AsyncTask<String,String,Bitmap>{

    private ImageView imageView;
    private TextView textView;
    private View view;

    public DisplayImage(ImageView imageView)
    {
        this.imageView = imageView;
        this.textView = null;
    }

    public DisplayImage(TextView textView)
    {
        this.textView = textView;
        this.imageView = null;
    }

    public DisplayImage(TextView textView, View view)
    {
        this.textView = textView;
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(strings[0]).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {

        if(textView == null)
        {
            imageView.setImageBitmap(result);
        }
        if(imageView == null)
        {
            Bitmap bitmapResized = Bitmap.createScaledBitmap(result, 150, 150, false);
            Drawable d = new BitmapDrawable(bitmapResized);
            textView.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
            if(view != null)
            {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}

