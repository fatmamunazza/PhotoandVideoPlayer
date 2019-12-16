package com.example.photoandvideoplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class photo_page extends Fragment implements AdapterView.OnClickListener {
     GridView gridCont;
     Button btn;
    public photo_page() {
        // Required empty public constructor
    }
    private static final Integer[] items={R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_photo_page, container, false);
        gridCont=(GridView)rootview.findViewById(R.id.gridCont);
        btn=rootview.findViewById(R.id.select_from_gallery);
        gridCont.setAdapter(new CustomGrid(getActivity(),items));
        gridCont.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             /*   FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frag, new Welcome_page());
                transaction.commit();*/

               Welcome_page photo_frag=new Welcome_page();
                Bundle args=new Bundle();
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),items[i]);
                args.putString("Image",encode(bitmap));
                photo_frag.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.frag,photo_frag).commit();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED &&  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED){

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    }
                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }


        });
        // Inflate the layout for this fragment
        return rootview;
    }
    private void pickImageFromGallery() {
        Intent I=new Intent(Intent.ACTION_PICK);
        I.setType("image/*");
        startActivityForResult(I,1000);
    }

    @Override
    public void onClick(View view) {


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1001:{
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1000 && data!=null){
            final Uri uri=data.getData();
            try {

                Welcome_page photo_frag=new Welcome_page();
                Bundle args=new Bundle();
                final InputStream str=getActivity().getContentResolver().openInputStream(uri);
                final Bitmap bitmap= BitmapFactory.decodeStream(str);
                args.putString("Image",encode(bitmap));
                photo_frag.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.frag,photo_frag).commit();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getActivity(),"You haven't picked image",Toast.LENGTH_LONG).show();
        }
    }

    public  static  String encode(Bitmap Img){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Img.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] b=byteArrayOutputStream.toByteArray();
        String Encode= Base64.encodeToString(b,Base64.DEFAULT);
        return Encode;
    }

}
