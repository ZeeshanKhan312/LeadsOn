package com.project.leadson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int RequestCode=1;
    public static ArrayList<MusicFiles> musicFiles;
    private String MY_SORT_PREF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission(); //For checking storage permission
    }

    //For checking storage permission
    public void permission(){
        //if not granted
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, RequestCode);
        }
        else{ //if granted permission to read and write
            musicFiles= getAllAudio(this);
            initViewPager();
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    // the main function which will check the permission of phone settings
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(RequestCode==requestCode) { //if permission granted
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission();
            } else { //if denied so repeated call until allowed
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, RequestCode);
            }
        }
    }


    //for Initializing all different View pager
    public void initViewPager(){
        ViewPager viewPager= findViewById(R.id.fragment_container);
        TabLayout tabLayout= findViewById(R.id.tab);

        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongFragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Album");
        viewPagerAdapter.addFragments(new ProfileFragment(), "Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //will fetch all the songs from the storage
    public  ArrayList<MusicFiles> getAllAudio(Context context){
        ArrayList<MusicFiles> temp_audioList= new ArrayList<>();
        String sortOrder=null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, //for path
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(uri, projection,null,null,sortOrder);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist=cursor.getString(4);
                //will add all songs one by one
                MusicFiles musicFiles = new MusicFiles(path,title, artist, album, duration);
                temp_audioList.add(musicFiles);

            }
            cursor.close();
        }

        return temp_audioList;
    }

}