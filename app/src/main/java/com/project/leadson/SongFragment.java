package com.project.leadson;

import static com.project.leadson.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SongFragment extends Fragment {
    RecyclerView recyclerView;
    static SongListAdapter songListAdapter;

    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_song,container,false);
       recyclerView=view.findViewById(R.id.songListRecyclerView);

       if(musicFiles.size()>0){
           songListAdapter= new SongListAdapter(getContext(),musicFiles);
           recyclerView.setAdapter(songListAdapter);
           recyclerView.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false));
       }
       return view;
    }
}