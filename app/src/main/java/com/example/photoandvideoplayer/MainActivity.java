package com.example.photoandvideoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


   ImageView Viewer;
   TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Viewer=findViewById(R.id.Viewer);
        text=findViewById(R.id.text);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag, new Welcome_page());
        transaction.commit();

    }
   public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);

       return true;
   }
   public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.nav_photo:
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                return true;
            case R.id.nav_video:
                Intent j=new Intent(getApplicationContext(),Video.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

   }


}
