package com.example.photoandvideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

class CustomGrid extends BaseAdapter {
    private Context Grid_Page;
    private final Integer[] Imageid;

    public CustomGrid(Context photo_page, Integer[] items) {
        Grid_Page = photo_page;
        this.Imageid = items;
    }

    @Override
    public int getCount() {
        return Imageid.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) Grid_Page
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            grid = new View(Grid_Page);
            grid = inflater.inflate(R.layout.grid_single, null);

            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

            imageView.setImageResource(Imageid[i]);
        } else {
            grid = (View) view;
        }

        return grid;
    }
}
