package com.example.photoandvideoplayer;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class Welcome_page extends Fragment {

  ImageView Viewer;
  TextView text;
    Button select_image,crop;
    final int PIC_CROP=1;

    public Welcome_page() {
        // Required empty public constructor
    }
  public void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode,resultCode,data);
      if(requestCode==PIC_CROP){
          Log.d("msg","Cropping of image completed");
          if(data!=null){
              Log.d("msg","Data transfer completed");
             Uri imageUri=data.getData();
             if(imageUri!=null) {

                 Bitmap selectedBitmap =null;
                 try {
                     selectedBitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri);
                 }
                 catch (IOException e){
                     e.printStackTrace();
                 }

                 FileOutputStream outputStream = null;
                 File d = new File("/sdcard/Cropped Image");
                 d.mkdir();
                 String file = String.format("%d.jpg", System.currentTimeMillis());
                 File out = new File(d, file);
                 try {
                     outputStream = new FileOutputStream(out);
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
                 selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                 try {
                     outputStream.flush();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 try {
                     outputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 Toast.makeText(getActivity(), "Cropped Image saved in Cropped Image folder", Toast.LENGTH_LONG).show();
             }
             else{
                 Toast.makeText(getActivity(), "Cropping of image does not possible with this image", Toast.LENGTH_LONG).show();
             }

          }
      }
  }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_welcome_page, container, false);
        Viewer=rootview.findViewById(R.id.Viewer);
        text=rootview.findViewById(R.id.text);
        select_image=rootview.findViewById(R.id.select);
        crop=rootview.findViewById(R.id.crop);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
           String pic=getArguments().getString("Image");
            byte[] byteArray = Base64.decode(pic, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Viewer.setImageBitmap(bitmap);
            text.setVisibility(View.GONE);

        }
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frag, new photo_page());
                transaction.commit();
            }
        });
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if( Viewer.getDrawable()==null){
                   Toast.makeText(getActivity(), "Select Image First", Toast.LENGTH_LONG).show();
               }
               else {
                   if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED){
                       requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                   }
                   else {
                       Bitmap image = ((BitmapDrawable) Viewer.getDrawable()).getBitmap();
                       performCrop(image);

                   }
               }
               }
        });


        // Inflate the layout for this fragment
        return rootview;

    }



    public Uri getImageUri(Context inContext, Bitmap img){
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path= MediaStore.Images.Media.insertImage(inContext.getContentResolver(),img, UUID.randomUUID().toString() + ".png","drawing");
        return Uri.parse(path);
    }
    private void performCrop(Bitmap pic) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            Uri contentUri=getImageUri(getActivity(),pic);
            cropIntent.setDataAndType(contentUri, "image/*");

            cropIntent.putExtra("crop", true);

            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);

            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);

            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }




}
