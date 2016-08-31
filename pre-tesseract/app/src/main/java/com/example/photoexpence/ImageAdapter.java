package com.example.photoexpence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by breck on 8/25/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] filepath;
    private String[] filename;
    private static LayoutInflater inflator =null;
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.question,
            R.drawable.coffee_mug,
            R.drawable.bike,
            R.drawable.be_random,
            R.drawable.dice,
            R.drawable.gumballs,
            R.drawable.rubics,

    };

    // Constructor
    public ImageAdapter(Context c, String[] fpath, String[] fname){
        mContext = c;
        filename = fname;
        Log.d("fname len ", Integer.toString(fname.length));
        Log.d("filename len ", Integer.toString(filename.length));

        filepath = fpath;
        inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.d("file path len:", Integer.toString(filepath.length));
        return filepath.length;//mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return position;//mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position; //0
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if(convertView ==null) {
            Log.d("Debug","");
            vi = inflator.inflate(R.layout.gridview_item, null);
        }
            TextView text = (TextView)vi.findViewById(R.id.text);
            ImageView image = (ImageView)vi.findViewById(R.id.image);
             image.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
           image.setLayoutParams(new RelativeLayout.LayoutParams(150, 150)); //--Change these numbers to increase the size of the images
            text.setText(filename[position]);

             BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =2;
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position], options);
            image.setImageBitmap(bmp);
            return vi;



        /*android.widget.ImageView imageView = new android.widget.ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(300, 300)); //--Change these numbers to increase the size of the images
        return imageView;*/
    }

}
